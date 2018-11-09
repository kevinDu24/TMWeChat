package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class CarPledgeDto {

    @ItemColumn(keyPath = "#saler", itemDateType = ItemDataTypeEnum.StringType)
    private String saler;//车辆经销商名称

    @ItemColumn(keyPath = "#licenseAttribute", itemDateType = ItemDataTypeEnum.StringType)
    private String licenseAttribute;//牌照属性

    @ItemColumn(keyPath = "#pledgeCity", itemDateType = ItemDataTypeEnum.StringType)
    private String pledgeCity;//回租赁抵押城市

    @ItemColumn(keyPath = "#pledgeCompany", itemDateType = ItemDataTypeEnum.StringType)
    private String pledgeCompany;//正租赁上牌&回租赁抵押公司
}
