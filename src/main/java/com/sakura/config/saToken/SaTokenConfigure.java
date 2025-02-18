package com.sakura.config.saToken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SaTokenConfigure implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATH_PATTERNS = new String[]{"/system/auth/login", "/system/auth/captcha", "/doc.html", "/v3/api-docs/**", "/webjars/**"};

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> SaRouter
                .match("/**")
                .notMatch(SaHttpMethod.OPTIONS)
                .notMatch(EXCLUDE_PATH_PATTERNS)
                .check(r -> StpUtil.checkLogin())))
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)            // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")        // 允许请求来源的域规则
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许所有的请求方法
                .allowedHeaders("*")             // 允许所有的请求头
                .maxAge(3600);
    }
}
