package com.tm.wechat.dto.push;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/6/21.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushMessageDto {

    private String messageType; //推送消息类型：1:单播，2：广播

    private String content; //内容

    private String title; //标题

    private String userName; //要推送到的用户名

}
