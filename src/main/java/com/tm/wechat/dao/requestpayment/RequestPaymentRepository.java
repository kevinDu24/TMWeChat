package com.tm.wechat.dao.requestpayment;

import com.tm.wechat.domain.RequestPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by pengchao on 2018/4/16.
 */
public interface RequestPaymentRepository  extends JpaRepository<RequestPayment, String> {

    RequestPayment findByApplyNum(String applyNum);

    @Query(nativeQuery = true, value = "SELECT " +
            "gps_state,insurance_info_state,file_info_state " +
            "FROM " +
            "request_payment " +
            "WHERE apply_num =?1")
    Object getRequestStatus(String applyNum);

    @Query(nativeQuery = true, value = "SELECT * FROM request_payment t1 WHERE t1.submit_user = ?1 and (t1.insurance_info_submit_state = '1' or t1.file_info_submit_state = '1')and (t1.apply_num like ?2 or t1.name like ?2) order by create_time desc")
    List<RequestPayment> findBySubmitUserAndCondition(String userId, String condition);
}
