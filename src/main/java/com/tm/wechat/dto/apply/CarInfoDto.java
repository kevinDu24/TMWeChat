package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class CarInfoDto {

    @ItemColumn(keyPath = "#mfrs", itemDateType = ItemDataTypeEnum.StringType)
    private String mfrs;//制造商

    @ItemColumn(keyPath = "#brand", itemDateType = ItemDataTypeEnum.StringType)
    private String brand;//品牌

    @ItemColumn(keyPath = "#type", itemDateType = ItemDataTypeEnum.StringType)
    private String type;//车型

    @ItemColumn(keyPath = "#typeId", itemDateType = ItemDataTypeEnum.StringType)
    private String typeId;//车型id

    @ItemColumn(keyPath = "#officialPrice", itemDateType = ItemDataTypeEnum.StringType)
    private String officialPrice;//指导价

    @ItemColumn(keyPath = "#salePrice", itemDateType = ItemDataTypeEnum.StringType)
    private String salePrice;//销售价格

    @ItemColumn(keyPath = "#carPurchaseTax", itemDateType = ItemDataTypeEnum.StringType)
    private String carPurchaseTax;//购置税

    @ItemColumn(keyPath = "#secondOfficialPrice", itemDateType = ItemDataTypeEnum.StringType)
    private String secondOfficialPrice;//二手车评估价

    @ItemColumn(keyPath = "#secondDate", itemDateType = ItemDataTypeEnum.StringType)
    private String secondDate;//二手车出场日期

    @ItemColumn(keyPath = "#secondYears", itemDateType = ItemDataTypeEnum.StringType)
    private String secondYears;//二手车车龄

    @ItemColumn(keyPath = "#secondDistance", itemDateType = ItemDataTypeEnum.StringType)
    private String secondDistance;//二手车公里数

    @ItemColumn(keyPath = "#displacement", itemDateType = ItemDataTypeEnum.StringType)
    private String displacement;//排量

    @ItemColumn(keyPath = "#seatNumber", itemDateType = ItemDataTypeEnum.StringType)
    private String seatNumber;//座位数量
}
