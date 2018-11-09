//package com.tm.wechat.domain.gxb;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.Data;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import java.util.Date;
//
///**
// * Created by xuhao on 2017/7/20.
// */
//@Data
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class AlipayTradeInfo {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;//主键
//
//    private String uuid; //唯一标识
//
//    private String alipayInfoId; //支付宝id
//
//    private String tradeNo; //交易号
//
//    private String title; //购物标题
//
//    private Double amount; //金额
//
//    private String tradeTime; //交易时间
//
//    private String tradeStatusName; //交易状态
//
//    private String txTypeName; //交易类型
//
//    private String otherSide; //交易对方
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
