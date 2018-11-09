package com.tm.wechat.dao;

import com.tm.wechat.domain.PushRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/6/21.
 */
public interface PushRecordRepository extends JpaRepository<PushRecord, String> {
}
