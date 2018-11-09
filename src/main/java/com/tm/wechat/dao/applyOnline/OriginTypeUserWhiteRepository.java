package com.tm.wechat.dao.applyOnline;

import com.tm.wechat.domain.originType.OriginTypeUserWhite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/24 12:47
 *  Description: 产品类型白名单
 */

public interface OriginTypeUserWhiteRepository extends JpaRepository<OriginTypeUserWhite, String> {

    List<OriginTypeUserWhite> findByFpCode(String fpCode);
}
