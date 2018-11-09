package com.tm.wechat.dao;

import com.tm.wechat.domain.ApplyInfoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/29 17:57
 *  Description: 预审批状态
 */
public interface ApplyInfoStatusRepository extends JpaRepository<ApplyInfoStatus, String>{

    /**
     * 根据 uniqueMark 查询有效数据
     * @param uniqueMark
     * @param isDelete
     * @return
     */
    List<ApplyInfoStatus> findByUniqueMarkAndIsDelete(String uniqueMark,String isDelete);

    /**
     * 根据 uniqueMark 和 itemKey 查询有效的数据
     * @param uniqueMark
     * @param itemKey
     * @param isDelete
     * @return
     */
    List<ApplyInfoStatus> findByUniqueMarkAndItemKeyAndIsDelete(String uniqueMark,String itemKey,String isDelete);

     /**
     * 根据 uniqueMark 和 itemKey 查询有效的数据
     * @param uniqueMark
     * @param itemKey
     * @param isDelete
     * @return
     */
    ApplyInfoStatus findTopByUniqueMarkAndItemKeyAndIsDelete(String uniqueMark,String itemKey,String isDelete);

}
