package com.tm.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by LEO on 16/10/10.
 */
@ConfigurationProperties(prefix = "message")
@Data
public class MessageProperties {
    private String userId;
    private String password;
    private String pszMsg;
    private String iMobiCount;
    private String MsgId;
    private String confirmMsg; //在线助力融一审页面
    private String fourElements; //四要素验证页面
    private String addressInput; //收货地址录入页面

}
