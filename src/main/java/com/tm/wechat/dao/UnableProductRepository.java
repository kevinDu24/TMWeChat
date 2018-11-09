package com.tm.wechat.dao;

import com.tm.wechat.domain.UnableProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2018/5/2.
 */
public interface UnableProductRepository  extends JpaRepository<UnableProduct, String> {

    List<UnableProduct> findByXtczdm(String userName);

    void deleteByXtczdm(String userName);
}
