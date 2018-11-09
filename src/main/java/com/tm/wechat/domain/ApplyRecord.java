package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


/**
 * 申请状态历史记录
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyRecord {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String approvalUuid; //申请人的唯一标识

    private String applyNum; //申请单号(from主系统)

    // 工行征信状态【100：审批中、1000：通过、1100：拒绝、300:退回待修改】
    // 微信保单信息完整度【0：未完善、1：已完善】
    // 天启状态 【100：审批中、1000：通过、1100：拒绝、300:退回待修改】
    private String status; //状态

    private String reason; //退回原因

    @CreatedDate
    private Date createTime;    //创建时间

    @CreatedBy
    private String createUser;  //创建用户

    private String origin; //来源【0：app本身、1：微信、2：在线助力融、3.工行产品】

}
