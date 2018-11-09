package com.tm.wechat.dto.contractsign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.ApplyContract;
import com.tm.wechat.domain.GuaranteeContract;
import com.tm.wechat.domain.JointContract;
import lombok.Data;

/**
 * Created by pengchao on 2018/3/26.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicantContractSignDto {

    private String phoneNum; //手机号

    private String idCard; //身份证号

    private String name; //姓名

    private String contactSignedPdf; //合同url签署后

    private String confirmationSignedPdf; //确认函url签署后

    private String repaymentPlanSignedPdf; //还款计划表url

    private String mortgageContractSignedPdf;//抵押合同

    private String riskNotificationSignedPdf; //风险告知书

    private String applyNum; // 申请编号

    private String contractNum; //合同编号

    private String signServiceId; //合同签约id

    private String signConfirmationServiceId; // 确认函签约ID

    private String signMortgageContractServiceId;//抵押合同 （已签署）记录id

    private String signRepaymentPlanServiceId;//还款计划表（已签署）记录id

    private String signRiskNotificationServiceId; //风险告知书 （已签署）记录id

    public ApplicantContractSignDto() {
    }

    public ApplicantContractSignDto(JointContract jointContract) {
        this.phoneNum = jointContract.getPhoneNum();
        this.idCard = jointContract.getIdCard();
        this.name = jointContract.getName();
        this.contactSignedPdf = jointContract.getContactSignedPdf();
        this.repaymentPlanSignedPdf = jointContract.getRepaymentPlanSignedPdf();
        this.applyNum = jointContract.getApplyNum();
        this.contractNum = jointContract.getContractNum();
        this.signServiceId = jointContract.getSignServiceId();
        this.signRepaymentPlanServiceId = jointContract.getSignRepaymentPlanServiceId();
    }

    public ApplicantContractSignDto(ApplyContract applyContract) {
        this.phoneNum = applyContract.getPhoneNum();
        this.idCard = applyContract.getIdCard();
        this.name = applyContract.getName();
        this.contactSignedPdf = applyContract.getContactSignedPdf();
        this.confirmationSignedPdf = applyContract.getConfirmationSignedPdf();
        this.mortgageContractSignedPdf = applyContract.getMortgageContractSignedPdf();
        this.repaymentPlanSignedPdf = applyContract.getRepaymentPlanSignedPdf();
        this.riskNotificationSignedPdf = applyContract.getRiskNotificationSignedPdf();
        this.applyNum = applyContract.getApplyNum();
        this.contractNum = applyContract.getContractNum();
        this.signServiceId = applyContract.getSignServiceId();
        this.signConfirmationServiceId = applyContract.getSignConfirmationServiceId();
        this.signMortgageContractServiceId = applyContract.getSignMortgageContractServiceId();
        this.signRepaymentPlanServiceId = applyContract.getSignRepaymentPlanServiceId();
        this.signRiskNotificationServiceId = applyContract.getSignRiskNotificationServiceId();
    }

    public ApplicantContractSignDto(GuaranteeContract guaranteeContract) {
        this.phoneNum = guaranteeContract.getPhoneNum();
        this.idCard = guaranteeContract.getIdCard();
        this.name = guaranteeContract.getName();
        this.contactSignedPdf = guaranteeContract.getContactSignedPdf();
        this.repaymentPlanSignedPdf = guaranteeContract.getRepaymentPlanSignedPdf();
        this.applyNum = guaranteeContract.getApplyNum();
        this.contractNum = guaranteeContract.getContractNum();
        this.signServiceId = guaranteeContract.getSignServiceId();
        this.signRepaymentPlanServiceId = guaranteeContract.getSignRepaymentPlanServiceId();
    }
}
