package com.tm.wechat.dao;

import com.tm.wechat.domain.WzApplyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by pengchao on 2018/1/8.
 */
public interface WzApplyInfoRepository extends JpaRepository<WzApplyInfo, String> {
    WzApplyInfo findByApplyNum(String applyNum);


    WzApplyInfo findTop1ByUniqueMarkOrderByCreateTimeDesc(String uniqueMark);

    /**
     * 根据身份证 ，状态 ，创建时间查找微众预审批申请
     * @param idCard        身份证
     * @param status        状态
     * @param createTime    创建时间
     * @return
     */
    WzApplyInfo findTop1ByIdCardAndStatusAndCreateTimeAfterOrderByCreateTimeDesc(String idCard,String status,Date createTime);

}
