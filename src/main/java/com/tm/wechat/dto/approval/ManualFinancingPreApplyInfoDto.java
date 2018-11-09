package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2017/6/1.
 */
@Data
public class ManualFinancingPreApplyInfoDto {
    private String userid;//经销商账号
    private String applyId;//申请编号
    private FinancingPreApplyDto financingPreApplyDto;// 申请信息
}
