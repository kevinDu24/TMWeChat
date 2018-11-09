package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by zcHu on 2017/5/18.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUrlDto implements Serializable{

    private String contactPdfUrl;//租赁主合同

    private String deliveryReceitp;//交接单确认函

    private String mortgageContractPdfUrl;//抵押合同

    private String repaymentPlanPdfUrl;//还款计划表

    private String riskNotificationPdfUrl; //风险告知书


}
