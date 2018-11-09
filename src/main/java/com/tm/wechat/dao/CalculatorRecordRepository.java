package com.tm.wechat.dao;

import com.tm.wechat.domain.CalculatorRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by LEO on 16/9/13.
 */
public interface CalculatorRecordRepository extends JpaRepository<CalculatorRecord, Long> {
    Page<CalculatorRecord> findByOperatorAndCustomerAndTimeBetweenOrderByTimeDesc(String operator,String customer, Date startDate, Date endDate, Pageable pageable);
    Page<CalculatorRecord> findByOperatorAndCustomerOrderByTimeDesc(String operator, String customer, Pageable pageable);
}
