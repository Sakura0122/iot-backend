package com.sakura.model.dto.system.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "角色菜单变更")
public class SysRoleMenuDTO {

    @Schema(description = "菜单id数组")
    private List<Long> menuIds;

    @Schema(description = "角色id")
    @NotNull(message = "角色id不能为空")
    private Long roleId;

}
