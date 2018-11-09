package com.tm.wechat.dao.sign;


import com.tm.wechat.domain.VideoSignRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2018/8/28.
 */
public interface VideoSignRecordRepository extends JpaRepository<VideoSignRecord, String> {
    List<VideoSignRecord> findByState(String state);
    VideoSignRecord findTop1ByApplyNumAndStateOrderByUpdateTimeDesc(String applyNum, String state);
    VideoSignRecord findTop1ByApplyNumOrderByUpdateTimeDesc(String applyNum);

}
