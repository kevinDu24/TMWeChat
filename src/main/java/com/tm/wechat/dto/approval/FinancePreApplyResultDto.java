package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class FinancePreApplyResultDto {

    private String userid;//经销商编号(用户名)

    private String applyId;//申请编号

    private String applyIdOfMaster;//主系统申请编号

    private String applyResult;//申请状态

    private String applyResultReason;//申请结果原因
}
