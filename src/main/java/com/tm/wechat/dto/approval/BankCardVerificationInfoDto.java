package com.tm.wechat.dto.approval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/6/30.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankCardVerificationInfoDto {
    private String contractNum; //合同号
    private String name; //姓名
    private String idCardNum; //身份证号
    private String bankCardNum; //银行卡号
    private String verificationCount; //核实次数
    private String recAddr; //收货地址
    private String phoneNum;//手机号
    private String address; //收货地址

}
