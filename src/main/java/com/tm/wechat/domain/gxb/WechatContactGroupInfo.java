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
// * Created by Leadu on 2017/7/19.
// */
//@Data
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class WechatContactGroupInfo {
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
//    private String groupNickName; //昵称
//
//    private String groupHeadImgPath; //头像存放路径
//
//    private Integer groupSex; //性别
//
//    private Integer groupStarFriend; //星级好友
//
//    private Integer isGroup; //是否群组
//
//    private Integer isOwner; //是否群主
//
//    private Integer memberCount; //群组人数
//
//    private Integer isCollection; //是否收藏了群
//
//    private Integer contactIntimacy; //亲密度
//
//    @CreatedDate
//    private Date createTime; //创建时间
//}
