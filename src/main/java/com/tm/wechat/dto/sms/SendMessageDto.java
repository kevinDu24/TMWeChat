package com.tm.wechat.dto.sms;

import lombok.Data;

/**
 * Created by pengchao on 2018/6/13.
 */
@Data
public class SendMessageDto {

    private String phoneNum; //手机号

    private String applyNum; //申请编号

}
