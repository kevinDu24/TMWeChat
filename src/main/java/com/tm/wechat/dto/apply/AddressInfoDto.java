package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/9/21.
 */
@Data
public class AddressInfoDto {

    @ItemColumn(keyPath = "#livingState", itemDateType = ItemDataTypeEnum.StringType)
    private String livingState;//居住状况

    @ItemColumn(keyPath = "#propertyArea", itemDateType = ItemDataTypeEnum.StringType)
    private String propertyArea;//房产区域

    @ItemColumn(keyPath = "#propertyPledgeState", itemDateType = ItemDataTypeEnum.StringType)
    private String propertyPledgeState;//房产抵押状况

    @ItemColumn(keyPath = "#propertyAcreage", itemDateType = ItemDataTypeEnum.StringType)
    private String propertyAcreage;//房产面积

    @ItemColumn(keyPath = "#householdRegistrationProv", itemDateType = ItemDataTypeEnum.StringType)
    private String householdRegistrationProv;//户籍所在省份

    @ItemColumn(keyPath = "#householdRegistrationCity", itemDateType = ItemDataTypeEnum.StringType)
    private String householdRegistrationCity;//户籍所在城市

    @ItemColumn(keyPath = "#actualAddress", itemDateType = ItemDataTypeEnum.StringType)
    private String actualAddress;//实际居住地址

    @ItemColumn(keyPath = "#actualAddressProv", itemDateType = ItemDataTypeEnum.StringType)
    private String actualAddressProv;//实际居住地址省份

    @ItemColumn(keyPath = "#actualAddressCity", itemDateType = ItemDataTypeEnum.StringType)
    private String actualAddressCity;//实际居住地址城市

}
