package com.tm.wechat.dto.sale;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/6/9.
 */
@Data
public class SalePlanDto {
    private List<SalePlan> salePlans;
    private Long levelId;
    private String area;
    private Long planCount; //合同量
    private Long realCount; //申请量

}
