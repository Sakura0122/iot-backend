package com.sakura.constant;

/**
 * @author: sakura
 * @date: 2024/10/3 16:52
 * @description:
 */
public interface RedisConstant {
    // 登录返回的token
    String USER_LOGIN_TOKEN = "login:token:";
    int USER_LOGIN_TOKEN_TIMEOUT = 60 * 30;

    // 验证码
    String CAPTCHA_REDIS_PREFIX = "captcha:";
    int CAPTCHA_REDIS_PREFIX_TIMEOUT = 60;

    // 角色权限
    String BUTTON_CACHE_PREFIX = "user:permission:";
    String MENU_CACHE_PREFIX = "user:menu:";
    String ROLE_CACHE_PREFIX = "user:role:";
}
