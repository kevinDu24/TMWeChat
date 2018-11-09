package com.tm.wechat.dto.contract;

import lombok.Data;

/**
 * Created by LEO on 16/9/1.
 */
@Data
public class ContractRec {
    private String applyNum; //申请编号
    private String fpName; //fp名称
    private String startDate; //开始时间
    private String endDate; //结束时间
    private String state; //合同状态,扣款状态
    private Integer page; //页码
}
