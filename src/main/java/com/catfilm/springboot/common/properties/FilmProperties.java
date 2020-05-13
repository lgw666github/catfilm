package com.catfilm.springboot.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = FilmProperties.Film_PREFIX)
public class FilmProperties {

    public static final String Film_PREFIX="film";

    //auth-open 自动转成第二个单词大写
    private String imgPre;
}
