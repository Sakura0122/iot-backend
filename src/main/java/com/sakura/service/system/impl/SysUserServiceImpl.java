package com.sakura.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.common.PageVo;
import com.sakura.common.ResultCodeEnum;
import com.sakura.constant.RedisConstant;
import com.sakura.exception.SakuraException;
import com.sakura.mapper.system.SysRoleMapper;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserRoleDTO;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysRole;
import com.sakura.model.po.system.SysUser;
import com.sakura.model.po.system.SysUserRole;
import com.sakura.model.vo.system.SysUserRoleVo;
import com.sakura.model.vo.system.SysUserVo;
import com.sakura.model.vo.system.UserInfoVo;
import com.sakura.service.system.SysUserRoleService;
import com.sakura.service.system.SysUserService;
import com.sakura.mapper.system.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author sakura
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2025-02-11 17:32:51
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper roleMapper;
    private final SysUserRoleService userRoleService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public PageVo<SysUserVo> getUserList(SysUserListDto userDto) {
        // 1. 获取分页查询条件
        Page<SysUser> p = userDto.toMpPageDefaultSortByUpdateTimeDesc();

        // 2.查询
        Page<SysUser> page = lambdaQuery()
                .between(StrUtil.isNotBlank(userDto.getBeginTime()) && StrUtil.isNotBlank(userDto.getEndTime()),
                        SysUser::getCreateTime, userDto.getBeginTime(), userDto.getEndTime())
                .and(StrUtil.isNotBlank(userDto.getKeyword()), wrapper -> wrapper
                        .like(SysUser::getUsername, userDto.getKeyword())
                        .or()
                        .like(SysUser::getName, userDto.getKeyword())
                        .or()
                        .like(SysUser::getPhone, userDto.getKeyword())
                )
                .page(p);

        // 3.返回
        return PageVo.of(page, SysUserVo.class);
    }

    @Override
    public void addUser(SysUserAddDto userDto) {
        // 1.密码加密
        String hashpw = BCrypt.hashpw(userDto.getPassword());
        userDto.setPassword(hashpw);

        // 2.保存
        SysUser sysUser = BeanUtil.copyProperties(userDto, SysUser.class);
        boolean result = this.save(sysUser);

        // 3.判断是否成功
        if (!result) {
            throw new SakuraException(ResultCodeEnum.FAIL);
        }
    }

    @Override
    public void updateUser(SysUserUpdateDto userDto) {
        // 1.拷贝
        SysUser sysUser = BeanUtil.copyProperties(userDto, SysUser.class);

        // 2.更新
        boolean result = this.updateById(sysUser);

        // 3.判断是否成功
        if (!result) {
            throw new SakuraException(ResultCodeEnum.FAIL);
        }
    }

    @Override
    public SysUserRoleVo getUserRole(Long userId) {
        // 1.查询所有角色
        List<SysRole> roles = roleMapper.selectList(null);
        List<SysUserRoleVo.RoleList> roleList = BeanUtil.copyToList(roles, SysUserRoleVo.RoleList.class);

        // 2.查询用户的角色id
        List<Long> selectIds = new ArrayList<>();
        List<SysUserRole> userRolesList = userRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .list();
        if (CollUtil.isNotEmpty(userRolesList)) {
            selectIds = userRolesList.stream().map(SysUserRole::getRoleId).toList();
        }

        // 3.返回
        SysUserRoleVo sysUserRoleVo = new SysUserRoleVo();
        sysUserRoleVo.setRoleList(roleList);
        sysUserRoleVo.setSelectIds(selectIds);
        return sysUserRoleVo;
    }

    @Override
    @Transactional
    public void updateUserRole(SysUserRoleDTO userRoleDTO) {
        // 0.更新用户权限
        this.updateUserPermissions(userRoleDTO.getUserId());

        // 1.删除用户所有角色
        userRoleService.lambdaUpdate()
                .eq(SysUserRole::getUserId, userRoleDTO.getUserId())
                .remove();

        if (CollUtil.isNotEmpty(userRoleDTO.getRoleIds())) {
            // 2.添加用户角色
            List<SysUserRole> list = userRoleDTO.getRoleIds().stream().map(roleId -> {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userRoleDTO.getUserId());
                sysUserRole.setRoleId(roleId);
                return sysUserRole;
            }).toList();

            userRoleService.saveBatch(list);
        }
    }

    @Override
    public UserInfoVo getUserInfo(Long userId) {
        // 1.查询用户信息
        SysUser sysUser = lambdaQuery()
                .eq(SysUser::getId, userId)
                .one();

        // 2.查询权限
        List<String> permissionList = this.getPermissionList(userId);
        List<String> menuList = this.getMenuList(userId);
        List<String> roleList = this.getRoleList(userId);

        // 3.返回
        UserInfoVo userInfoVo = BeanUtil.copyProperties(sysUser, UserInfoVo.class);
        userInfoVo.setRoles(roleList);
        userInfoVo.setPermissions(permissionList);
        userInfoVo.setMenus(menuList);
        return userInfoVo;
    }

    /**
     * 获取用户权限
     *
     * @param userId 用户id
     * @return 用户权限数组
     */
    public List<String> getPermissionList(Long userId) {
        String key = RedisConstant.PERMISSION_CACHE_PREFIX + userId.toString();
        List<String> userPermissionCode = JSONUtil.toBean(redisTemplate.opsForValue().get(key), new TypeReference<>() {
                },
                false);
        if (CollUtil.isEmpty(userPermissionCode)) {
            userPermissionCode = baseMapper.getUserPermsCode(userId).stream().filter(StrUtil::isNotBlank).toList();
            redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userPermissionCode), 30, TimeUnit.MINUTES);
        }
        return userPermissionCode;
    }

    /**
     * 获取用户菜单
     *
     * @param userId 用户id
     * @return 用户菜单数组
     */
    public List<String> getMenuList(Long userId) {
        String key = RedisConstant.MENU_CACHE_PREFIX + userId.toString();
        List<String> userMenuCode = JSONUtil.toBean(redisTemplate.opsForValue().get(key), new TypeReference<>() {
                },
                false);
        if (CollUtil.isEmpty(userMenuCode)) {
            userMenuCode = baseMapper.getUserComponentCode(userId);
            redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userMenuCode), 30, TimeUnit.MINUTES);
        }
        return userMenuCode;
    }

    /**
     * 获取用户角色
     *
     * @param userId 用户id
     * @return 用户角色数组
     */
    public List<String> getRoleList(Long userId) {
        String key = RedisConstant.ROLE_CACHE_PREFIX + userId.toString();
        List<String> userRoleCode = JSONUtil.toBean(redisTemplate.opsForValue().get(key), new TypeReference<>() {
                },
                false);
        if (CollUtil.isEmpty(userRoleCode)) {
            userRoleCode = baseMapper.getUserRoleCode(userId);
            redisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userRoleCode), 30, TimeUnit.MINUTES);
        }
        return userRoleCode;
    }

    /**
     * 更新用户权限
     * @param userId 用户id
     */
    public void updateUserPermissions(Long userId) {
        redisTemplate.delete(RedisConstant.PERMISSION_CACHE_PREFIX + userId.toString());
        redisTemplate.delete(RedisConstant.MENU_CACHE_PREFIX + userId.toString());
        redisTemplate.delete(RedisConstant.ROLE_CACHE_PREFIX + userId.toString());
    }

    /**
     * 更新用户权限
     * @param userIds 用户ids
     */
    public void updateUserPermissions(List<Long> userIds) {
        // 生成所有待删除的 Redis 键
        Set<String> keys = userIds.stream()
                .flatMap(userId -> Stream.of(
                        RedisConstant.PERMISSION_CACHE_PREFIX + userId.toString(),
                        RedisConstant.MENU_CACHE_PREFIX + userId.toString(),
                        RedisConstant.ROLE_CACHE_PREFIX + userId.toString()
                ))
                .collect(Collectors.toSet());

        // 批量删除
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
