package com.tm.wechat.dto.requestpayment;

import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 交强险保单信息
 * @author: ChengQC
 * @create: 2018-09-25 13:08
 **/
@Data
public class SaliIsInsurancePolicyInfoDto {
    /**
     * 保险公司名称
     */
    private String insurancecompanyName;
    /**
     * 交强险保费保单号
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
    private String referAmount;
    /**
     * 保单实际金额
     */
    private String practicalAmount;
    /**
     * 车船税参考融资额
     */
    private String taxreferAmount;
    /**
     * 车船税实际金额
     */
    private String taxstraightToCompensateAmount;
    /**
     * fp名字
     */
    private String fpName;

    /**
     * 申请编号
     */
    private String applyNum;
}