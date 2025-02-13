package com.sakura.service.system.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.sakura.common.ResultCodeEnum;
import com.sakura.constant.RedisConstant;
import com.sakura.exception.SakuraException;
import com.sakura.model.dto.system.auth.LoginDto;
import com.sakura.model.po.system.SysUser;
import com.sakura.service.system.AuthService;
import com.sakura.service.system.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StringRedisTemplate stringRedisTemplate;
    private final SysUserService userService;

    @Override
    public String createCaptcha(String key) {
        // 1.生成验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(115, 42, 0, 20);
        // 1.1 设置四则运算的验证码生成器 并且指定参与运算的数字位数为1
        lineCaptcha.setGenerator(new MathGenerator(1));
        // 1.2 获取生成验证码的值
        String code = lineCaptcha.getCode();

        // 2.将验证码写入到redis中 有效期为1分钟
        String redisKey = RedisConstant.CAPTCHA_REDIS_PREFIX + key;
        stringRedisTemplate.opsForValue().set(redisKey, code, Duration.ofSeconds(RedisConstant.CAPTCHA_REDIS_PREFIX_TIMEOUT));

        // 3.返回base64图片
        return "data:image/png;base64," + lineCaptcha.getImageBase64();
    }

    @Override
    public String login(LoginDto loginDto) {
        // 1.校验验证码
        String redisKey = RedisConstant.CAPTCHA_REDIS_PREFIX + loginDto.getKey();
        String redisValue = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isEmpty(redisValue)) {
            throw new SakuraException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        // 删除验证码
        stringRedisTemplate.delete(redisKey);
        // 校验用户输入的验证码是否正确
        if (!new MathGenerator().verify(redisValue, loginDto.getCode())) {
            throw new SakuraException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        // 2.查询用户
        SysUser user = userService.lambdaQuery()
                .eq(SysUser::getUsername, loginDto.getUsername())
                .one();
        if (user == null) {
            throw new SakuraException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (user.getStatus() == 0) {
            throw new SakuraException(ResultCodeEnum.ACCOUNT_STOP);
        }
        if(!BCrypt.checkpw(loginDto.getPassword(), user.getPassword())){
            throw new SakuraException(ResultCodeEnum.PASSWORD_ERROR);
        }

        // 3.登录
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }
}




