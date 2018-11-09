package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/7/11.
 */
@Data
public class UsedCarEvaluationDto {

    @ItemColumn(keyPath = "#carBillId", itemDateType = ItemDataTypeEnum.StringType)
    private String carBillId;//评估单号

    @ItemColumn(keyPath = "#mark", itemDateType = ItemDataTypeEnum.StringType)
    private String mark;//备注

    @ItemColumn(keyPath = "#images", itemDateType = ItemDataTypeEnum.ListType, listInnerClassType = UsedCarImagesDto.class )
    private List<UsedCarImagesDto> images;

}
