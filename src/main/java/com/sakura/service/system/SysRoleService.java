package com.sakura.service.system;

import com.sakura.common.PageVo;
import com.sakura.model.dto.system.role.SysRoleListDto;
import com.sakura.model.dto.system.role.SysRoleMenuDTO;
import com.sakura.model.po.system.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.model.vo.system.SysRoleMenuVo;
import com.sakura.model.vo.system.SysRoleVo;

/**
* @author sakura
* @description 针对表【sys_role(角色)】的数据库操作Service
* @createDate 2025-02-11 17:32:51
*/
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     * @param roleListDto 查询条件
     * @return 角色列表
     */
    PageVo<SysRoleVo> getRoleList(SysRoleListDto roleListDto);

    /**
     * 查询角色菜单信息
     * @param roleId 角色id
     * @return 菜单信息
     */
    SysRoleMenuVo getRoleMenu(Long roleId);

    /**
     * 更新角色菜单信息
     * @param roleMenuDTO 角色菜单信息
     */
    void updateRoleMenu(SysRoleMenuDTO roleMenuDTO);
}
