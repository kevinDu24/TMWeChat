package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/7/18.
 */
@Data
public class UsedCarImagesDto {

    @ItemColumn(keyPath = "#imageClass", itemDateType = ItemDataTypeEnum.StringType)
    private String imageClass;//二手车图片分类

    @ItemColumn(keyPath = "#imageSeqNum", itemDateType = ItemDataTypeEnum.StringType)
    private String imageSeqNum;//二手车图片序号

    @ItemColumn(keyPath = "#imageUrl", itemDateType = ItemDataTypeEnum.StringType)
    private String imageUrl;//二手车图片url

}
