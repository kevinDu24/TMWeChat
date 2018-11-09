package com.tm.wechat.service.common;

import com.tm.wechat.config.RedisProperties;
import com.tm.wechat.dao.RedisRepository;
import com.tm.wechat.dao.SysUserInfoRepository;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.domain.SysUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: TMWeChat
 * @description: 基础Service 其他Service 继承使用,本类放一些通用的，需要经常调用的方法
 * @author: ChengQC
 * @create: 2018-10-12 15:20
 **/
@Service
public class BaseService {

    /**
     * redis 缓存工具
     */
    @Autowired
    private RedisRepository redisRepository;

    /**
     * 用户查询
     */
    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 系统用户详情查询
     */
    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;



    /**
     * 根据用户名及厂商名称查询用户
     * @param userName  用户名 SH000
     * @param customer  taimeng 或 yachi ,  默认 taimeng
     * @return
     */
    public SysUser getSysUser(String userName,String customer){
        SysUser sysUser ;
        // 从redis中获取SysUser -- By ChengQiChuan 2018/10/12 12:35
        Object redisObj =  redisRepository.get(RedisProperties.sysUser_userName + userName);
        // 如果在redis中没有获取到数据就去数据库查 -- By ChengQiChuan 2018/10/12 12:35
        if(redisObj != null) {
            sysUser = (SysUser) redisObj;
        }else{
            sysUser = sysUserRepository.findByXtczdmAndCustomer(userName, customer);
            redisRepository.save(RedisProperties.sysUser_userName + userName, sysUser,  RedisProperties.sysUser_userNameTime);
        }
        return sysUser;
    }


    /**
     * 系统用户详情查询
     * @param userName  用户名 SH000
     * @return
     */
    public SysUserInfo getSysUserInfo(String userName){
        SysUserInfo sysUserInfo ;
        // 从redis中获取 SysUserInfo -- By ChengQiChuan 2018/10/12 16:38
        Object redisObj =  redisRepository.get(RedisProperties.sysUserInfo_userName + userName);
        // 如果在redis中没有获取到数据就去数据库查 -- By ChengQiChuan 2018/10/12 16:38
        if(redisObj != null) {
            sysUserInfo = (SysUserInfo) redisObj;
        }else{
            sysUserInfo = sysUserInfoRepository.findByXtczdm(userName);
            redisRepository.save(RedisProperties.sysUserInfo_userName + userName, sysUserInfo,  RedisProperties.sysUserInfo_userNameTime);
        }
        return sysUserInfo;
    }


}