package com.tm.wechat.dao;

import com.tm.wechat.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by LEO on 16/9/8.
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByParentIdAndTypeAndPlanDateBetweenAndCustomer(Long parentId, Integer type, Date startDate, Date endDate, String customer);
    List<Budget> findByLevelIdAndTypeAndPlanDateBetweenAndCustomer(Long parentId, Integer type, Date startDate, Date endDate, String customer);
    List<Budget> findByParentIdAndPlanDateBetween(Long parentId, Date startDate, Date endDate);
}
