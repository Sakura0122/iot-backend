package com.sakura.model.vo.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author: sakura
 * @date: 2025/2/11 17:55
 * @description:
 */
@Data
@Schema(description = "角色的菜单VO")
public class SysRoleMenuVo {

    @Schema(description = "菜单列表")
    private List<menuList> menuList;

    @Schema(description = "选中的菜单id")
    private List<Long> selectIds;

    @Data
    public static class menuList {

        @Schema(description = "菜单id")
        private Long id;

        @Schema(description = "菜单名称")
        private String title;

        private List<menuList> children;
    }
}
