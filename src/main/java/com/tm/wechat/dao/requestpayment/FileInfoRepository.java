package com.tm.wechat.dao.requestpayment;

import com.tm.wechat.domain.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by pengchao on 2018/3/22.
 */
public interface FileInfoRepository extends JpaRepository<FileInfo, String> {

    List<FileInfo> findByApplyNum(String applyNum);

    FileInfo findTop1ByApplyNumAndName(String applyNum, String name);
    //查询申请创建未提交列表
    @Query(nativeQuery = true, value = "select A.item_string_value as name, B.apply_num as applyNum, B.status, B.approval_uuid as uniqueMark, B.update_time as time, B.create_time as createtime, B.create_user as createUser from (select * from approval t where t.unique_mark in (SELECT t1.approval_uuid FROM apply_info t1 where t1.create_user in ?1 and t1.version =?2 and t1.status = '31000' and length(t1.apply_num)<10 and (t1.wx_state <> '0' or t1.wx_state is null)) and t.item_key = '#idCardInfoDto#name') A INNER JOIN apply_info B ON A.unique_mark = B.approval_uuid WHERE (B.wx_state <> '0'  or B.wx_state is null) and (A.item_string_value like ?3 or B.apply_num like ?3) ORDER BY B.update_time DESC")
    List<Object> findLocalInfo(List<String> userList, String version, String condition);
}
