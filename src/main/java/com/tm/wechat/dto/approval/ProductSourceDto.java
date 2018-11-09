package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/6/30.
 */
@Data
public class ProductSourceDto {
    @ItemColumn(keyPath = "#productSource", itemDateType = ItemDataTypeEnum.StringType)
    private String productSource;//产品来源
}
