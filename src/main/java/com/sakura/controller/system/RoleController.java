package com.sakura.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import com.sakura.common.PageVo;
import com.sakura.common.Result;
import com.sakura.model.dto.system.role.SysRoleAddDto;
import com.sakura.model.dto.system.role.SysRoleListDto;
import com.sakura.model.dto.system.role.SysRoleMenuDTO;
import com.sakura.model.dto.system.role.SysRoleUpdateDto;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserRoleDTO;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysRole;
import com.sakura.model.po.system.SysUser;
import com.sakura.model.vo.system.SysRoleMenuVo;
import com.sakura.model.vo.system.SysRoleVo;
import com.sakura.model.vo.system.SysUserRoleVo;
import com.sakura.model.vo.system.SysUserVo;
import com.sakura.service.system.SysRoleService;
import com.sakura.service.system.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author: sakura
 * @date: 2025/2/11 16:02
 * @description:
 */
@RestController
@RequestMapping("/system/role")
@Tag(name = "角色管理")
@Validated
public class RoleController {

    @Resource
    private SysRoleService roleService;

    @GetMapping
    @Operation(summary = "分页查询角色列表")
    @SaCheckPermission("sysRole.list")
    public Result<PageVo<SysRoleVo>> list(SysRoleListDto roleListDto) {
        PageVo<SysRoleVo> list = roleService.getRoleList(roleListDto);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询角色详细信息")
    @SaCheckPermission("sysRole.detail")
    public Result<SysRoleVo> getUserDetailById(@PathVariable Long id) {
        SysRole sysRole = roleService.getById(id);
        SysRoleVo sysRoleVo = BeanUtil.copyProperties(sysRole, SysRoleVo.class);
        return Result.success(sysRoleVo);
    }

    @PostMapping
    @Operation(summary = "新增角色")
    @SaCheckPermission("sysRole.add")
    public Result<Void> addUser(@Validated @RequestBody SysRoleAddDto roleAddDto) {
        roleService.save(BeanUtil.copyProperties(roleAddDto, SysRole.class));
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改角色")
    @SaCheckPermission("sysRole.update")
    public Result<Void> updateRole(@Validated @RequestBody SysRoleUpdateDto roleUpdateDto) {
        roleService.updateById(BeanUtil.copyProperties(roleUpdateDto, SysRole.class));
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "删除角色")
    @SaCheckPermission("sysRole.delete")
    public Result<Void> deleteUser(@PathVariable String ids) {
        roleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

    @GetMapping("/menu/{roleId}")
    @Operation(summary = "查询角色菜单")
    public Result<SysRoleMenuVo> getRoleMenu(@PathVariable Long roleId) {
        SysRoleMenuVo sysRoleMenuVo = roleService.getRoleMenu(roleId);
        return Result.success(sysRoleMenuVo);
    }

    @PutMapping("/menu")
    @Operation(summary = "修改角色菜单")
    @SaCheckPermission("sysRole.assignMenu")
    public Result<Void> updateUserRole(@Validated @RequestBody SysRoleMenuDTO roleMenuDTO) {
        roleService.updateRoleMenu(roleMenuDTO);
        return Result.success();
    }
}
