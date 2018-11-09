package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.io.Serializable;
import java.util.Date;

/**
 * 预审批申请信息表
 * Created by zcHu on 2017/5/11.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyInfo implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String approvalUuid; //申请人的唯一标识

    private String applyNum; //申请单号(from主系统)

    private String status; //状态

    private String certificationStatus;// 认证状态

    private String usedCarEvaluationStatus;// 二手车评估状态

    private String reason; //退回原因

    private String reason_weBank; //退回原因_微众

    private String reason_icbc; //退回原因_工行

    private String isAutoApproval; // 0: 自动审批， 1:人工审批           （现在只有0  没有1 了）

    private Date submitTime;//在线申请提交时间

    private Date approvalSubmitTime;//预审批提交时间

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

    private String version; // 版本号

    private String origin; //来源【0：app本身、1：微信、2：在线助力融、3.工行产品】

    private String wxState; //微信保单信息完整度【0：未完善、1：已完善】

    private String icbcState; //工行征信状态【100：审批中、1000：通过、1100：拒绝、300:退回待修改】

    private String approvalFileState;//预审批文件提交到主系统状态【0：未提交、1：已提交】

    private Double approvalLon;//预审批提交经度

    private Double approvalLat;//预审批提交纬度
}
