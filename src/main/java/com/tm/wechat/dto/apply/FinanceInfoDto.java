package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class FinanceInfoDto {

    @ItemColumn(keyPath = "#paymentRatio", itemDateType = ItemDataTypeEnum.StringType)
    private String paymentRatio;//首付比例

    @ItemColumn(keyPath = "#paymentAmount", itemDateType = ItemDataTypeEnum.StringType)
    private String paymentAmount;//首付金额

    @ItemColumn(keyPath = "#serviceRatio", itemDateType = ItemDataTypeEnum.StringType)
    private String serviceRatio;//服务费率

    @ItemColumn(keyPath = "#serviceAmount", itemDateType = ItemDataTypeEnum.StringType)
    private String serviceAmount;//服务金额

    @ItemColumn(keyPath = "#poundageRatio", itemDateType = ItemDataTypeEnum.StringType)
    private String poundageRatio;//手续费率

    @ItemColumn(keyPath = "#poundageAmount", itemDateType = ItemDataTypeEnum.StringType)
    private String poundageAmount;//手续费金额

    @ItemColumn(keyPath = "#bondRatio", itemDateType = ItemDataTypeEnum.StringType)
    private String bondRatio;//保证金费率

    @ItemColumn(keyPath = "#bondAmount", itemDateType = ItemDataTypeEnum.StringType)
    private String bondAmount;//保证金金额

    @ItemColumn(keyPath = "#rentRatio", itemDateType = ItemDataTypeEnum.StringType)
    private String rentRatio;//租赁管理费率

    @ItemColumn(keyPath = "#rentAmount", itemDateType = ItemDataTypeEnum.StringType)
    private String rentAmount;//租赁管理费用

    @ItemColumn(keyPath = "#financeAmount", itemDateType = ItemDataTypeEnum.StringType)
    private String financeAmount;//融资金额

    @ItemColumn(keyPath = "#financeTerm", itemDateType = ItemDataTypeEnum.StringType)
    private String financeTerm;//融资期限

    @ItemColumn(keyPath = "#totalInvestment", itemDateType = ItemDataTypeEnum.StringType)
    private String totalInvestment;//投资总额

    @ItemColumn(keyPath = "#purchaseTaxFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String purchaseTaxFlag;//是否融购置税（0/1）

    @ItemColumn(keyPath = "#purchaseTax", itemDateType = ItemDataTypeEnum.StringType)
    private String purchaseTax;//购置税

    @ItemColumn(keyPath = "#insuranceFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String insuranceFlag;//是否融保险（0/1）

    @ItemColumn(keyPath = "#compulsoryInsurance ", itemDateType = ItemDataTypeEnum.StringType)
    private String compulsoryInsurance ;//交强险

    @ItemColumn(keyPath = "#commercialInsurance", itemDateType = ItemDataTypeEnum.StringType)
    private String commercialInsurance ;//商业险

    @ItemColumn(keyPath = "#vehicleTax", itemDateType = ItemDataTypeEnum.StringType)
    private String vehicleTax;//车船税

    @ItemColumn(keyPath = "#gpsFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String gpsFlag;//是否融GPS（0/1）

    @ItemColumn(keyPath = "#gps", itemDateType = ItemDataTypeEnum.StringType)
    private String gps;//gps价格

    @ItemColumn(keyPath = "#xfwsFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String xfwsFlag;//是否融先锋卫士（0/1）

    @ItemColumn(keyPath = "#xfws", itemDateType = ItemDataTypeEnum.StringType)
    private String xfws;//先锋卫士金额

    @ItemColumn(keyPath = "#xfwsTerm", itemDateType = ItemDataTypeEnum.StringType)
    private String xfwsTerm;//先锋卫士期限

    @ItemColumn(keyPath = "#unexpectedGear", itemDateType = ItemDataTypeEnum.StringType)
    private String unexpectedGear;//人身意外档位

    @ItemColumn(keyPath = "#unexpectedFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String unexpectedFlag;//是否融人身意外（0/1）

    @ItemColumn(keyPath = "#unexpected", itemDateType = ItemDataTypeEnum.StringType)
    private String unexpected;//人身意外金额


    //新增融资信息参数
    @ItemColumn(keyPath = "#poundageRatioMonthlyFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String poundageRatioMonthlyFlag;//服务率是否月结

    @ItemColumn(keyPath = "#gpsMonthlyFlag", itemDateType = ItemDataTypeEnum.StringType)
    private String gpsMonthlyFlag;//gps是否月结

    @ItemColumn(keyPath = "#limitOfLiabilityInsurance", itemDateType = ItemDataTypeEnum.StringType)
    private String limitOfLiabilityInsurance;//第三方责任险限额

}
