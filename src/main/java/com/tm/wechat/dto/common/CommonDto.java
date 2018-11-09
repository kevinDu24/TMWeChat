package com.tm.wechat.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonDto {
    private Res result;
    private Object bam068;
    private Object repaymentBankList;
    private Object gpsList;
    private Object spdycsList;
    private Object fpScList;
    private Object productPlanInfo;
    private Object vehicleInfoList;
    private Object vehicleInfo;
    private Object insuranceInfo;
    private Object getNextAreaList;
    private Object tjYuQiLvByTimeList;
    private Object tjYuQiLvByAreaList;
    private Object getQueryYbcpList;
    private Object getYbfyList;
    private Object queryCpcxList;
    private List shenqinliang;
}
