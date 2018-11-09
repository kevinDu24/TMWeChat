package com.tm.wechat.dto.sms;

import lombok.Data;

/**
 * Created by pengchao on 2018/7/23.
 */
@Data
public class SendShortMessageDto {

    private String phoneNum; //手机号

    private String text; //短信内容

}
