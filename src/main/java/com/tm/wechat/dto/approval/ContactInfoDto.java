package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/27.
 */
@Data
public class ContactInfoDto {

    @ItemColumn(keyPath = "#contact1Name", itemDateType = ItemDataTypeEnum.StringType)
    private String contact1Name;//联系人1姓名

    @ItemColumn(keyPath = "#contact1Mobile", itemDateType = ItemDataTypeEnum.StringType)
    private String contact1Mobile;//联系人1手机

    @ItemColumn(keyPath = "#contact1Relationship", itemDateType = ItemDataTypeEnum.StringType)
    private String contact1Relationship;//联系人1关系

    @ItemColumn(keyPath = "#contact2Name", itemDateType = ItemDataTypeEnum.StringType)
    private String contact2Name;//联系人2姓名

    @ItemColumn(keyPath = "#contact2Mobile", itemDateType = ItemDataTypeEnum.StringType)
    private String contact2Mobile;//联系人2手机

    @ItemColumn(keyPath = "#contact2Relationship", itemDateType = ItemDataTypeEnum.StringType)
    private String contact2Relationship;//联系人2关系

}
