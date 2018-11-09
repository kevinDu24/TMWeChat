package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/4/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCardSignInfoDto {

    private String name;//借记卡户名

    private String bank;//借记卡开户行

    private String bankCardNum;//借记卡卡号

    private String bankcardImg;//银行卡图片

    private String applyNum; //申请编号

    private String phoneNum; //手机号

    private String authState;//认证状态

    private String idCardNum;//身份证号码
    
    private String uniqueMark; //唯一标识
     

}
