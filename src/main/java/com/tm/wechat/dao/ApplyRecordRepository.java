package com.tm.wechat.dao;

import com.tm.wechat.domain.ApplyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRecordRepository extends JpaRepository<ApplyRecord, String> {
    List<ApplyRecord> findByApprovalUuidOrderByCreateTimeDesc(String uniqueMark);

    ApplyRecord findTopByApprovalUuidAndOriginOrderByCreateTimeDesc(String uniqueMark,String origin);
}
