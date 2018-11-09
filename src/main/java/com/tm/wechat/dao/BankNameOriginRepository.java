package com.tm.wechat.dao;

import com.tm.wechat.domain.BankNameOrigin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LEO on 16/9/27.
 */
public interface BankNameOriginRepository extends JpaRepository<BankNameOrigin, Long>{

    List<BankNameOrigin> findByOriginType(String originType);
}
