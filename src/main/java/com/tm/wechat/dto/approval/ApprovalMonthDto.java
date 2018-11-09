package com.tm.wechat.dto.approval;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class ApprovalMonthDto {

    private JSONArray monthArray; //月份数组

    private JSONArray passArray; //申请通过数组（预审批/在线申请）

    private JSONArray refuseArray; //申请拒绝数组（预审批/在线申请）

    private JSONArray returnArray; //退回待修改数组（在线申请）
}
