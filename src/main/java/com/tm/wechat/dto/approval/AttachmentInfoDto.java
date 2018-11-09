package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/18.
 */
@Data
public class AttachmentInfoDto {
    @ItemColumn(keyPath = "#url", itemDateType = ItemDataTypeEnum.StringType)
    private String url;
}
