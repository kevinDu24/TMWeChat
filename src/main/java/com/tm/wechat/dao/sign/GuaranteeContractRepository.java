package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.GuaranteeContract;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/3/19.
 */

public interface GuaranteeContractRepository  extends JpaRepository<GuaranteeContract, String> {

    GuaranteeContract findByApplyNum(String applyNum);
}
