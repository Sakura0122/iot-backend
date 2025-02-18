package com.sakura.service.system;

import com.sakura.common.PageVo;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserRoleDTO;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.model.vo.system.SysUserRoleVo;
import com.sakura.model.vo.system.SysUserVo;
import com.sakura.model.vo.system.UserInfoVo;

import java.util.List;

/**
* @author sakura
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-02-11 17:32:51
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     * @param userDto 查询条件
     * @return 用户列表
     */
    PageVo<SysUserVo> getUserList(SysUserListDto userDto);

    /**
     * 新增用户
     *
     * @param userDto 用户信息
     */
    void addUser(SysUserAddDto userDto);

    /**
     * 修改用户
     * @param userDto 用户信息
     */
    void updateUser(SysUserUpdateDto userDto);

    /**
     * 查询用户角色
     * @param userId 用户ID
     * @return 用户角色
     */
    SysUserRoleVo getUserRole(Long userId);

    /**
     * 修改用户角色
     * @param userRoleDTO 用户角色信息
     */
    void updateUserRole(SysUserRoleDTO userRoleDTO);

    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVo getUserInfo(Long userId);

    /**
     * 更新redis中用户权限
     * @param userId 用户id
     */
    void updateUserPermissions(Long userId);

    /**
     * 更新redis中用户权限
     * @param userIds 用户ids
     */
    void updateUserPermissions(List<Long> userIds);

    /**
     * 根据用户ID查询用户权限
     * @param userId 用户id
     * @return 用户权限
     */
    List<String> getPermissionList(Long userId);

    /**
     * 根据用户ID查询用户菜单
     * @param userId 用户id
     * @return 用户菜单
     */
    List<String> getMenuList(Long userId);

    /**
     * 根据用户ID查询用户角色
     * @param userId 用户id
     * @return 用户角色
     */
    List<String> getRoleList(Long userId);
}
