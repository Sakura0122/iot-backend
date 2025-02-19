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
@Schema(description = "用户信息VO")
public class UserInfoVo {

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "角色编码")
    private List<String> roles;

    @Schema(description = "菜单")
    private List<String> menus;

    @Schema(description = "权限")
    private List<String> buttons;
}
