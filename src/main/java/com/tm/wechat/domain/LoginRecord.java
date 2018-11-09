package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by LEO on 16/10/2.
 * 用户登录记录表
 */
@Entity
@Data
public class LoginRecord {
    @Id
    @GeneratedValue
    private Long id; // 主键
    private String address; // 登陆地址
    private Date time; // 登陆成功时间
    private String userName; // 用户名
    private String deviceId; // 设备号
    private String deviceType; // 设备类型 android:3、ios:4
    private Date logoutTime; // 退出登陆时间
    private String effectiveFlg; // 有效标识 0:无效、1:有效   是否正在登录中
    private String customer; // 客户标识 : taimeng or yachi
    private String deviceToken; //友盟设备token

    public LoginRecord(){}

    public LoginRecord(String address, String userName,String deviceType, String deviceToken){
        this.address = address;
        this.time = new Date();
        this.userName = userName;
        this.customer = "taimeng";
        this.deviceType = deviceType;
        this.deviceToken =deviceToken;
    }

    public LoginRecord(String address, String userName, String customer, String deviceId, String deviceType){
        this.address = address;
        this.time = new Date();
        this.userName = userName;
        this.customer = customer;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.effectiveFlg = "1";
    }
}
