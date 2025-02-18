package com.sakura.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import com.sakura.common.Result;
import com.sakura.common.ResultCodeEnum;
import com.sakura.exception.SakuraException;
import com.sakura.model.dto.system.menu.SysMenuAddDto;
import com.sakura.model.dto.system.menu.SysMenuUpdateDto;
import com.sakura.model.po.system.SysMenu;
import com.sakura.model.vo.system.SysMenuVo;
import com.sakura.service.system.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author: sakura
 * @date: 2025/2/11 16:02
 * @description:
 */
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
@Tag(name = "菜单管理")
@Validated
public class MenuController {

    private final SysMenuService menuService;

    @GetMapping
    @Operation(summary = "列表查询")
    @SaCheckPermission("sys.menu.query")
    public Result<List<SysMenuVo>> list() {
        return Result.success(menuService.getMenuList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询菜单信息")
    @SaCheckPermission("sys.menu.detail")
    public Result<SysMenuVo> getMenuDetailById(@PathVariable String id) {
        SysMenu sysMenu = menuService.getById(id);
        return Result.success(BeanUtil.copyProperties(sysMenu, SysMenuVo.class));
    }

    @PostMapping
    @Operation(summary = "新增菜单")
    @SaCheckPermission("sys.menu.add")
    public Result<Void> addMenu(@Validated @RequestBody SysMenuAddDto menuAddDto) {
        menuService.save(BeanUtil.copyProperties(menuAddDto, SysMenu.class));
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "修改菜单")
    @SaCheckPermission("sys.menu.update")
    public Result<Void> updateMenu(@Validated @RequestBody SysMenuUpdateDto menuUpdateDto) {
        boolean result = menuService.updateById(BeanUtil.copyProperties(menuUpdateDto, SysMenu.class));
        if (result) {
            menuService.deleteMenuCache();
            return Result.success();
        } else {
            throw new SakuraException(ResultCodeEnum.FAIL);
        }
    }

    @DeleteMapping("/{ids}")
    @Operation(summary = "删除菜单")
    @SaCheckPermission("sys.menu.delete")
    public Result<Void> deleteUser(@PathVariable String ids) {
        boolean result = menuService.removeByIds(Arrays.asList(ids.split(",")));
        if (result) {
            menuService.deleteMenuCache();
            return Result.success();
        } else {
            throw new SakuraException(ResultCodeEnum.FAIL);
        }
    }

}
