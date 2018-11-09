package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.CarSignInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by pengchao on 2018/4/10.
 */
public interface CarSignInfoRepository  extends JpaRepository<CarSignInfo, String> {

    CarSignInfo findByApplyNum(String applyNum);

    @Query(nativeQuery = true, value = "SELECT " +
            "t1.apply_num as applyNum, " +
            "t1.submit_state as carInfoStatus," +
            "t2.auth_state as bankInfoState " +
            "FROM " +
            "car_sign_info t1 " +
            "LEFT JOIN bank_card_sign t2 ON t1.apply_num = t2.apply_num " +
            "WHERE t1.apply_num =?1")
    Object getInfoStatus(String applyNum);
}
