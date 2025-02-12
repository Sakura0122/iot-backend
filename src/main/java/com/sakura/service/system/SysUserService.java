package com.sakura.service.system;

import com.sakura.common.PageVo;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserListDto;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.model.vo.system.SysUserVo;

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
}
