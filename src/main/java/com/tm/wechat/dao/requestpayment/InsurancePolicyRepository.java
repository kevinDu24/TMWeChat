package com.tm.wechat.dao.requestpayment;

import com.tm.wechat.domain.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2018/3/22.
 */
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, String> {

    List<InsurancePolicy> findByApplyNum(String applyNum);
    InsurancePolicy findTop1ByApplyNumAndName(String applyNum, String name);
}
