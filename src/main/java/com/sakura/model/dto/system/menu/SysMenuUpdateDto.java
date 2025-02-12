package com.sakura.model.dto.system.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "编辑菜单")
public class SysMenuUpdateDto {

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "父级id")
    @NotNull(message = "父级id不能为空")
    private Long parentId;

    @Schema(description = "类型(1:菜单,2:按钮)")
    private Integer type;

    @Schema(description = "菜单标题")
    @NotBlank(message = "菜单标题不能为空")
    private String title;

    @Schema(description = "组件名称")
    private String component;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "排序")
    private Integer sortValue;

    @Schema(description = "状态(0:禁止,1:正常)")
    private Integer status;

}
