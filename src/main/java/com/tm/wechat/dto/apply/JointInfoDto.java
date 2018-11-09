package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * 共申人信息
 * Created by pengchao on 2017/7/11.
 */
@Data
public class JointInfoDto {

    @ItemColumn(keyPath = "#jointName", itemDateType = ItemDataTypeEnum.StringType)
    private String jointName;//共申人姓名

    @ItemColumn(keyPath = "#jointMobile", itemDateType = ItemDataTypeEnum.StringType)
    private String jointMobile;//共申人手机

    @ItemColumn(keyPath = "#jointIdType", itemDateType = ItemDataTypeEnum.StringType)
    private String jointIdType;//共申人证件类型

    @ItemColumn(keyPath = "#jointIdty", itemDateType = ItemDataTypeEnum.StringType)
    private String jointIdty;//证件号码

    @ItemColumn(keyPath = "#jointRelationship", itemDateType = ItemDataTypeEnum.StringType)
    private String jointRelationship;//与承租人关系

}
