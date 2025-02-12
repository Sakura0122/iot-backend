package com.sakura.model.dto.system.role;

import com.sakura.common.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色搜索对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "角色搜索对象")
public class SysRoleListDto extends PageDto {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

}
