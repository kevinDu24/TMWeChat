package com.tm.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by zcHu on 17/5/9.
 */
@ConfigurationProperties(prefix = "phone")
@Data
public class PhoneSearchProperties {
    private String apikey;
}
