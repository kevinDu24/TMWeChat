package com.tm.wechat.dto.approval.xw_Bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.approval.FinancePreApplyResultDto;
import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/22 10:57
 *  Description: 查询主系统新网审批结果返回列表
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XW_FinancePreApplyResultListDto {
    private Res result;
    private XW_FinancePreApplyResultDto applyInfo;
    private List<Map> lr;
    private List<XW_FinancePreApplyResultDto> resultList;
}
