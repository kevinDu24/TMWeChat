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
//public class AlipayBindedBankCards {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;
//
//    private String uuid; //用户id
//
//    private String alipayInfoId; //支付宝id
//
//    private String bankName;//银行名称
//
//    private String cardNo;//卡号后4位
//
//    private Integer cardType;//类型 ，1借记卡 2信用卡
//
//    private String cardOwnerName;//银行卡持卡人姓名
//
//    private Boolean isExpress;//是否开通快捷支付
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
