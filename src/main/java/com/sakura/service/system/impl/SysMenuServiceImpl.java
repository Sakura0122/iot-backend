package com.sakura.service.system.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sakura.constant.RedisConstant;
import com.sakura.model.dto.system.menu.SysMenuAddDto;
import com.sakura.model.dto.system.menu.SysMenuUpdateDto;
import com.sakura.model.po.system.SysMenu;
import com.sakura.model.po.system.SysUser;
import com.sakura.model.vo.system.SysMenuVo;
import com.sakura.service.system.SysMenuService;
import com.sakura.mapper.system.SysMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sakura
 * @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
 * @createDate 2025-02-11 17:32:51
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public List<SysMenuVo> getMenuList() {
        // 1.获取所有菜单
        List<SysMenu> list = lambdaQuery()
                .orderByAsc(SysMenu::getSortValue)
                .list();

        // 2.遍历所有菜单，构造菜单树
        List<SysMenuVo> treeList = new ArrayList<>();
        for (SysMenuVo rootNode : this.getRootNodes(list)) {
            SysMenuVo sysMenuVo = BeanUtil.copyProperties(rootNode, SysMenuVo.class);
            SysMenuVo childrenNode = this.getChildrenNode(sysMenuVo, list);
            treeList.add(childrenNode);
        }
        return treeList;
    }

    @Override
    public void deleteMenuCache() {
        redisTemplate.delete(RedisConstant.MENU_CACHE_PREFIX + "*");
        redisTemplate.delete(RedisConstant.PERMISSION_CACHE_PREFIX + "*");
    }

    /**
     * 获取父级跟节点
     *
     * @param list 菜单列表
     * @return 父级跟节点菜单列表
     */
    private List<SysMenuVo> getRootNodes(List<SysMenu> list) {
        List<SysMenuVo> rootList = new ArrayList<>();
        for (SysMenu sysMenu : list) {
            // 找到所有父级节点
            if (sysMenu.getParentId() == 0) {
                SysMenuVo sysMenuTreeVO = BeanUtil.copyProperties(sysMenu, SysMenuVo.class);
                rootList.add(sysMenuTreeVO);
            }
        }
        return rootList;
    }

    private SysMenuVo getChildrenNode(SysMenuVo sysMenu, List<SysMenu> menuList) {
        List<SysMenuVo> childrenList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (menu.getParentId().equals(sysMenu.getId())) {
                SysMenuVo childrenNode = BeanUtil.copyProperties(menu, SysMenuVo.class);
                childrenList.add(getChildrenNode(childrenNode, menuList));
            }
        }
        sysMenu.setChildren(childrenList);
        return sysMenu;
    }
}




