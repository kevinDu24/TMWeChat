package com.tm.wechat.dao;

import com.tm.wechat.domain.TmSysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LEO on 16/12/3.
 */
public interface SysRoleRepository extends JpaRepository<TmSysRole, Long> {
    List<TmSysRole> findByUserNameAndCustomer(String userName, String customer);
}
