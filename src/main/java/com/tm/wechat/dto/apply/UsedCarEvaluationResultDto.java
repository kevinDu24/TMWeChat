package com.tm.wechat.dto.apply;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/7/13.
 */
@Data
public class UsedCarEvaluationResultDto {

    @ItemColumn(keyPath = "#carBillId", itemDateType = ItemDataTypeEnum.StringType)
    private String carBillId;//评估单号

    @ItemColumn(keyPath = "#price", itemDateType = ItemDataTypeEnum.StringType)
    private String price;//评估价格

    @ItemColumn(keyPath = "#years", itemDateType = ItemDataTypeEnum.StringType)
    private String years;//车龄

    @ItemColumn(keyPath = "#dateOfProduction", itemDateType = ItemDataTypeEnum.StringType)
    private String dateOfProduction;//出厂日期

    @ItemColumn(keyPath = "#resultReason", itemDateType = ItemDataTypeEnum.StringType)
    private String resultReason;//原因
}
