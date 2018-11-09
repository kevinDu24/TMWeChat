package com.tm.wechat.dao;

import com.tm.wechat.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LEO on 16/8/26.
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByXtczdm(String account);
    SysUser findByXtczdmAndCustomer(String account, String customer);
    @Query(nativeQuery = true, value = "SELECT * FROM zam003 t1 WHERE t1.xtczdm =?1 and (t1.xtbmid is null or t1.xtbmid = '0') and t1.xtjsid = '22' and t1.xtqybz = '是' and customer =?2")
    SysUser beSaller(String userName, String customer);

    @Query(nativeQuery = true, value = "SELECT * FROM zam003 t1 WHERE (t1.xtbmid is null or t1.xtbmid = '0') and t1.xtjsid = '22' and t1.xtqybz = '是' and customer =?1")
    List<SysUser> getSaller(String customer);
}
