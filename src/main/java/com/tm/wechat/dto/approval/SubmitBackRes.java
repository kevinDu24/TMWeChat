package com.tm.wechat.dto.approval;

import com.tm.wechat.dto.result.Res;
import lombok.Data;

/**
 * Created by zcHu on 2016/5/16.
 */
@Data
public class SubmitBackRes {
    private Res result;
    private String userid;
    private String applyId;
    private String applyIdOfMaster;
    private String applyResult;
    private String applyResultReason;
}
