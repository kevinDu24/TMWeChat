package com.tm.wechat.dao;

import com.tm.wechat.domain.TmSysResource;
import com.tm.wechat.domain.TmSysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by LEO on 16/12/3.
 */
public interface SysResourceRepository extends JpaRepository<TmSysResource, Long>{
    List<TmSysResource> findDistinctByTmSysRolesIn(List<TmSysRole> tmSysRoles);
    TmSysResource findByName(String name);
}
