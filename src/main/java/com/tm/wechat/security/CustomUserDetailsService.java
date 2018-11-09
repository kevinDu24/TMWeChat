package com.tm.wechat.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.RedisProperties;
import com.tm.wechat.dao.RedisRepository;
import com.tm.wechat.dao.SysUserInfoRepository;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.dao.SysUserRoleRepository;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.domain.SysUserInfo;
import com.tm.wechat.domain.SysUserRole;
import com.tm.wechat.dto.result.UserInfoResult;
import com.tm.wechat.service.TMInterface;
import com.tm.wechat.consts.ContractSignStatus;
import com.tm.wechat.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by LEO on 2015/9/12.
 */
public class CustomUserDetailsService extends BaseService implements UserDetailsService {
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TMInterface tmInterface;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;
    @Autowired
    private RedisRepository redisRepository;

    /**
     *
     * spring security 实现方式大致可以分为这几种：
     *     1. 配置文件实现，只需要在配置文件中指定拦截的 url 所需要权限、配置 userDetailsService 指定用户名、密码、对应权限，就可以实现。
     *     2. 实现 UserDetailsService，loadUserByUsername(String userName) 方法，根据 userName 来实现自己的业务逻辑返回 UserDetails 的实现类，需要自定义 User 类实现 UserDetails，比较重要的方法是 getAuthorities()，用来返回该用户所拥有的权限。
     *     3. 通过自定义 filter 重写 spring security 拦截器，实现动态过滤用户权限。
     *     4. 通过自定义 filter 重写 spring security 拦截器，实现自定义参数来检验用户，并且过滤权限。
     *      -- By ChengQiChuan 2018/10/11 10:33
     * @param userName
     * @return
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName){
        if(userName == null || userName.isEmpty()){
            throw new BadCredentialsException("用户名错误");
        }
        String headerParam = httpServletRequest.getHeader("Header-Param");
//        String customer;
//        if(headerParam == null || "".equals(headerParam)){
//            customer = "taimeng";
//        }else {
//            customer = JsonPath.from(headerParam).get("systemflag");
//        }

        //查询系统用户信息
        SysUserInfo sysUserInfo = getSysUserInfo(userName);

        if(sysUserInfo != null && ContractSignStatus.NEW.code().equals(sysUserInfo.getLoginAuth())){
            throw new BadCredentialsException("无登录权限");
        }
        if(sysUserInfo != null && ContractSignStatus.NEW.code().equals(sysUserInfo.getActivationState())){
            throw new BadCredentialsException("未完成认证");
        }
        UserInfoResult codeResult = new UserInfoResult();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName, customer);
        SysUser sysUser = null;
        try {
            // 从redis中获取用户信息 -- By ChengQiChuan 2018/10/11 14:21
            Object redisObj =  redisRepository.get(RedisProperties.userInfo_userName + userName);
            String result = "";
            // 如果在redis中没有获取到数据就去主系统查 -- By ChengQiChuan 2018/10/11 14:26
            if(redisObj != null) {
                result = (String) redisObj;
            }else{
                result = tmInterface.getUserInfo("ClientInfoRe", userName);
                //保存用户信息2分钟，2分钟超时之后需要重新查
                redisRepository.save(RedisProperties.userInfo_userName + userName, result,  RedisProperties.userInfo_userNameTime);
            }
            codeResult = objectMapper.readValue(result, UserInfoResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadCredentialsException("登录失败,系统异常");
        }
        if ("true".equals(codeResult.getIsSuccess())) {
            sysUser = codeResult.getZam003Re();

            SysUserRole sysUserRole = codeResult.getZam017Re();

            if(sysUser != null){
                sysUser.setCustomer("taimeng");
                if(sysUser.getXTCZID() != null ){
                    sysUserRepository.save(sysUser);
                }
            }
            if(sysUserRole != null){
                if(sysUserRole.getXTBCID() != null){
                    sysUserRoleRepository.save(sysUserRole);
                }
            }
        } else {
            throw new BadCredentialsException(codeResult.getResultMsg());
        }
        if(sysUser == null || sysUser.getXTQYBZ().equals("否") || "否".equals(sysUser.getSFDLTMB())){
            throw new BadCredentialsException("用户名错误");
        }
        return new User(sysUser.getXtczdm(), sysUser.getXTCZMM().toLowerCase(), Sets.newHashSet());
    }
}
