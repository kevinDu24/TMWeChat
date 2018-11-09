package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/4/12.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignFileUrlDto {


    private String applyNum; //申请编号

    private String leaseContract;//租赁主合同

    private String deliveryReceitp;//交接单确认函

    private String mortgageContract;//抵押合同

    private String repaymentPlan;//还款计划表

    private String riskNotification; //风险告知书
}
