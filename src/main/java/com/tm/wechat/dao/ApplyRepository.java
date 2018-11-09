package com.tm.wechat.dao;

import com.tm.wechat.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by pengchao on 2017/7/11.
 */
public interface ApplyRepository extends JpaRepository<Apply, String> {
    //查询车辆信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#carInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findCarInfo(String uniqueMark, String version);

    //查询产品方案
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#productPlanDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findProductPlan(String uniqueMark, String version);


    //查询车辆信息、车辆抵押信息、融资信息、二手车评估信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE (t1.item_key LIKE '%#carInfoDto%' OR t1.item_key LIKE '%#carPledgeDto%' OR t1.item_key LIKE '%#financeInfoDto%') AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findProductInfo(String uniqueMark, String version);

    //查询车辆抵押信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#carPledgeDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findCarPledgeInfo(String uniqueMark, String version);

    //查询融资信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#financeInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findFinanceInfo(String uniqueMark, String version);

    //查询客户详细信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#detailedDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findDetailedInfo(String uniqueMark, String version);

    //查询客户职业信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#workInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findWorkInfo(String uniqueMark, String version);

    //查询共申人信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#jointInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findJointInfo(String uniqueMark, String version);

    //查询客户地址信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#addressInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findAddressInfo(String uniqueMark, String version);

    //查询客户附件信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#applyFileDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findApplyFileInfo(String uniqueMark, String version);

    //查询共申人附件信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#jointFileDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findJointFileInfo(String uniqueMark, String version);

    //查询担保人附件信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#guaranteeFileDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findGuaranteeFileInfo(String uniqueMark, String version);

    //查询配偶附件信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#mateFileDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findMateFileInfo(String uniqueMark, String version);

    //查询二手车评估信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#usedCarEvaluationDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findUsedCarEvaluation(String uniqueMark, String version);

    //查询二手车评估结果信息信息
    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key LIKE '%#usedCarEvaluationResultDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findUsedCarEvaluationResult(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.item_key = '#usedCarEvaluationDto#carBillId' AND t1.item_string_value =?1 AND t1.version =?2 ORDER BY create_time DESC LIMIT 1")
    Apply findUsedCarEvaluationNum (String carBillId, String version);

    //查询申请创建未提交列表
    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_time as createtime, B.create_user as createUser from (select * from approval t where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status = '1000' and length(t1.apply_num)<10 and (t1.wx_state <> '0' or t1.wx_state is null)) and t.item_key = '#idCardInfoDto#name') A INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid WHERE (B.wx_state <> '0'  or B.wx_state is null) and (A.item_string_value like ?3 or B.apply_num like ?3) ORDER BY B.update_time DESC")
    List<Object> findLocalInfo(List<String> userList, String version, String condition);

    //查询退回待修改列表
    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.reason as reason, B.status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_user as createUser from (select * from approval t where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status = '4000' and length(t1.apply_num)<10)  and t.item_key = '#idCardInfoDto#name') A INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid WHERE A.item_string_value like ?3 or B.apply_num like ?3 ORDER BY B.update_time DESC")
    List<Object> findBackInfo(List<String> userList, String version, String condition);

    //查询申请审批中列表
    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.status as status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_user as createUser from (select * from approval t where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status = '2000' and length(t1.apply_num)<10)  and t.item_key = '#idCardInfoDto#name') A INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid WHERE A.item_string_value like ?3 or B.apply_num like ?3 ORDER BY B.update_time DESC")
    List<Object> findSubmitInfo(List<String> userList, String version, String condition);

    //查询审批已完成列表
    @Query(nativeQuery = true, value = "select " +
            "A.item_string_value as name, " +
            "B.reason as reason, " +
            "B.status as status," +
            "B.approval_uuid as uniqueMark, " +
            "B.update_time as time," +
            "B.apply_num as applyNum ," +
            "C.item_string_value as idCardNum, " +
            "D.item_string_value as phoneNumber, " +
            "B.create_user as createUser " +
            "from " +
            "(select * from approval t " +
            "where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status in ?3  and length(t1.apply_num)<10) and t.item_key = '#idCardInfoDto#name') A " +
            "left join (select * from approval t3 " +
            "where t3.item_key = '#idCardInfoDto#idCardNum') C on C.unique_mark = A.unique_mark " +
            "left join (select * from approval t2 " +
            "where t2.item_key = '#otherInfoDto#phoneNumber') D on D.unique_mark = A.unique_mark " +
            "INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid " +
            "WHERE A.item_string_value like ?4 or B.apply_num like ?4 ORDER BY B.update_time DESC")
    List<Object> findAchieveInfo(List<String> userList, String version, List<String> statusList, String condition);


    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.unique_mark =?1 AND t1.version =?2")
    List<Apply> findFullInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM apply t1 WHERE t1.unique_mark = (SELECT t.approval_uuid FROM apply_info t where t.apply_num =?1) AND t1.version =?2")
    List<Apply> findApplyInfo(String applyNum, String version);//根据申请编号查询预审批所有信息

    //根据二手车图片类型查询图片URL
    @Query(nativeQuery = true, value = "SELECT " +
            "t1.item_key, " +
            "t1.item_string_value, " +
            "t1.unique_mark, " +
            "t2.image_class " +
            "FROM " +
            "apply t1, " +
            "(SELECT item_key, unique_mark, item_string_value image_class FROM apply WHERE unique_mark =?1 and (item_string_value = '行驶证'OR item_string_value = '登记证')) t2 " +
            "WHERE t1.unique_mark = t2.unique_mark " +
            "AND t1.item_key =(SELECT REPLACE(t2.item_key, 'imageClass', 'imageUrl'))")
    List<Object> getUsedCarImageUrl(String uniqueMark);

    //查询车辆新旧
    @Query(nativeQuery = true, value = "SELECT item_string_value FROM apply t1 WHERE t1.item_key = '#productPlanDto#mainType' AND t1.unique_mark =?1 AND t1.version =?2")
    String findMainType(String uniqueMark, String version);

}
