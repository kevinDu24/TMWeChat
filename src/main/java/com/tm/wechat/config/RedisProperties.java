package com.tm.wechat.config;

/**
 * @program: TMWeChat
 * @description: Redis 存储配置
 * @author: ChengQC
 * @create: 2018-10-12 12:37
 **/
public class RedisProperties {

    /**
     * SysUser 信息 2 min
     */
    public static final String sysUser_userName = "sysUser_userName_";
    public static final int sysUser_userNameTime = 2*60;

    /**
     * UserName 信息 2 min
     */
    public static final String userInfo_userName = "userInfo_userName_";
    public static final int userInfo_userNameTime = 2*60;


    /**
     * userList 组织架构 信息   5 min
     */
    public static final String userList_userName = "userList_userName_";
    public static final int userList_userNameTime = 5*60;

    /**
     * userList 组织架构 信息   5 min
     */
    public static final String userList_userInfo = "userList_userInfo_";
    public static final int userList_userInfoTime = 5*60;


    /**
     * sysUserInfo 信息   2 min
     */
    public static final String sysUserInfo_userName = "sysUserInfo_userName_";
    public static final int sysUserInfo_userNameTime = 2*60;


    /**
     * 上传身份证信息，新建预审批
     */
    public static final String addIdentityInfo_uniqueMark = "addIdentityInfo_uniqueMark";
    public static final int addIdentityInfo_uniqueMarkTime = 1;
}