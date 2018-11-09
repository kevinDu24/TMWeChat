package com.tm.wechat.dao.sign;

import com.tm.wechat.domain.BankCardSign;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by pengchao on 2018/4/16.
 */
public interface BankCardSignRepository   extends JpaRepository<BankCardSign, String> {

    BankCardSign findByApplyNum(String applyNum);

    /**
     * 查询最后一条
     * @param applyNum
     * @return
     */
    BankCardSign findTopByApplyNum(String applyNum);

}
