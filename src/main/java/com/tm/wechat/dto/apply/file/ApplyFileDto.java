package com.tm.wechat.dto.apply.file;

import com.tm.wechat.dto.apply.ApplyDto;
import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/2/24.
 */
@Data
public class ApplyFileDto {

    @ItemColumn(keyPath = "#letterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String letterOfCredit; //征信照片

    @ItemColumn(keyPath = "#handHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String handHoldLetterOfCredit;//手持征信

    @ItemColumn(keyPath = "#applyForm", itemDateType = ItemDataTypeEnum.StringType)
    private String applyForm;//申请表照片

    @ItemColumn(keyPath = "#frontImg", itemDateType = ItemDataTypeEnum.StringType)
    private String frontImg; //身份证正面图

    @ItemColumn(keyPath = "#behindImg", itemDateType = ItemDataTypeEnum.StringType)
    private String behindImg;//身份证反面图

    @ItemColumn(keyPath = "#driveLicenceImg", itemDateType = ItemDataTypeEnum.StringType)
    private String driveLicenceImg;//驾驶证件图片

    @ItemColumn(keyPath = "#bankImg", itemDateType = ItemDataTypeEnum.StringType)
    private String bankImg;//银行卡图片

}
