package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class ProductPlanDto {

    @ItemColumn(keyPath = "#mainType", itemDateType = ItemDataTypeEnum.StringType)
    private String mainType;//新车/二手车

    @ItemColumn(keyPath = "#productTypeId", itemDateType = ItemDataTypeEnum.StringType)
    private String productTypeId;//产品类型id

    @ItemColumn(keyPath = "#productTypeName", itemDateType = ItemDataTypeEnum.StringType)
    private String productTypeName;//产品类型名称

    @ItemColumn(keyPath = "#root", itemDateType = ItemDataTypeEnum.StringType)
    private String root;//来源

    @ItemColumn(keyPath = "#type", itemDateType = ItemDataTypeEnum.StringType)
    private String type;//车辆类型

    @ItemColumn(keyPath = "#specificTypeId", itemDateType = ItemDataTypeEnum.StringType)
    private String specificTypeId;//细分产品id

    @ItemColumn(keyPath = "#specificTypeName", itemDateType = ItemDataTypeEnum.StringType)
    private String specificTypeName;//细分产品名称
}
