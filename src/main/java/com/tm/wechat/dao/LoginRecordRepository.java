package com.tm.wechat.dao;

import com.tm.wechat.domain.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LEO on 16/10/2.
 */
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long>{

    List<LoginRecord> findByUserNameAndCustomerOrderByTimeDesc(String userName, String customer);

    LoginRecord findTop1ByUserNameAndCustomerAndDeviceIdOrderByTimeDesc(String userName, String customer, String deviceId);

    /**
     * 获取最后一次登录记录
     * @param userName
     * @param customer
     * @return
     */
    LoginRecord findTop1ByUserNameAndCustomerOrderByTimeDesc(String userName, String customer);
}
