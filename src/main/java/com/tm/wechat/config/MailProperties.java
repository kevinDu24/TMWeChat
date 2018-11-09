package com.tm.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2016/09/19.
 */
@ConfigurationProperties(prefix = "mail")
@Data
public class MailProperties {
    private String host; //主机地址
    private String protocol; //邮件协议
    private String auth; //认证
    private String port; //端口
    private String address; //发件箱
    private String pwd; //密码
    private String cc; //抄送方邮箱
}
