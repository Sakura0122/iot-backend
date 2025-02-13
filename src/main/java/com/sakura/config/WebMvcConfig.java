package com.sakura.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: sakura
 * @date: 2024/10/3 11:58
 * @description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 添加路径规则
                .allowCredentials(true)            // 是否允许在跨域的情况下传递Cookie
                .allowedOriginPatterns("*")        // 允许请求来源的域规则
                .allowedMethods("*")               // 允许所有的请求方法
                .allowedHeaders("*");              // 允许所有的请求头
    }
}
