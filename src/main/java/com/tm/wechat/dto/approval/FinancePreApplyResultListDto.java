package com.tm.wechat.dto.approval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinancePreApplyResultListDto {
    private Res result;
    private FinancePreApplyResultDto applyInfo;
    private List<Map> lr;
    private List<FinancePreApplyResultDto> resultList;
}
