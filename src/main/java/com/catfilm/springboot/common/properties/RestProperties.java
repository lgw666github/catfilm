package com.catfilm.springboot.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = RestProperties.REST_PREFIX)
public class RestProperties {

    public static final String REST_PREFIX="rest";

    //auth-open 自动转成第二个单词大写
    private boolean authOpen;
}
