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
// * Created by xuhao on 2017/7/19.
// */
//@Data
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class WechatInfo {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;
//
//    private String uuid; //用户id
//
//    private String uin; //微信账户唯一标示
//
//    private String nickName; //昵称
//
//    private String headImgPath; //头像存放路径
//
//    private Integer sex; //性别
//
//    private String signature; //签名
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
