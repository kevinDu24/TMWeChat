package com.tm.wechat.dao;

import com.tm.wechat.domain.ApplyInfoNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/15 19:38
 *  Description: 预审批记录表
 */
public interface ApplyInfoNewRepository extends JpaRepository<ApplyInfoNew, String>{

    /**
     * 根据 uniqueMark （ApprovalUuid） 和 Version 查询预审批记录
     * @param uniqueMark
     * @param version
     * @return
     */
    ApplyInfoNew findTopByApprovalUuidAndVersion(String uniqueMark, String version);

    //根据申请单号查询
    ApplyInfoNew findByApplyNum(String applyNum);

    //根据创建用户查询
    List<ApplyInfoNew> findByCreateUser(String userName);

    /**
     * 查询各个状态的预审批列表数量
     * @param userList
     * @param version
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT\n" +
            "t1.toSubmitListCount,t2.backListCount,t3.approvalListCount,t4.passListCount \n" +
            "FROM( SELECT count( * ) AS toSubmitListCount FROM apply_info_new a WHERE \n" +
            "create_user in ?1 AND version = ?2 \n" +
            "AND ( a.hpl_status = '0' OR ( a.hpl_status = '1000' AND a.wx_state = '0' ) ) \n" +
            ") AS t1,(SELECT count( * ) AS backListCount FROM apply_info_new a WHERE \n" +
            "create_user in ?1 AND version = ?2 \n" +
            "AND (( a.hpl_status = '1000' AND a.product_status = '300' ) ) \n" +
            ") AS t2,(SELECT count( * ) AS approvalListCount FROM apply_info_new a WHERE \n" +
            "create_user in ?1 AND version = ?2 \n" +
            "AND ( a.hpl_status = '100' OR ( a.hpl_status = '1000' AND a.product_status = '100' ) ) \n" +
            ") AS t3,(SELECT count( * ) AS passListCount FROM apply_info_new a WHERE \n" +
            "create_user in ?1 AND version = ?2 \n" +
            "AND (-- hpl 或 产品提供商 拒绝的\n" +
            "( a.hpl_status = '1100' OR a.product_status = '1100' ) \n" +
            "OR -- hpl 天启通过（自有产品,助力融）\n" +
            "( a.hpl_status = '1000' AND a.origin IN ( '0', '2' ) ) \n" +
            "OR -- hpl天启、产品提供商 都通过\n" +
            "( a.hpl_status = '1000' AND a.product_status = '1000' ) \n" +
            "OR -- 微信过来的单子，hpl 天启过了，微信保单信息完整就算成功了\n" +
            "( a.hpl_status = '1000' AND a.wx_state = '1' AND a.origin = '1' ) \n" +
            ") ) AS t4")
    Object[] findApprovalCount(List<String> userList, String version);

    //根据版本号,hpl天启状态查询
    List<ApplyInfoNew> findByVersionAndHplStatus(String version, String hplStatus);

    //根据版本号，产品类型，产品审批状态查询
    List<ApplyInfoNew> findByVersionAndOriginAndProductStatus(String version,String origin,String productStatus);

    //根据版本号,hpl天启状态及产品类型查询
    List<ApplyInfoNew> findByVersionAndOriginAndHplStatus(String version, String origin, String hplStatus);

    //查询预审批月申请量拒绝的
    @Query(nativeQuery = true, value = "SELECT to_char(create_time, 'YYYY-MM'), count(*) FROM apply_info_new where status='1100' and create_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(create_time, 'YYYY-MM') ORDER BY to_char(create_time, 'YYYY-MM')")
    List<Object> findMonthDataRefuse(String bginTime, String endTime);
    //查询预审批月申请量通过的
    @Query(nativeQuery = true, value = "SELECT to_char(create_time, 'YYYY-MM'), count(*) FROM apply_info_new where status >= '1000' and status <> '1100' and create_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(create_time, 'YYYY-MM') ORDER BY to_char(create_time, 'YYYY-MM')")
    List<Object> findMonthDataPass(String bginTime, String endTime);
    //查询在线申请月申请量拒绝的
    @Query(nativeQuery = true, value = "SELECT to_char(submit_time, 'YYYY-MM'), count(*) FROM apply_info_new where status='3500' and submit_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(submit_time, 'YYYY-MM') ORDER BY to_char(submit_time, 'YYYY-MM')")
    List<Object> findMonthDataRefuseOnline(String bginTime, String endTime);
    //查询在线申请月申请量通过的
    @Query(nativeQuery = true, value = "SELECT to_char(submit_time, 'YYYY-MM'), count(*) FROM apply_info_new where status = '3000' and submit_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(submit_time, 'YYYY-MM') ORDER BY to_char(submit_time, 'YYYY-MM')")
    List<Object> findMonthDataPassOnline(String bginTime, String endTime);
    //查询在线申请月申请量退回待修改的
    @Query(nativeQuery = true, value = "SELECT to_char(submit_time, 'YYYY-MM'), count(*) FROM apply_info_new where status = '4000' and submit_time between to_date(?1 , 'YYYY-MM') and to_date(?2 , 'YYYY-MM') GROUP BY to_char(submit_time, 'YYYY-MM') ORDER BY to_char(submit_time, 'YYYY-MM')")
    List<Object> findMonthDataReturnOnline(String bginTime, String endTime);

}
