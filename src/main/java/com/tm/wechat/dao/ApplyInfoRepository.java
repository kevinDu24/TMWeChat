package com.tm.wechat.dao;

import com.tm.wechat.domain.ApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zcHu on 17/5/11.
 */
public interface ApplyInfoRepository extends JpaRepository<ApplyInfo, String>{
    ApplyInfo findByApprovalUuidAndVersion(String uniqueMark, String version);
    ApplyInfo findByApplyNum(String applyNum);
    @Query(nativeQuery = true, value = "SELECT * FROM apply_info WHERE create_user in ?1 AND version = ?2 and length(apply_num)<10;")
    List<ApplyInfo> findCount(List<String> userList, String version);

//    @Query(nativeQuery = true, value = "SELECT * FROM apply_info WHERE create_user in ?1 AND version = ?2")
//    List<ApplyInfo> findApprovalCount(List<String> userList, String version);

// 修改数据库结构，更改查询方式 -- By ChengQiChuan 2018/10/15 18:26
//    /**
//     * 查询列表数量
//     * @param userList
//     * @param version
//     * @return
//     */
//    @Query(nativeQuery = true, value = "select t1.toSubmitListCount,t2.backListCount,t3.approvalListCount,t4.passListCount from (\n" +
//            "SELECT count(*) AS toSubmitListCount FROM apply_info a \n" +
//            "WHERE create_user in ?1 AND version = ?2 \n" +
//            "and ( a.status = '0' or (a.status = '1000' and a.wx_state = '0'))\n" +
//            ") as t1,(\n" +
//            "SELECT count(*) AS backListCount FROM apply_info a \n" +
//            "WHERE create_user in ?1 AND version = ?2 \n" +
//            "and ( a.status = '300' or (a.status = '1000' and a.origin = '3' and a.icbc_state = '300'))\n" +
//            ") as t2,\n" +
//            "(SELECT count(*) AS approvalListCount FROM apply_info a \n" +
//            "WHERE create_user in ?1 AND version = ?2 \n" +
//            "and ( a.status = '100' or (a.status = '1000' and a.origin = '3' and a.icbc_state = '100'))\n" +
//            ") as t3,\n" +
//            "(SELECT count(*) AS passListCount FROM apply_info a \n" +
//            "WHERE create_user in ?1 AND version = ?2 \n" +
//            "and (((a.status = '1000' and LENGTH(a.apply_num) = 8 ) or a.status = '1100') " +
//            "and ( a.wx_state is null or a.wx_state != '0' ) " +
//            "and ( a.icbc_state is null or a.icbc_state = '1000'))\n" +
//            ") as t4\n")
//    Object[] findApprovalCount(List<String> userList, String version);


    /**
     * 查询列表数量
     * @param userList
     * @param version
     * @return
     */
    @Query(nativeQuery = true, value = "select t1.toSubmitListCount,t2.backListCount,t3.approvalListCount,t4.passListCount from (\n" +
            "SELECT count(*) AS toSubmitListCount FROM apply_info a \n" +
            "WHERE create_user in ?1 AND version = ?2 \n" +
            "and ( a.status = '0' or (a.status = '1000' and a.wx_state = '0'))\n" +
            ") as t1,(\n" +
            "SELECT count(*) AS backListCount FROM apply_info a \n" +
            "WHERE create_user in ?1 AND version = ?2 \n" +
            "and ( a.status = '300' or (a.status = '1000' and a.origin = '3' and a.icbc_state = '300'))\n" +
            ") as t2,\n" +
            "(SELECT count(*) AS approvalListCount FROM apply_info a \n" +
            "WHERE create_user in ?1 AND version = ?2 \n" +
            "and ( a.status = '100' or (a.status = '1000' and a.origin = '3' and a.icbc_state = '100'))\n" +
            ") as t3,\n" +
            "(SELECT count(*) AS passListCount FROM apply_info a \n" +
            "WHERE create_user in ?1 AND version = ?2 \n" +
            "and (((a.status = '1000' and LENGTH(a.apply_num) = 8 ) or a.status = '1100') " +
            "and ( a.wx_state is null or a.wx_state != '0' ) " +
            "and ( a.icbc_state is null or a.icbc_state = '1000'))\n" +
            ") as t4\n")
    Object[] findApprovalCount(List<String> userList, String version);


    List<ApplyInfo> findByVersionAndStatus(String version, String status);
    List<ApplyInfo> findByVersionAndIcbcState(String version, String status);
    List<ApplyInfo> findByVersionAndStatusAndOrigin(String version, String status, String origin);
    @Query(nativeQuery = true, value = "SELECT t1.* FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status in ('1000','2000','3000','3500','4000') and length(t1.apply_num)<10")
    List<ApplyInfo> findAuthCount(List<String> userList, String version);
    List<ApplyInfo> findByCreateUser(String userName);
    //查询预审批月申请量拒绝的
    @Query(nativeQuery = true, value = "SELECT to_char(create_time, 'YYYY-MM'), count(*) FROM apply_info where status='1100' and create_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(create_time, 'YYYY-MM') ORDER BY to_char(create_time, 'YYYY-MM')")
    List<Object> findMonthDataRefuse(String bginTime, String endTime);
    //查询预审批月申请量通过的
    @Query(nativeQuery = true, value = "SELECT to_char(create_time, 'YYYY-MM'), count(*) FROM apply_info where status >= '1000' and status <> '1100' and create_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(create_time, 'YYYY-MM') ORDER BY to_char(create_time, 'YYYY-MM')")
    List<Object> findMonthDataPass(String bginTime, String endTime);
    //查询在线申请月申请量拒绝的
    @Query(nativeQuery = true, value = "SELECT to_char(submit_time, 'YYYY-MM'), count(*) FROM apply_info where status='3500' and submit_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(submit_time, 'YYYY-MM') ORDER BY to_char(submit_time, 'YYYY-MM')")
    List<Object> findMonthDataRefuseOnline(String bginTime, String endTime);
    //查询在线申请月申请量通过的
    @Query(nativeQuery = true, value = "SELECT to_char(submit_time, 'YYYY-MM'), count(*) FROM apply_info where status = '3000' and submit_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(submit_time, 'YYYY-MM') ORDER BY to_char(submit_time, 'YYYY-MM')")
    List<Object> findMonthDataPassOnline(String bginTime, String endTime);
    //查询在线申请月申请量退回待修改的
    @Query(nativeQuery = true, value = "SELECT to_char(submit_time, 'YYYY-MM'), count(*) FROM apply_info where status = '4000' and submit_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(submit_time, 'YYYY-MM') ORDER BY to_char(submit_time, 'YYYY-MM')")
    List<Object> findMonthDataReturnOnline(String bginTime, String endTime);

}
