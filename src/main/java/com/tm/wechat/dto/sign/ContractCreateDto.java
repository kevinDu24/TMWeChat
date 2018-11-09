package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zcHu on 2017/5/18.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractCreateDto implements Serializable{

    @JsonProperty("basqbh")
    private String applyNum; //二次营销申请编号

    @JsonProperty("baddbh")
    private String contractNum; //合同编号

    private String jointContactPdf; //共申人合同
    
    private String jointRepaymentPlanPdfUrl; //共申人确认函
    
    private String guaranteeContactPdf; //担保人合同

    private String guaranteeRepaymentPlanPdfUrl; //担保人确认函


    private String leaseContract;//租赁主合同

    private String deliveryReceitp;//交接单确认函

    private String mortgageContractPdfUrl;//抵押合同

    private String repaymentPlanPdfUrl;//还款计划表

    private String riskNotificationPdfUrl; //风险告知书



}
