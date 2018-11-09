package com.tm.wechat.dto.apply;

import lombok.Data;

/**
 * Created by pengchao on 2017/7/17.
 */
@Data
public class LeaseCalculationDto {

    private String productId;//细分产品id

    private String productName;//细分产品名称

    private String leaseType;//租赁类型(回租赁)

    private String applyType;//申请类型(个人)

    private String carType;//车辆类型

    private String mainType;//车辆新旧(新车、二手车)

    private String salePrice;//售价

    private String financeTerm;//融资期限

    private String paymentRatio;//首付比例

    private String tailPay;//尾付比例

    private String serviceRatio;//服务费率

    private String poundageRatio;//手续费率

    private String bondRatio;//保证金费率

    private String rentRatio;//租赁管理费率

    private String purchaseTax;//a2购置税

    private String installationFee;//a3加装费

    private String extendedWarrantyFee;//a4延保

    private String gpsFee;//a6 GPS硬件

    private String compulsoryInsurance ;//交强险合计

    private String commercialInsurance ;//商业险合计

    private String vehicleTax;//车船税合计

    private String xfws;//先锋卫士金额

    private String unexpected;//人身意外档位

    private String qingHongBao;// 清泓宝

    private String competitiveProducts;//精品

    private String xfwsFlag;//是否融先锋卫士(0/1)

    private String xfwsTerm;//先锋卫士期限

    private String productTypeName;//产品类型名称

    private String root; //来源

    private String gpsFeeFlag;//a6 是否GPS硬件  0:不融  1：融

}
