package com.tm.wechat.dao;

import com.tm.wechat.domain.SysUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/4/14.
 */
public interface SysUserInfoRepository extends JpaRepository<SysUserInfo, Long> {
    SysUserInfo findByXtczdm(String userName);
    SysUserInfo findByPhoneNum(String userName);
    SysUserInfo findByIdCardNum(String idCardNum);
}
