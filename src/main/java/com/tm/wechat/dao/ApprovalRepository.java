package com.tm.wechat.dao;

import com.tm.wechat.domain.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zcHu on 17/5/10.
 */
public interface ApprovalRepository extends JpaRepository<Approval, String>{
//    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#productSourceDto%' AND t1.unique_mark =?1 AND t1.version =?2")
//    List<Approval> findProductSourceInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#idCardInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findIdCardInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#driveLicenceInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findDriveInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#bankCardInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findBankInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#otherInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findOtherInfo(String uniqueMark, String version);


    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#maritalStatusDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findMaritalStatusInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#mateInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findMateInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#contactInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findContactInfo(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#approvalAttachmentDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findApprovalAttachment(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE (t1.item_key LIKE '%#approvalAttachmentDto%' OR  t1.item_key LIKE '%#idCardInfoDto#%Img%' ) AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findApprovalAttachmentAndIdCardInfoImg(String uniqueMark, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.item_key LIKE '%#attachmentInfoDto%' AND t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findattachmentInfo(String uniqueMark, String version);

//    @Query(nativeQuery = true, value = "SELECT * FROM (SELECT A.approval_uuid,A.create_user,A.create_time ,B.item_string_value,A.origin,A.wx_state\n" +
//            "FROM apply_info A LEFT JOIN (\n" +
//            "        SELECT A.approval_uuid,B.*\n" +
//            "         FROM apply_info A INNER JOIN approval B ON A.approval_uuid = B.unique_mark\n" +
//            "         WHERE A.create_user in ?1\n" +
//            "            AND A.version =?2\n" +
//            "            AND (A.status = '0' or (A.status in ('100','300','1000') and A.origin = '3' and A.icbc_state = '0') or (A.status = '1000' and A.origin = '1' and A.wx_state = '0'))\n" +
//            "            AND (B.item_key = '#idCardInfoDto#name')\n" +
//            "    ) B ON A.approval_uuid = B.approval_uuid \n" +
//            "WHERE A.create_user in ?1 \n" +
//            "     AND A.version = ?2\n" +
//            "      AND (A.status = '0' or (A.status in ('100','300','1000') and A.origin = '3' and A.icbc_state = '0') or (A.status = '1000' and A.origin = '1' and A.wx_state = '0')) \n" +
//            "ORDER BY A.create_time DESC) C  WHERE C.item_string_value LIKE ?3 or C.approval_uuid LIKE ?3")
//    List<Object> findLocalInfo(List<String> userNameList, String version, String search);


    @Query(nativeQuery = true, value = "SELECT DISTINCT A.approval_uuid,A.create_user,A.create_time ,B.item_string_value,A.origin,A.wx_state\n" +
            "FROM apply_info_new A \n" +
            "LEFT JOIN approval B ON A.approval_uuid = B.unique_mark  \n" +
            "WHERE A.create_user in ?1 AND A.version =?2 AND B.item_string_value LIKE ?3  AND A.origin in ?4 AND \n" +
            "( A.hpl_status = '0' or A.product_status = '0' or (A.hpl_status = '1000' and A.wx_state = '0')) " +
            "AND B.item_key = '#idCardInfoDto#name'" +
            "ORDER BY A.create_time DESC LIMIT ?5 OFFSET ?6 \n")
    List<Object> findLocalInfo(List<String> userNameList, String version, String search,List<String> originTypeList, Integer size, Integer pageSize);


    @Query(nativeQuery = true, value = "select B.item_string_value as name, A.apply_num, A.hpl_reason, A.hpl_status, A.approval_uuid, A.update_time, A.create_user,A.product_status,A.product_reason,A.origin\n" +
            "FROM apply_info_new A LEFT JOIN approval B on B.unique_mark = A.approval_uuid \n" +
            "WHERE A.create_user in ?1 AND A.version =?2 \n" +
            "AND (A.hpl_status = '300' or A.product_status = '300')\n" +
            "AND B.item_key = '#idCardInfoDto#name'\n" +
            "ORDER BY A.update_time DESC")
    List<Object> findBackInfo(List<String> userNameList, String version);

//    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.reason_icbc as reason, B.status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_user as createUser from " +
//            "(select * from approval t where t.unique_mark in " +
//            "(SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and (t1.status = '1000' and t1.origin = '3' and t1.icbc_state = '300'))  and t.item_key = '#idCardInfoDto#name') A " +
//            "INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid AND (A.item_string_value LIKE ?3 OR B.apply_num LIKE ?3) ORDER BY B.update_time DESC LIMIT ?4 OFFSET ?5")
//    List<Object> findICBCAttachmentBackInfo(List<String> userNameList, String version, String condition, Integer size, Integer pageSize);
//    @Query(nativeQuery = true, value = "select DISTINCT B.item_string_value as name, A.apply_num, A.product_reason, A.hpl_status, A.approval_uuid, A.update_time, A.create_user\n" +
//            "FROM apply_info_new A LEFT JOIN approval B on B.unique_mark = A.approval_uuid \n" +
//            "WHERE A.create_user in ?1 and A.version = ?2 \n" +
//            "AND (A.hpl_status = '1000' and A.origin = '3' and A.product_status = '300')\n" +
//            "AND B.item_key = '#idCardInfoDto#name'\n" +
//            "AND (B.item_string_value LIKE ?3 OR A.apply_num LIKE ?3)\n" +
//            "ORDER BY A.update_time DESC LIMIT ?4 OFFSET ?5")
//    List<Object> findICBCAttachmentBackInfo(List<String> userNameList, String version, String condition, Integer size, Integer pageSize);


//    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.reason as reason, B.status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_user as createUser from " +
//            "(select * from approval t where t.unique_mark in " +
//            "(SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and (t1.status = '1000' and t1.origin = '3' and t1.icbc_state = '300'))  and t.item_key = '#idCardInfoDto#name') A " +
//            "INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid AND (A.item_string_value LIKE ?3 OR B.apply_num LIKE ?3) ORDER BY B.update_time DESC")
//    List<Object> findICBCAttachmentBackCount(List<String> userNameList, String version, String condition);
//    @Query(nativeQuery = true, value = "select count(DISTINCT A.approval_uuid)\n" +
//            "FROM apply_info_new A LEFT JOIN approval B on B.unique_mark = A.approval_uuid \n" +
//            "WHERE A.create_user in ?1 and A.version = ?2 \n" +
//            "AND (A.hpl_status = '1000' and A.origin = '3' and A.product_status = '300')\n" +
//            "AND B.item_key = '#idCardInfoDto#name'\n" +
//            "AND (B.item_string_value LIKE ?3 OR A.apply_num LIKE ?3)\n")
//    List<Object> findICBCAttachmentBackCount(List<String> userNameList, String version, String condition);

    @Query(nativeQuery = true, value = "select DISTINCT B.item_string_value as name, A.apply_num, A.product_reason, A.hpl_status, A.approval_uuid, A.update_time, A.create_user,A.origin \n" +
            "FROM apply_info_new A LEFT JOIN approval B on B.unique_mark = A.approval_uuid \n" +
            "WHERE A.create_user in ?1 and A.version = ?2 \n" +
            "AND (A.hpl_status = '1000' and A.origin in ?4 and A.product_status = '300')\n" +
            "AND B.item_key = '#idCardInfoDto#name'\n" +
            "AND (B.item_string_value LIKE ?3 OR A.apply_num LIKE ?3)\n" +
            "ORDER BY A.update_time DESC LIMIT ?5 OFFSET ?6")
    List<Object> findAttachmentBackInfo(List<String> userNameList, String version, String condition,List<String> origin, Integer size, Integer pageSize);

    @Query(nativeQuery = true, value = "select count(DISTINCT A.approval_uuid)\n" +
            "FROM apply_info_new A LEFT JOIN approval B on B.unique_mark = A.approval_uuid \n" +
            "WHERE A.create_user in ?1 and A.version = ?2 \n" +
            "AND (A.hpl_status = '1000' and A.origin in ?4 and A.product_status = '300')\n" +
            "AND B.item_key = '#idCardInfoDto#name'\n" +
            "AND (B.item_string_value LIKE ?3 OR A.apply_num LIKE ?3)\n")
    List<Object> findAttachmentBackCount(List<String> userNameList, String version, String condition,List<String> origin);

    //    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.status as status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_user as createUser, B.approval_submit_time as approvalSubmitTime " +
//            "from (select * from approval t where t.unique_mark in (" +
//            "SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and (t1.status = '100' and (t1.icbc_state in ('100','300','1000') or t1.icbc_state is null OR t1.icbc_state = '') or (t1.status = '1000' and t1.origin = '3' " +
//            "and t1.icbc_state = '100')))  and t.item_key = '#idCardInfoDto#name') A " +
//            "INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid ORDER BY B.update_time DESC")
//    List<Object> findSubmitInfo(List<String> userNameList, String version);
    @Query(nativeQuery = true, value = "select DISTINCT B.item_string_value as name, A.apply_num, A.hpl_status, A.approval_uuid, A.update_time, A.create_user, A.approval_submit_time\n" +
            "from apply_info_new A LEFT JOIN approval B ON A.approval_uuid = B.unique_mark\n" +
            "WHERE  A.create_user in ?1 and A.version = ?2\n" +
            "AND (\n" +
            "(A.hpl_status = '100' and ( A.product_status in ('100','300','1000') or A.product_status is null OR A.product_status = '' ))\n" +
            "or\n" +
            "(A.hpl_status = '1000' and A.product_status = '100' )\n" +
            ")\n" +
            "and B.item_key = '#idCardInfoDto#name'\n" +
            "ORDER BY A.update_time DESC LIMIT ?3 OFFSET ?4")
    List<Object> findSubmitInfo(List<String> userNameList, String version, Integer size, Integer pageSize);

//    @Query(nativeQuery = true, value = "select " +
//            "A.item_string_value as name, " +
//            "B.reason as reason, " +
//            "B.status as status," +
//            "B.approval_uuid as uniqueMark, " +
//            "B.is_auto_approval as isAutoApproval, " +
//            "B.update_time as time," +
//            "B.apply_num as applyNum ," +
//            "C.item_string_value as idCardNum, " +
//            "D.item_string_value as phoneNumber, " +
//            "B.certification_status as certificationStatus, " +
//            "B.create_user as createUser, " +
//            "B.origin as origin, " +
//            "B.icbc_state as icbcState " +
//            "from " +
//            "(select * from approval t " +
//            "where t.unique_mark in ((SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status =?3  and (t1.icbc_state = '1000' or t1.icbc_state is null OR t1.icbc_state = '') " +
//            "and (t1.wx_state <> '0' or t1.wx_state is null) and length(t1.apply_num)<10) UNION (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and (t1.status =?4 or t1.icbc_state = '1100'))) and t.item_key = '#idCardInfoDto#name') A " +
//            "left join (select * from approval t3 " +
//            "where t3.item_key = '#idCardInfoDto#idCardNum') C on C.unique_mark = A.unique_mark " +
//            "left join (select * from approval t2 " +
//            "where t2.item_key = '#otherInfoDto#phoneNumber') D on D.unique_mark = A.unique_mark " +
//            "INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid " +
//            "WHERE (A.item_string_value like ?5 " +
//            "OR B.apply_num like ?5) " +
//            "AND (B.wx_state <> '0' or B.wx_state is null)" +
//            "ORDER BY B.update_time DESC")
//    List<Object> findAchieveInfo(List<String> userNameList, String version, String passStatus, String refuseStatus, String condition);
    @Query(nativeQuery = true, value = "select B.item_string_value as name,A.hpl_reason,A.hpl_status,A.approval_uuid,A.update_time,A.apply_num,C.idCardNum,D.phoneNumber,A.create_user,A.origin,A.product_status\n" +
            "from Apply_info_new A \n" +
            "LEFT JOIN approval B on B.unique_mark = A.approval_uuid\n" +
            "LEFT JOIN (select t3.item_string_value idCardNum,t3.unique_mark unique_markc  from approval t3 \n" +
            "where t3.item_key = '#idCardInfoDto#idCardNum') C on C.unique_markc = A.approval_uuid\n" +
            "left join (select t2.item_string_value phoneNumber,t2.unique_mark unique_markd from approval t2  \n" +
            "where t2.item_key = '#otherInfoDto#phoneNumber') D on D.unique_markd = A.approval_uuid\n" +
            "WHERE \n" +
            "A.create_user in ?1 and A.version = ?2 AND A.origin in ?3  AND (B.item_string_value like ?6 OR A.apply_num like ?6  )  \n" +
            "AND (\n" +
            " -- hpl 或 产品提供商 拒绝的\n" +
            " (A.hpl_status = ?5 or A.product_status = ?5)\n" +
            " -- hpl 天启通过（自有产品,助力融）\n" +
            " or (A.hpl_status = ?4 and A.origin in ('0','2') )\n" +
            " -- hpl天启、产品提供商 都通过\n" +
            " or (A.hpl_status = ?4 and A.product_status = ?4)\n" +
            " -- 微信过来的单子，hpl 天启过了，微信保单信息完整就算成功了\n" +
            " or (A.hpl_status = ?4 and A.wx_state = '1' and a.origin = '1')\n" +
            ")\n" +
            "AND B.item_key = '#idCardInfoDto#name'\n" +
            "ORDER BY B.update_time DESC LIMIT ?7 OFFSET ?8 ")
    List<Object> findAchieveInfo(List<String> userNameList, String version,List<String> originTypeList, String passStatus, String refuseStatus, String condition, Integer size, Integer pageSize);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.unique_mark =?1 AND t1.version =?2")
    List<Approval> findFullInfo(String uniqueMark, String version);


    @Query(nativeQuery = true, value = "SELECT * FROM (SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\tapproval t1\n" +
            "WHERE\n" +
            "\tt1.item_key LIKE '%#otherInfoDto%'\n" +
            "OR t1.item_key LIKE '%#idCardInfoDto%'\n" +
            "OR t1.item_key LIKE '%#driveLicenceInfoDto%'\n" +
            "OR t1.item_key LIKE '%#attachmentInfoDto%'\n" +
            "OR t1.item_key LIKE '%#bankCardInfoDto%'\n" +
            ") t2\n" +
            "WHERE t2.unique_mark = ?1 \n" +
            "AND t2.create_user = ?2 \n" +
            "AND t2. VERSION = ?3 ")
    List<Approval> findBasicInfo(String uniqueMark, String userName, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.unique_mark = (SELECT t2.approval_uuid FROM apply_info_new t2 where t2.apply_num =?1 and t2.version =?2 and t2.status = '1000') AND t1.version =?2")
    List<Approval> findAllInfo(String applyNum, String version);

    @Query(nativeQuery = true, value = "select " +
            "A.item_string_value as name, " +
            "B.reason as reason, " +
            "B.status as status," +
            "B.approval_uuid as uniqueMark, " +
            "B.is_auto_approval as isAutoApproval, " +
            "B.update_time as time," +
            "B.apply_num as applyNum ," +
            "C.item_string_value as idCardNum, " +
            "D.item_string_value as phoneNumber, " +
            "B.certification_status as certificationStatus, " +
            "B.create_user as createUser " +
            "from " +
            "(select * from approval t " +
            "where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info_new t1 where t1.create_user in ?1 and t1.version =?2 and t1.status in ('1000','2000','3000','3500','4000') " +
            "and length(t1.apply_num)<10 and (t1.certification_status is null or t1.certification_status = '' or t1.certification_status = '000')) and t.item_key = '#idCardInfoDto#name') A " +
            "left join (select * from approval t3 " +
            "where t3.item_key = '#idCardInfoDto#idCardNum') C on C.unique_mark = A.unique_mark " +
            "left join (select * from approval t2 " +
            "where t2.item_key = '#otherInfoDto#phoneNumber') D on D.unique_mark = A.unique_mark " +
            "INNER JOIN apply_info_new B ON A.unique_mark = B.approval_uuid ORDER BY B.update_time DESC")
    List<Object> findAuthInfo1(List<String> userList, String version);

    @Query(nativeQuery = true, value = "select " +
            "A.item_string_value as name, " +
            "B.reason as reason, " +
            "B.status as status," +
            "B.approval_uuid as uniqueMark, " +
            "B.is_auto_approval as isAutoApproval, " +
            "B.update_time as time," +
            "B.apply_num as applyNum ," +
            "C.item_string_value as idCardNum, " +
            "D.item_string_value as phoneNumber, " +
            "B.certification_status as certificationStatus, " +
            "B.create_user as createUser " +
            "from " +
            "(select * from approval t " +
            "where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info_new t1 where t1.create_user in ?1 and t1.version =?2 and t1.status in ('1000','2000','3000','3500','4000') " +
            "and length(t1.apply_num)<10 and (t1.certification_status is not null and t1.certification_status <> '' and  t1.certification_status <> '000')) and t.item_key = '#idCardInfoDto#name') A " +
            "left join (select * from approval t3 " +
            "where t3.item_key = '#idCardInfoDto#idCardNum') C on C.unique_mark = A.unique_mark " +
            "left join (select * from approval t2 " +
            "where t2.item_key = '#otherInfoDto#phoneNumber') D on D.unique_mark = A.unique_mark " +
            "INNER JOIN apply_info_new B ON A.unique_mark = B.approval_uuid ORDER BY B.update_time DESC")
    List<Object> findAuthInfo2(List<String> userList, String version);

    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.unique_mark = (SELECT t.approval_uuid FROM apply_info_new t where t.apply_num =?1) AND t1.version =?2")
    List<Approval> findApprovalInfo(String applyNum, String version);//根据申请编号查询预审批所有信息

    List<Approval> findByUniqueMarkAndVersion(String uniqueMark, String version);


    @Query(nativeQuery = true, value = "SELECT A .item_string_value AS NAME, B.apply_num AS applyNum,B.hpl_status,B.approval_uuid AS uniqueMark,B.update_time AS TIME,B.create_time AS CREATETIME,B.create_user AS CREATEUSER " +
            "FROM (SELECT * FROM approval T WHERE T .unique_mark IN (" +
            "SELECT t1.approval_uuid FROM apply_info_new t1 WHERE t1.create_user IN ?1 AND t1. VERSION =?2 AND t1.hpl_status IN ( '1000','0','100','2000','4000')) " +
            "AND T .item_key = '#idCardInfoDto#name') A INNER JOIN apply_info_new B ON A .unique_mark = B.approval_uuid " +
            "AND (A .item_string_value LIKE ?3 OR B.apply_num LIKE ?3) ORDER BY B.update_time DESC")
    List<Object> findApplyList(List<String> userList, String version, String condition);//查询所有的审批订单


    @Query(nativeQuery = true, value = "SELECT A .item_string_value AS NAME, B.apply_num AS applyNum,B.hpl_status,B.approval_uuid AS uniqueMark,B.update_time AS TIME,B.create_time AS CREATETIME,B.create_user AS CREATEUSER " +
            "FROM (SELECT * FROM approval T WHERE T .unique_mark IN (" +
            "SELECT t1.approval_uuid FROM apply_info_new t1 WHERE t1.create_user IN ?1 AND t1. VERSION =?2 AND t1.hpl_status = ?3 ) " +
            "AND T .item_key = '#idCardInfoDto#name') A INNER JOIN apply_info_new B ON A .unique_mark = B.approval_uuid " +
            "AND (A .item_string_value LIKE ?4 OR B.apply_num LIKE ?4) ORDER BY B.update_time DESC")
    List<Object> findApplyListByStatus(List<String> userList, String version, String status, String condition);//查询所有的审批订单

    //查询用户四要素信息
    @Query(nativeQuery = true, value = "SELECT * FROM approval WHERE " +
            "(item_key = '#idCardInfoDto#name' " +
            "OR item_key = '#idCardInfoDto#idCardNum' " +
            "OR item_key = '#otherInfoDto#phoneNumber' " +
            "OR item_key = '#bankCardInfoDto#bank' " +
            "OR item_key = '#bankCardInfoDto#accountNum') " +
            "AND unique_mark = ?1 AND version =?2")
    List<Approval> findUserBasicInfo(String uniqueMark, String version);



    //查询用户四要素信息
    @Query(nativeQuery = true, value = "SELECT * FROM approval WHERE " +
            "(item_key = '#idCardInfoDto#name' " +
            "OR item_key = '#idCardInfoDto#idCardNum' " +
            "OR item_key = '#bankCardInfoDto#bankPhoneNum' " +
            "OR item_key = '#bankCardInfoDto#bank' " +
            "OR item_key = '#bankCardInfoDto#accountNum' " +
            "OR item_key = '#bankCardInfoDto#name') " +
            "AND unique_mark = ?1 AND version =?2")
    List<Approval> findWeBankUserBasicInfo(String uniqueMark, String version);


    //查询预审批银行卡照片
    @Query(nativeQuery = true, value = "SELECT * FROM approval t1 WHERE t1.unique_mark = (SELECT t2.approval_uuid FROM apply_info_new t2 where t2.apply_num =?1 and t2.version =?2 ) AND t1.version =?2 AND t1. item_key = '#bankCardInfoDto#bankImg' ")
    Approval findBankImg(String applyNum, String version);

    //根据经销商代码统计报单
    @Query(nativeQuery = true, value = "select A.create_user,count(A.create_user) \n" +
            "from Apply_info_new A  \n" +
            "WHERE  \n" +
            "A.create_user in ?1 \n" +
            "AND ( \n" +
            " -- hpl 或 产品提供商 拒绝的 \n" +
            " (A.hpl_status = ?3 or A.product_status = ?3) \n" +
            " -- hpl 天启通过（自有产品,助力融） \n" +
            " or (A.hpl_status = ?2 and A.origin in ('0','2') ) \n" +
            " -- hpl天启、产品提供商 都通过 \n" +
            " or (A.hpl_status = ?2 and A.product_status = ?2) \n" +
            " -- 微信过来的单子，hpl 天启过了，微信保单信息完整就算成功了 \n" +
            " or (A.hpl_status = ?2 and A.wx_state = '1' and a.origin = '1') \n" +
            ") \n" +
            "group by A.create_user")
    List<Object> findAchieveInfoCountGroupByFpName(List<String> userNameList, String passStatus, String refuseStatus);
}
