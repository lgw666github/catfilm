package com.catfilm.springboot.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiListing;

@Data
@Configuration
@ConfigurationProperties(prefix = OrderFileProperties.SEATFILE_PREFIX)
public class OrderFileProperties {

    public static final String SEATFILE_PREFIX="order-file";

    //file-pre 自动转成第二个单词大写
    private String filePre;

    //支付宝回掉地址
    private String alipayCallBack;
}

