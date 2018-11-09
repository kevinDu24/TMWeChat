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
 * Created by pengchao on 2018/4/10.
 *  签约时补充车辆信息
 */

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarSignInfo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String applyNum; //申请编号

    private String vehicleIdentifyNum; //车架号

    private String dateOfManufacture; //出厂日期

    private String contractNum; //合同号

    private String colour; //颜色

    private String engineNo; // 发动机号

    private String vehicleLicensePlate; // 车牌号

    private String vehicleConfigDescription; // 车辆配置描述

    private String signAuthBookImg; //签署授权书照片

    private String authBookImg; //授权书照片

    private String holdAuthBookImg; //手持征信授权

    private String submitState; //提交状态  0:未提交  1：已提交 默认未提交



    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

}
