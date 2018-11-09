package com.tm.wechat.dao;

import com.tm.wechat.domain.UsedCarAnalysisFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/7/23.
 */
public interface UsedCarAnalysisFileRepository extends JpaRepository<UsedCarAnalysisFile, String> {
    UsedCarAnalysisFile findByAnalysisId(String unique_mark);
}
