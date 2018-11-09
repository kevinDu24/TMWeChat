package com.tm.wechat.dto.apply;

import lombok.Data;

/**
 * Created by pengchao on 2017/7/19.
 */
@Data
public class ApplyParamDto {

    private String mainType;//新车/二手车

    private String carType;//车辆类型(乘用车、皮卡)

    private String specificTypeId;//细分产品id

    private String specificTypeName;//细分产品名称

    private String salePrice;//销售价格

    private String leaseType;//牌照属性

    private String guidancePrices;//车辆指导价

    private String usedCarGuidancePrices;//二手车评估价

    private String displacement;//排量

    private String seatNumber;//座位数量

    private String referenceCarPurchaseTax;//购置税

    private String productTypeName; //产品类型名称

    private String root; //来源（HPL、先锋特惠）

}
