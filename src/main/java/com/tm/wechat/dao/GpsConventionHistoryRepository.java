package com.tm.wechat.dao;


import com.tm.wechat.domain.GpsConventionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * GPS邀约信息提交历史Repository
 * Created by zcHu on 17/5/11.
 */
public interface GpsConventionHistoryRepository extends JpaRepository<GpsConventionHistory, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM gps_convention_history t1 WHERE t1.create_user = ?1 and (t1.apply_num like ?2 or t1.contacts_name like ?2) order by create_time desc")
    List<GpsConventionHistory> findByUserAndCondition(String userId, String condition);
}
