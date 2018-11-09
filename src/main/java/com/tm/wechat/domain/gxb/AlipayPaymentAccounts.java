//package com.tm.wechat.domain.gxb;
//
///**
// * Created by Leadu on 2017/7/20.
// */
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
//public class AlipayPaymentAccounts {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;
//
//    private String uuid; //用户唯一标识
//
//    private String alipayInfoId; //支付宝id
//
//    private String category;//缴费项目，（电费）
//
//    private String city;//地区
//
//    private String organization;//收款单位
//
//    private String accountName;//户名
//
//    private String accountCode;//缴费号码
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
