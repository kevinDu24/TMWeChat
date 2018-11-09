package com.tm.wechat.dto.sale;

import lombok.Data;

/**
 * Created by LEO on 16/9/8.
 */
@Data
public class SalePlan {
    private Long levelId;
    private String area;
    private Long planCount; //合同量
    private Long realCount; //申请量
}
