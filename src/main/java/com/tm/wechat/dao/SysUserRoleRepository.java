package com.tm.wechat.dao;

import com.tm.wechat.domain.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LEO on 16/8/26.
 */
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {

    @Query(nativeQuery = true, value = "with  a as (SELECT * from zam017 t  INNER JOIN zam003 y " +
            "ON T.xtczdm = y.xtczdm where t.sjbmdm in " +
            "(SELECT t1.xtbcid FROM public.zam017 t1 where t1.xtczdm=?1) and (t.xtbmdm = 'QYJL' or t.xtbmdm = 'XSJL') AND y.xtqybz  = '是'), " +
            "b as (select * from zam017 INNER JOIN zam003 ON zam017.xtczdm = zam003.xtczdm where sjbmdm in (select a.xtbcid from a)  and (xtbmdm = 'QYJL' or xtbmdm = 'XSJL') AND zam003.xtqybz  = '是') " +
            "select a.* from a UNION ALL select b.* from b")
    List<SysUserRole> getGroupList(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM zam017 T WHERE T .sjbmdm IN \n" +
            "(SELECT t1.xtbcid FROM PUBLIC .zam017 t1 WHERE t1.xtczdm = ?1)\n" +
            "UNION ALL\n" +
            "SELECT * FROM zam017 t2 WHERE t2.sjbmdm IN \n" +
            "(SELECT xtbcid FROM zam017 T WHERE T .sjbmdm IN \n" +
            "(SELECT t1.xtbcid FROM PUBLIC .zam017 t1 WHERE t1.xtczdm = ?1))\n" +
            "UNION ALL\n" +
            "SELECT * FROM zam017 t3 WHERE t3.sjbmdm IN \n" +
            "(SELECT xtbcid FROM zam017 t2 WHERE t2.sjbmdm IN \n" +
            "(SELECT T .xtbcid FROM zam017 T WHERE T .sjbmdm IN \n" +
            "(SELECT t1.xtbcid FROM PUBLIC .zam017 t1 WHERE t1.xtczdm = ?1)))")
    List<SysUserRole> getApplyGroupList(String name);

    SysUserRole findByXtczdm(String xtczdm);
    SysUserRole findByXtbmdmAndXtjgid(String xtbmdm,Long xtjgid);
    @Query(nativeQuery = true, value = "SELECT * FROM zam017 t1 WHERE t1.xtczdm =?1")
    SysUserRole getUserType(String xtczdm);
    @Query(nativeQuery = true, value = "select * from zam017 where xtbmdm = 'ZJL' and xtjgid = ?1")
    SysUserRole getZJL(Long xtjgid);

    SysUserRole findByXtyqhm(String code);
}
