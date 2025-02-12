package com.sakura.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "spzx.auth")
public class UserAuthProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
