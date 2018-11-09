package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/5/23.
 */
@Data
public class MaritalStatusDto {

    @ItemColumn(keyPath = "#maritalStatus", itemDateType = ItemDataTypeEnum.StringType)
    private String maritalStatus;//婚姻状况
}
