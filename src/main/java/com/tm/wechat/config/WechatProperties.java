package com.tm.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: TMWeChat
 * @description: 微信公众号配置
 * @author: ChengQC
 * @create: 2018-10-15 13:03
 **/
@ConfigurationProperties(prefix = "wechat")
@Data
public class WechatProperties {

    private String sign;         //

    private String timestamp;    //时间戳

    private String appid;        //微信 appId

    private String appsecret;    //微信参数 appsecret

    private String token;        //微信token值
}