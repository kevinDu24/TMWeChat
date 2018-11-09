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
import java.io.Serializable;
import java.util.Date;

/**
 * Created by pengchao on 2018/4/13.
 * 太盟宝新增账号用户信息表
 */

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserInfo implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String name;// 用户姓名

    private String phoneNum;// 用户名称

    private String idCardNum;// 用户密码

    private String bankNum;//银行卡号

    private String weChat; //微信号

    private String phoneState;//手机认证状态

    private String idCardState; //身份验证状态

    private String userInfoState;//用户信息

    private String activationState; //激活状态

    private String xtczdm; //用户系统操作代码
    
    private String loginAuth; //默认有登录权限，为0时无


    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;
     

     
}
