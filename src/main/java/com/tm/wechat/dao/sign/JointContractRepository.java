package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.JointContract;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/3/19.
 */
public interface JointContractRepository  extends JpaRepository<JointContract, String> {

    JointContract findByApplyNum(String applyNum);
}
