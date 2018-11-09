package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class DriveLicenceInfoDto {

    @ItemColumn(keyPath = "#name", itemDateType = ItemDataTypeEnum.StringType)
    private String name;//驾驶证姓名

    @ItemColumn(keyPath = "#sex", itemDateType = ItemDataTypeEnum.StringType)
    private String sex;//性别

    @ItemColumn(keyPath = "#nationality", itemDateType = ItemDataTypeEnum.StringType)
    private String nationality;//国籍

    @ItemColumn(keyPath = "#dateOfBirth", itemDateType = ItemDataTypeEnum.StringType)
    private String dateOfBirth;//出生日期

    @ItemColumn(keyPath = "#firstIssueDate", itemDateType = ItemDataTypeEnum.StringType)
    private String firstIssueDate; //初次领证日期

    @ItemColumn(keyPath = "#address", itemDateType = ItemDataTypeEnum.StringType)
    private String address;//地址

    @ItemColumn(keyPath = "#quasiDriveType", itemDateType = ItemDataTypeEnum.StringType)
    private String quasiDriveType;//准驾车型

    @ItemColumn(keyPath = "#licenceNum", itemDateType = ItemDataTypeEnum.StringType)
    private String licenceNum;//证件编号

    @ItemColumn(keyPath = "#effectiveTerm", itemDateType = ItemDataTypeEnum.StringType)
    private String effectiveTerm;//有效期限

    @ItemColumn(keyPath = "#licenceImg", itemDateType = ItemDataTypeEnum.StringType)
    private String licenceImg;//证件图片
}
