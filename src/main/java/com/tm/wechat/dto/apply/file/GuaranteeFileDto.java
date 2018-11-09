package com.tm.wechat.dto.apply.file;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/3/29.
 */
@Data
public class GuaranteeFileDto {
    
    @ItemColumn(keyPath = "#guaranteeIdCardFrontImg", itemDateType = ItemDataTypeEnum.StringType)
    private String guaranteeIdCardFrontImg; //担保人身份证正面

    @ItemColumn(keyPath = "#guaranteeIdCardBehindImg", itemDateType = ItemDataTypeEnum.StringType)
    private String guaranteeIdCardBehindImg;//担保人身份证反面

    @ItemColumn(keyPath = "#guaranteeHandHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String guaranteeHandHoldLetterOfCredit;//担保人手持征信授权书

    @ItemColumn(keyPath = "#guaranteeLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String guaranteeLetterOfCredit;//担保人征信授权书
}
