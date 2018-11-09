package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 预审批申请状态表
 * 记录预审批的各个审批状态信息
 *
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyInfoStatus implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String uniqueMark; // 客户唯一标识

    // 类型key
    // #hpl             hpl 天网
    // #wz_bank         微众预审批
    // #icbc            工行审批
    // #xw_bank 新网风控 ,  #xw_bank#photo_msg  新网大头照
    private String itemKey; //key

    //状态码,这个状态比较杂，需要根据记录的不同的东西来
    //1.如果是 自营 hpl ， 工行 icbc 就查 ApprovalType
    //2.如果是 微众 wz_bank  就查 ApplyType
    //3.如果是 新网 xw_bank 就查 XW_FinancePreApplyResultDto
    private String status;

    private String itemReason;  //备注

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    private Date submitTime; //提交时间

    private Date resultTime; //结果时间

    private String interval; //提交到得到结果间隔时间

    @LastModifiedDate
    private Date updateTime;

    private String isDelete = "0"; // 是否删除   0 未删除  1 已删除
}
