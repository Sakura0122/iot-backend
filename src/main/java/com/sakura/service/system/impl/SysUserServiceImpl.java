package com.sakura.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.common.PageVo;
import com.sakura.common.ResultCodeEnum;
import com.sakura.exception.SakuraException;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserRoleDTO;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysRole;
import com.sakura.model.po.system.SysUser;
import com.sakura.model.po.system.SysUserRole;
import com.sakura.model.vo.system.SysUserRoleVo;
import com.sakura.model.vo.system.SysUserVo;
import com.sakura.service.system.SysRoleService;
import com.sakura.service.system.SysUserRoleService;
import com.sakura.service.system.SysUserService;
import com.sakura.mapper.system.SysUserMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sakura
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2025-02-11 17:32:51
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleService roleService;
    private final SysUserRoleService userRoleService;

    @Override
    public PageVo<SysUserVo> getUserList(SysUserListDto userDto) {
        // 1. 获取分页查询条件
        Page<SysUser> p = userDto.toMpPageDefaultSortByUpdateTimeDesc();

        // 2.查询
        Page<SysUser> page = lambdaQuery()
                .eq(SysUser::getStatus, 1)
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
    public SysUserRoleVo getUserRole(String userId) {
        // 1.查询所有角色
        List<SysRole> roles = roleService.list();
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
    public void updateUserRole(SysUserRoleDTO userRoleDTO) {
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
}
