package com.tm.wechat.dto.apply.file;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/3/2.
 * 配偶附件
 */
@Data
public class MateFileDto {
    @ItemColumn(keyPath = "#mateIdCardFrontImg", itemDateType = ItemDataTypeEnum.StringType)
    private String mateIdCardFrontImg; //配偶身份证正面

    @ItemColumn(keyPath = "#mateIdCardBehindImg", itemDateType = ItemDataTypeEnum.StringType)
    private String mateIdCardBehindImg;//配偶身份证反面

    @ItemColumn(keyPath = "#mateHandHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String mateHandHoldLetterOfCredit;//配偶手持征信授权书

    @ItemColumn(keyPath = "#mateLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String mateLetterOfCredit;//配偶征信授权书
}
