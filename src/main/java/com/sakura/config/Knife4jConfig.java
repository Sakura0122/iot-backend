package com.sakura.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: sakura
 * @date: 2024/10/2 12:34
 * @description:
 */
@Configuration
public class Knife4jConfig {

    /***
     * @description 自定义接口信息
     */
    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("iot系统API接口文档")
                        .version("1.0")
                        .description("iot系统API接口文档")
                        .contact(new Contact().name("sakura"))); // 设定作者
    }

    @Bean
    public GroupedOpenApi adminApi() {      // 创建了一个api接口的分组
        return GroupedOpenApi.builder()
                .group("系统管理")         // 分组名称
                .pathsToMatch("/system/**")  // 接口请求路径规则
                .build();
    }
}
