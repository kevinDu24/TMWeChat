package com.tm.wechat.dto.contractsign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/3/13.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantContractDto {

    private String phoneNum; //手机号

    private String idCard; //身份证号

    private String name; //姓名

    private String contactPdf; //合同url

    private String repaymentPlanPdf; //确认函url

    private String confirmationPdf;//交接单确认函

    private String mortgageContractPdf;//抵押合同

    private String riskNotificationPdf; //风险告知书

    public ApplicantContractDto() {
    }

    public ApplicantContractDto(String name, String idCard) {
        this.idCard = idCard;
        this.name = name;
    }
}
