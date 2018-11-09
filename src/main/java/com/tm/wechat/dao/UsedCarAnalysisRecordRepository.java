package com.tm.wechat.dao;

import com.tm.wechat.domain.UsedCarAnalysisRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/7/18.
 */
public interface UsedCarAnalysisRecordRepository extends JpaRepository<UsedCarAnalysisRecord, String> {
    UsedCarAnalysisRecord findByAnalysisId(String unique_mark);

}
