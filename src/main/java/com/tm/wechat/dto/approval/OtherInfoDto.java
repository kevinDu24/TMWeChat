package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/10.
 */
@Data
public class OtherInfoDto {

    @ItemColumn(keyPath = "#homeNumber", itemDateType = ItemDataTypeEnum.StringType)
    private String homeNumber;//住宅电话

    @ItemColumn(keyPath = "#phoneNumber", itemDateType = ItemDataTypeEnum.StringType)
    private String phoneNumber;//手机号

    @ItemColumn(keyPath = "#vicePhoneNumber", itemDateType = ItemDataTypeEnum.StringType)
    private String vicePhoneNumber;//副手机号
}
