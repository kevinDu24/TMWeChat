package com.tm.wechat.dto.sign;

import lombok.Data;

/**
 * Created by pengchao on 2018/6/12.
 */
@Data
public class FourFactorVerificationInfoDto {

    private String name;//借记卡户名

    private String bank;//借记卡开户行

    private String bankCardNum;//借记卡卡号

    private String bankcardImg;//银行卡图片

    private String applyNum; //申请编号

    private String phoneNum; //手机号

    private String idCardNum;//身份证号码
}
