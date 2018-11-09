package com.tm.wechat.dao;

import com.tm.wechat.domain.SysUserFunction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2018/4/23.
 */
public interface SysUserFunctionRepository extends JpaRepository<SysUserFunction, Long> {

    List<SysUserFunction> findByXtczdm(String userName);

    List<SysUserFunction> findByFunctionId(Long functionId);

    void deleteByXtczdm(String userName);

    SysUserFunction findTop1ByXtczdmAndFunctionIdOrderByUpdateTimeDesc(String userName, Long functionId);

}
