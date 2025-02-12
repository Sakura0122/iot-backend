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
@Schema(description = "用户的角色VO")
public class SysUserRoleVo {

    @Schema(description = "选中id数组")
    private List<Long> selectIds;

    @Schema(description = "角色信息数组")
    private List<RoleList> roleList;

    @Data
    public static class RoleList {

        @Schema(description = "角色id")
        private Long id;

        @Schema(description = "角色名称")
        private String roleName;

        @Schema(description = "角色编码")
        private String roleCode;
    }
}
