package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class IdCardInfoDto {

    @ItemColumn(keyPath = "#name", itemDateType = ItemDataTypeEnum.StringType)
    private String name;//姓名

    @ItemColumn(keyPath = "#sex", itemDateType = ItemDataTypeEnum.StringType)
    private String sex;//性别

    @ItemColumn(keyPath = "#dateOfBirth", itemDateType = ItemDataTypeEnum.StringType)
    private String dateOfBirth;//出生日期

    @ItemColumn(keyPath = "#nation", itemDateType = ItemDataTypeEnum.StringType)
    private String nation;//民族

    @ItemColumn(keyPath = "#address", itemDateType = ItemDataTypeEnum.StringType)
    private String address;//地址

    @ItemColumn(keyPath = "#idCardNum", itemDateType = ItemDataTypeEnum.StringType)
    private String idCardNum;//身份证号码

    @ItemColumn(keyPath = "#effectiveTerm", itemDateType = ItemDataTypeEnum.StringType)
    private String effectiveTerm;//有效期限

    @ItemColumn(keyPath = "#frontImg", itemDateType = ItemDataTypeEnum.StringType)
    private String frontImg; //正面图

    @ItemColumn(keyPath = "#behindImg", itemDateType = ItemDataTypeEnum.StringType)
    private String behindImg;//反面图

    @ItemColumn(keyPath = "#issuingAuthority", itemDateType = ItemDataTypeEnum.StringType)
    private String issuingAuthority;//签发机构

    @ItemColumn(keyPath = "#productSource", itemDateType = ItemDataTypeEnum.StringType)
    private String productSource;//产品来源
}
