package com.tm.wechat.dao.applyOnline;

import com.tm.wechat.domain.originType.OriginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/24 12:47
 *  Description: 产品类型
 */

public interface OriginTypeRepository extends JpaRepository<OriginType, String> {

    List<OriginType> findByActive(String active);
}
