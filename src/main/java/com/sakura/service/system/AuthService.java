package com.sakura.service.system;

import com.sakura.model.dto.system.auth.LoginDto;

public interface AuthService {

    /**
     * 生成验证码
     * @param key redis中存储的key
     * @return base64验证码图片
     */
    String createCaptcha(String key);

    /**
     * 登录
     * @param loginDto 登录信息
     * @return token
     */
    String login(LoginDto loginDto);
}
