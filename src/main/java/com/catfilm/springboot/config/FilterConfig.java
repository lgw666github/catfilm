package com.catfilm.springboot.config;

import com.catfilm.springboot.common.properties.RestProperties;
import com.catfilm.springboot.controller.filter.AuthFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    @Bean
    @ConditionalOnProperty(prefix = RestProperties.REST_PREFIX, name = "auth-open", havingValue = "true")
    //matchIfMissing=true 即使没有filter.auth-open也可以，默认为false；havingValue跟auth-open的值相等才能加载

    public AuthFilter jwtAuthenticationTokenFilter() {

        return new AuthFilter();
    }

}
