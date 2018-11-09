package com.tm.wechat.dao;


import com.tm.wechat.domain.OrderAssignHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 订单分配历史表Repository
 * Created by zcHu on 17/5/11.
 */
public interface OrderAssignHistoryRepository extends JpaRepository<OrderAssignHistory, String> {
    @Query(nativeQuery = true, value = "SELECT * FROM order_assign_history t1 WHERE t1.create_user = ?1 and (t1.apply_num like ?2 or t1.name like ?2) order by create_time desc")
    List<OrderAssignHistory> findBySourceUserAndCondition(String userId, String condition);
}
