package com.sakura.model.dto.system.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户角色变更")
public class SysUserRoleDTO {

    @Schema(description = "角色id数组")
    private List<Long> roleIds;

    @Schema(description = "用户id")
    @Min(value = 1, message = "用户id不能为空")
    private Long userId;

}
