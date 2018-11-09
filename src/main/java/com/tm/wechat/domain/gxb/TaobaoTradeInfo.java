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
//public class TaobaoTradeInfo {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;
//
//    private String uuid; //唯一标识
//
//    private String taobaoId; //淘宝id
//
//    private String orderNumber;//订单号
//
//    private String tradeStatusName;//交易状态
//
//    private String orderCreateTime; //订单创建时间
//
//    private String endTime;//订单完成时间
//
//    private String payTime;//支付时间
//
//    private Double actualFee;//实付金额
//
//    private String itemTitle;//商品名称
//
//    private Integer quantity;//购买数量
//
//    private Double original;//原价
//
//    private Double actual;//实际金额
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
