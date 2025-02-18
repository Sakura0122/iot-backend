package com.sakura.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.sakura.common.PageVo;
import com.sakura.common.Result;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserRoleDTO;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysUser;
import com.sakura.model.vo.system.SysUserRoleVo;
import com.sakura.model.vo.system.SysUserVo;
import com.sakura.model.vo.system.UserInfoVo;
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
@RequestMapping("/system/user")
@Tag(name = "用户管理")
@Validated
public class UserController {

    @Resource
    private SysUserService userService;

    @GetMapping
    @Operation(summary = "分页查询用户列表")
    @SaCheckPermission("sysUser.list")
    public Result<PageVo<SysUserVo>> list(SysUserListDto userDto) {
        PageVo<SysUserVo> list = userService.getUserList(userDto);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "用户详情")
    @SaCheckPermission("sysUser.detail")
    public Result<SysUserVo> getUserDetailById(@PathVariable Long id) {
        SysUser sysUser = userService.getById(id);
        SysUserVo sysUserVo = BeanUtil.copyProperties(sysUser, SysUserVo.class);
        return Result.success(sysUserVo);
    }

    @GetMapping("/userInfo")
    @Operation(summary = "用户信息")
    @SaCheckLogin
    public Result<UserInfoVo> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(userService.getUserInfo(userId));
    }

    @PostMapping
    @Operation(summary = "新增用户")
    @SaCheckPermission("sysUser.add")
    public Result<Void> addUser(@Validated @RequestBody SysUserAddDto userDto) {
        userService.addUser(userDto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改用户")
    @SaCheckPermission("sysUser.update")
    public Result<Void> updateUser(@Validated @RequestBody SysUserUpdateDto userDto) {
        userService.updateUser(userDto);
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "删除用户")
    @SaCheckPermission("sysUser.delete")
    public Result<Void> deleteUser(@PathVariable String ids) {
        userService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.success();
    }

    @GetMapping("/role/{userId}")
    @Operation(summary = "查询用户角色")
    public Result<SysUserRoleVo> getUserRole(@PathVariable Long userId) {
        SysUserRoleVo sysUserRoleVo = userService.getUserRole(userId);
        return Result.success(sysUserRoleVo);
    }

    @PutMapping("/role")
    @Operation(summary = "修改用户角色")
    @SaCheckPermission("sysUser.assignRole")
    public Result<Void> updateUserRole(@Validated @RequestBody SysUserRoleDTO userRoleDTO) {
        userService.updateUserRole(userRoleDTO);
        return Result.success();
    }

}
