package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.ApplyContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by pengchao on 2018/3/14.
 */
public interface ApplyContractRepository extends JpaRepository<ApplyContract, String> {
    //查询合同未提交列表
    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_time as createtime, B.create_user as createUser,C .item_string_value AS root,E .item_string_value AS mainType " +
            "from (select * from approval t where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status = '3000' and length(t1.apply_num)<10 and (t1.wx_state <> '0' or t1.wx_state is null)) and t.item_key = '#idCardInfoDto#name') A " +
            "INNER JOIN(SELECT * FROM apply t2 WHERE t2.item_key = '#productPlanDto#root') C ON C .unique_mark = A .unique_mark " +
            "INNER JOIN (SELECT * FROM apply t4 WHERE t4.item_key = '#productPlanDto#mainType') E ON E .unique_mark = A .unique_mark "+
            "INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid WHERE (B.wx_state <> '0'  or B.wx_state is null) and (A.item_string_value like ?3 or B.apply_num like ?3) " +
            "AND B.create_time between to_date(?4 , 'YYYYMMDD') and to_date(?5 , 'YYYYMMDD') + 1  ORDER BY B.update_time DESC")
    List<Object> findLocalInfo(List<String> userList, String version, String condition, String beginTime, String endTime);

    ApplyContract findByApplyNum(String applyNum);
    
    ApplyContract findTopByApplyNum(String applyNum);

    @Query(nativeQuery = true, value = "SELECT " +
            "t1.state as contractStatus," +
            "t1.isICBC as isICBC," +
            "t2.state as jointContractStatus," +
            "t3.state as guaranteeStatus," +
            "t1.name as applyName," +
            "t1.id_card as applyIdCard," +
            "t2.name as jointName," +
            "t2.id_card as jointIdCard," +
            "t3.name as guaranteeName," +
            "t3.id_card as guaranteeIdCard " +
            "FROM " +
            "apply_contract t1 " +
            "LEFT JOIN joint_contract t2 ON t1.ID = t2.apply_contract_id " +
            "LEFT JOIN guarantee_contract t3 ON t1.ID = t3.apply_contract_id " +
            "WHERE t1.apply_num =?1")
    Object getSignStatus(String applyNum);

    @Query(nativeQuery = true, value = "SELECT * FROM apply_contract t1 WHERE t1.submit_user = ?1 and t1.submit_state = '1' and (t1.apply_num like ?2 or t1.name like ?2) order by create_time desc")
    List<ApplyContract> findBySubmitUserAndCondition(String userId, String condition);

}
