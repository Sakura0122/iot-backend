package com.sakura.model.vo.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: sakura
 * @date: 2025/2/11 17:55
 * @description:
 */
@Data
@Schema(description = "菜单VO")
public class SysMenuVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "所属上级")
    private Long parentId;

    @Schema(description = "类型(1:菜单,2:按钮)")
    private Integer type;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "组件名称")
    private String component;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "排序")
    private Long sortValue;

    @Schema(description = "状态(0:禁止,1:正常)")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    private List<SysMenuVo> children;
}
