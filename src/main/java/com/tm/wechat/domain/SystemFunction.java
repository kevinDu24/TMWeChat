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
 * Created by pengchao on 2018/4/23.
 * 经销商用户功能权限
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemFunction {

    @Id
    @GeneratedValue
    private Long id;

//    private String userId;// 用户id
//
//    private String xtczdm; //用户系统操作代码
//
//    private String onLineApply;// 在线申请
//
//    private String calculators;//计算器
//
//    private String contracts; //合同查询
//
//    private String sales;//销售查询
//
//    private String gpsConvention; //GPS邀约
//
//    private String orderBind; //订单绑定
//
//    private String onApplySign;//在线签约
//
//    private String requestPayment; //请款
//
//    private String information; //资讯
//
//    private String letterOfCredit; //征信拍照

    private String description;//功能描述

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;


}
