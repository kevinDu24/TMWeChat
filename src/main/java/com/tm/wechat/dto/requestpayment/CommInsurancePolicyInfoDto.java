package com.tm.wechat.dto.requestpayment;

import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 商业险保单信息
 * @author: ChengQC
 * @create: 2018-09-25 11:55
 **/
@Data
public class CommInsurancePolicyInfoDto {

    /**
     * 保险公司名称
     */
    private String insurancecompanyName;

    /**
     * 商业险保费保单号
     */
    private String policyNumber;

    /**
     * 投保开始时间
     */
    private String startTime;
    /**
     * 投保结束时间
     */
    private String endTime;
    /**
     * 参考融资额
     */
    private String referAmount="";
    /**
     * 保单实际金额
     */
    private String practicalAmount;
    /**
     * 是否直赔
     */
    private String isStraightToCompensate;
    /**
     * 直赔金额
     */
    private String straightToCompensateAmount;
    /**
     * fp
     */
    private String fpName;

    private String applyNum;
}