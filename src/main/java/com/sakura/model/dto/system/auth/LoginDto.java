package com.sakura.model.dto.system.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author: sakura
 * @date: 2025/2/13 10:27
 * @description:
 */
@Data
@Schema(description = "登录参数")
public class LoginDto {
    @Schema(description = "验证码key")
    @NotBlank(message = "验证码key不能为空")
    private String key;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 16, message = "用户名长度必须在4到16个字符之间")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度必须在6到16个字符之间")
    private String password;
}
