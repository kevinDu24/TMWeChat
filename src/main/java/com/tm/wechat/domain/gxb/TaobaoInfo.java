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
//public class TaobaoInfo {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;//主键
//
//    private String uuid; //唯一标识
//
//    private String name; //姓名
//
//    private String mobile; //手机号码
//
//    private String identityCard; //身份证号
//
//    private String taobaoAccount; //淘宝账号
//
//    private String alipayAccount; //支付宝账号
//
//    private int creditLevelAsBuyer; //作为买家信用额度
//
//    private Boolean isVerified; //是否实名认证
//
//    private Integer wholeCount; //交易总笔数
//
//    private Double wholeFee; //交易总金额
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
