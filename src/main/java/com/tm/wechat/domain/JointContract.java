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
 * 共申人电子合同信息（目前没用）
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JointContract {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String phoneNum; //手机号

    private String idCard; //身份证号

    private String name; //姓名

    private String state; //订单状态:0未提交、1已提交

    private String applyNum; //申请编号

    private String contractNum; //合同号

    private String contactPdf; // 合同url（未签署，主系统）

    private String contactSignedPdf; // 合同url（已签署）

    private String repaymentPlanPdf; // 还款计划表（未签署，主系统）

    private String repaymentPlanSignedPdf; // 还款计划表（已签署）

    private String accountId; // e签宝账户标识

    private String sealData; // 个人电子印章图片base64数据

    private String signServiceId; // 合同签署记录id

    private String signRepaymentPlanServiceId; // 还款计划表id

    private String faceImageUrl; // 活体检测截取照片

    private String idCardUrl; // 身份证照片

    private String applyContractId; //申请人合同id

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;


    public JointContract() {
    }

    public JointContract(ApplicantContractDto applicantContractDto) {
        this.phoneNum = applicantContractDto.getPhoneNum();
        this.idCard = applicantContractDto.getIdCard();
        this.name = applicantContractDto.getName();
        this.contactPdf = applicantContractDto.getContactPdf();
        this.repaymentPlanPdf = applicantContractDto.getRepaymentPlanPdf();
    }
}
