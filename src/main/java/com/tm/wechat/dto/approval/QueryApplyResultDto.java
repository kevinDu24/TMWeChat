package com.tm.wechat.dto.approval;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/8/8.
 */
@Data
public class QueryApplyResultDto {

    private List<ApplyIdOfMasterDto> applyIdOfMasterList;
}
