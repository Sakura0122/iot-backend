package com.sakura.service.system;

import com.sakura.model.dto.system.menu.SysMenuAddDto;
import com.sakura.model.dto.system.menu.SysMenuUpdateDto;
import com.sakura.model.dto.system.user.SysUserAddDto;
import com.sakura.model.dto.system.user.SysUserUpdateDto;
import com.sakura.model.po.system.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sakura.model.vo.system.SysMenuVo;

import java.util.List;

/**
* @author sakura
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2025-02-11 17:32:51
*/
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单列表
     * @return 菜单列表
     */
    List<SysMenuVo> getMenuList();

    /**
     * 删除菜单缓存
     */
    void deleteMenuCache();
}
