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
import java.util.Date;

/**
 * Created by pengchao on 2018/4/16.
 * 请款信息表
 */

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestPayment {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String applyNum; //申请编号

    private String sn; //SN号

    private String gpsState; // 0:未激活，1：已激活

    private String insuranceInfoState; //0:未保存，1：已保存

    private String fileInfoState; // 0:未保存，1：已保存

    private String insuranceInfoSubmitState; //0:未提交，1：已提交

    private String fileInfoSubmitState; // 0:未提交，1：已提交

    private Date submitTime; //提交时间

    private String submitUser; //提交人

    private String name;//申请人姓名




    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

}
