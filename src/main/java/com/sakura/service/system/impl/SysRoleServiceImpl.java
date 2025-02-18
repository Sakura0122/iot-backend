package com.sakura.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.common.PageVo;
import com.sakura.model.dto.system.role.SysRoleListDto;
import com.sakura.model.dto.system.role.SysRoleMenuDTO;
import com.sakura.model.po.system.SysRole;
import com.sakura.model.po.system.SysRoleMenu;
import com.sakura.model.po.system.SysUserRole;
import com.sakura.model.vo.system.SysMenuVo;
import com.sakura.model.vo.system.SysRoleMenuVo;
import com.sakura.model.vo.system.SysRoleVo;
import com.sakura.service.system.*;
import com.sakura.mapper.system.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sakura
 * @description 针对表【sys_role(角色)】的数据库操作Service实现
 * @createDate 2025-02-11 17:32:51
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysMenuService menuService;
    private final SysUserRoleService userRoleService;
    private final SysRoleMenuService roleMenuService;
    private final SysUserService userService;

    @Override
    public PageVo<SysRoleVo> getRoleList(SysRoleListDto roleListDto) {
        // 1.分页参数
        Page<SysRole> p = roleListDto.toMpPageDefaultSortByUpdateTimeDesc();

        // 2.查询
        Page<SysRole> page = lambdaQuery()
                .like(StrUtil.isNotBlank(roleListDto.getKeyword()), SysRole::getRoleName, roleListDto.getKeyword())
                .or()
                .like(StrUtil.isNotBlank(roleListDto.getKeyword()), SysRole::getRoleCode, roleListDto.getKeyword())
                .page(p);

        // 3.返回
        return PageVo.of(page, SysRoleVo.class);
    }

    @Override
    public SysRoleMenuVo getRoleMenu(Long roleId) {
        // 1.查询全部菜单
        List<SysMenuVo> menuList = menuService.getMenuList();

        // 2.查询角色的菜单id
        List<Long> selectIds = new ArrayList<>();
        List<SysRoleMenu> roleMenuList = roleMenuService.lambdaQuery()
                .eq(SysRoleMenu::getRoleId, roleId)
                .list();
        if (CollUtil.isNotEmpty(roleMenuList)) {
            selectIds = roleMenuList.stream().map(SysRoleMenu::getMenuId).toList();
        }

        // 3.返回
        SysRoleMenuVo sysRoleMenuVo = new SysRoleMenuVo();
        sysRoleMenuVo.setMenuList(menuList);
        sysRoleMenuVo.setSelectIds(selectIds);
        return sysRoleMenuVo;
    }

    @Override
    @Transactional
    public void updateRoleMenu(SysRoleMenuDTO roleMenuDTO) {
        // 1.删除角色的菜单
        roleMenuService.lambdaUpdate()
                .eq(SysRoleMenu::getRoleId, roleMenuDTO.getRoleId())
                .remove();

        if (CollUtil.isNotEmpty(roleMenuDTO.getMenuIds())) {
            // 2.添加角色的菜单
            List<SysRoleMenu> list = roleMenuDTO.getMenuIds().stream().map(menuId -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleMenuDTO.getRoleId());
                sysRoleMenu.setMenuId(menuId);
                return sysRoleMenu;
            }).toList();

            roleMenuService.saveBatch(list);
        }

        List<SysUserRole> userRoles = userRoleService.lambdaQuery()
                .eq(SysUserRole::getRoleId, roleMenuDTO.getRoleId())
                .list();
        if (CollUtil.isNotEmpty(userRoles)) {
            // 3.更新用户角色的缓存
            userService.updateUserPermissions(userRoles.stream().map(SysUserRole::getUserId).toList());
            // userRoles.forEach(userRole -> {
            //     Long userId = userRole.getUserId();
            //     userService.updateUserPermissions(userId);
            // });
        }
    }
}




