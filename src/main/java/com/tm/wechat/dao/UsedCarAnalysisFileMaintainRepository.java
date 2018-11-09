package com.tm.wechat.dao;

import com.tm.wechat.domain.UsedCarAnalysisFileMaintain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by pengchao on 2018/7/23.
 */
public interface UsedCarAnalysisFileMaintainRepository extends JpaRepository<UsedCarAnalysisFileMaintain, String> {
    List<UsedCarAnalysisFileMaintain> findByIsMust(String isMust);
    UsedCarAnalysisFileMaintain findTop1ByFileName(String fileName);
    Page<UsedCarAnalysisFileMaintain> findByOrderByUpdateTimeDesc(Pageable pageable);
}
