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
//public class WechatContactInfo {
//
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid",strategy = "uuid")
//    private String id;
//
//    private String uuid; //用户id
//
//    private String wechatId; //微信信息id
//
//    private String contactNickName; //昵称
//
//    private String contactHeadImgPath; //头像存放路径
//
//    private String contactRemarkName; //备注名
//
//    private Integer contactSex; //性别
//
//    private String signature; //签名
//
//    private Integer contactVerifyFlag; //账号类型
//
//    private Integer contactStarFriend; //星级好友
//
//    private String contactProvince; //省
//
//    private String contactCity; //市
//
//    private Integer contactIntimacy; //亲密度
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
