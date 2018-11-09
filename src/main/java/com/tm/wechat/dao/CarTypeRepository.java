package com.tm.wechat.dao;

import com.tm.wechat.domain.CarType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by LEO on 16/9/13.
 */
public interface CarTypeRepository extends JpaRepository<CarType, Long>{
}
