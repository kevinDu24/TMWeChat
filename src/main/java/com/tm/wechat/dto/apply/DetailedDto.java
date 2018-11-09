package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/7/11.
 */
@Data
public class DetailedDto {

    @ItemColumn(keyPath = "#diploma", itemDateType = ItemDataTypeEnum.StringType)
    private String diploma;//学历

    @ItemColumn(keyPath = "#marriage", itemDateType = ItemDataTypeEnum.StringType)
    private String marriage;//婚姻状况

    @ItemColumn(keyPath = "#houseOwnership", itemDateType = ItemDataTypeEnum.StringType)
    private String houseOwnership;//有无房产

    @ItemColumn(keyPath = "#realDriverName", itemDateType = ItemDataTypeEnum.StringType)
    private String realDriverName;//实际用车人

    @ItemColumn(keyPath = "#realDriverMobile", itemDateType = ItemDataTypeEnum.StringType)
    private String realDriverMobile;//实际用车人手机

    @ItemColumn(keyPath = "#realDrivingProv", itemDateType = ItemDataTypeEnum.StringType)
    private String realDrivingProv;//车辆使用省份

    @ItemColumn(keyPath = "#realDrivingCity", itemDateType = ItemDataTypeEnum.StringType)
    private String realDrivingCity;//车辆使用城市

    @ItemColumn(keyPath = "#accountType", itemDateType = ItemDataTypeEnum.StringType)
    private String accountType;//户籍类型

//    @ItemColumn(keyPath = "#actualAddress", itemDateType = ItemDataTypeEnum.StringType)
//    private String actualAddress;//实际居住地址

    @ItemColumn(keyPath = "#letterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String letterOfCredit;//征信授权书

    @ItemColumn(keyPath = "#handHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String handHoldLetterOfCredit;//手持征信授权书
}
