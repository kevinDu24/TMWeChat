package com.tm.wechat.dto.calculator.yc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by HJYang on 2016/12/5.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculateReqDto {
    private Long carBrandId; //车辆品牌
    private Long financeKindId; //融资产品id
    private Double carPrice; //售价
    private Double gpsPrice; //gps费用
    private Double otherPrice; //其他费用(保险/人身意外保障/其他等)
    private Integer financePeriod; //融资期限
    private Double downPay; //首付比例
    private Integer payMode; //手续费支付方式(明白融特有),0为一次性支付,1为分期支付
}
