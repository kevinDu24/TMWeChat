package com.tm.wechat.dto.calculator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/13.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculatorRecDto {
    private Long carTypeId; // 车型id
    private Long financeProductId; // 融资产品id
    private Double sellingPrice; // 售价
    private Double gpsPrice; // gps费用
    private Double otherPrice; // 其他费用(保险/人身意外保障/其他等)
    private Integer financePeriod; // 融资期限
    private Double downPay; // 首付比例
    private Integer payMode; // 手续费支付方式(明白融特有),0为一次性支付,1为分期支付

    // 保存历史记录需要的字段
    private String carTypeName; // 车型名称
    private String financeProductName; // 融资产品名称
}
