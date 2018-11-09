package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.GpsInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/3/19.
 */
public interface GpsInfoRepository extends JpaRepository<GpsInfo, String> {

    GpsInfo findByApplyNum(String applyNum);

}
