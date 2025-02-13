package com.sakura.controller.system;

import com.sakura.common.Result;
import com.sakura.model.dto.system.auth.LoginDto;
import com.sakura.service.system.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: sakura
 * @date: 2025/2/13 10:08
 * @description:
 */
@RestController
@Tag(name = "登录管理")
@RequestMapping("/system/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @GetMapping("/captcha")
    @Operation(summary = "获取验证码")
    public Result<String> captcha(@RequestParam String key) {
        return Result.success(authService.createCaptcha(key));
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<String> login(@Validated @RequestBody LoginDto loginDto) {
        return Result.success(authService.login(loginDto));
    }
}
