package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.contractsign.ApplicantContractDto;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2018/3/14.
 * 申请人电子合同签约信息
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyContract {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String phoneNum; //手机号

    private String idCard; //身份证号

    private String name; //姓名

    private String state; //订单状态:0未签署、1已签署

    private String applyNum; //申请编号

    private String contractNum; //合同号

    private String contactPdf; // 合同url（未签署，主系统）

    private String contactSignedPdf; // 合同url（已签署）

    private String confirmationPdf; // 确认函url（未签署，主系统）

    private String confirmationSignedPdf; // 确认函url（已签署）

    private String mortgageContractPdf;//抵押合同

    private String repaymentPlanPdf;//还款计划表

    private String riskNotificationPdf; //风险告知书


    private String mortgageContractSignedPdf;//抵押合同 （已签署）

    private String repaymentPlanSignedPdf;//还款计划表（已签署）

    private String riskNotificationSignedPdf; //风险告知书 （已签署）

    private String accountId; // e签宝账户标识

    private String sealData; // 个人电子印章图片base64数据

    private String signServiceId; // 合同签署记录id

    private String signConfirmationServiceId; // q确认函签署记录id

    private String signMortgageContractServiceId;//抵押合同 （已签署）记录id

    private String signRepaymentPlanServiceId;//还款计划表（已签署）记录id

    private String signRiskNotificationServiceId; //风险告知书 （已签署）记录id

    private String faceImageUrl; // 活体检测截取照片

    private String idCardUrl; // 身份证照片

    private String isICBC; //是否是工行产品

    private String submitState; //订单提交状态:0未提交、1已提交

    private Date submitTime;

    private String submitUser; //提交人

    private String signUser; //签约时的用户名

    private String createState; //合同生成状态0未生成、1已生成

    private String getState; //合同获取状态0未获取、1已获取

    private String signStatus;//微众电子签约状态

    private String signDate;//微众签约数据



    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

    public ApplyContract() {
    }

    public ApplyContract(ApplicantContractDto applicantContractDto) {
        this.phoneNum = applicantContractDto.getPhoneNum();
        this.idCard = applicantContractDto.getIdCard();
        this.name = applicantContractDto.getName();
        this.contactPdf = applicantContractDto.getContactPdf();
        this.repaymentPlanPdf = applicantContractDto.getRepaymentPlanPdf();
        this.confirmationPdf = applicantContractDto.getConfirmationPdf();
        this.mortgageContractPdf = applicantContractDto.getMortgageContractPdf();
        this.riskNotificationPdf = applicantContractDto.getRiskNotificationPdf();
    }
}
