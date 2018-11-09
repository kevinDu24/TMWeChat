package com.tm.wechat.dto.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by PengChao on 16/9/12.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsMessage {
    private String terminalId;
    private String msgContent;
    private Integer type;   //0:发送消息;1:反馈消息
    private String operator;

    public SmsMessage(){}

    public SmsMessage(String terminalId, String msgContent, Integer type, String operator){
        this.terminalId = terminalId;
        this.msgContent = msgContent;
        this.type = type;
        this.operator = operator;
    }
}
