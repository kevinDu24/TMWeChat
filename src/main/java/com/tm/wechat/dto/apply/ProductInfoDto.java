package com.tm.wechat.dto.apply;

import lombok.Data;

/**
 * Created by pengchao on 2017/7/17.
 */
@Data
public class ProductInfoDto {

    private String productId;//产品id

    private String productName;//产品名称

    private String leaseType;//租赁类型(回租赁)

    private String applyType;//申请类型(个人)

    private String carType;//车辆类型

    private String mainType;//车辆新旧(新车、二手车)

    private String salePrice;//售价

}
