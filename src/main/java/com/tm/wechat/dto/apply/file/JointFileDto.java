package com.tm.wechat.dto.apply.file;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/2/24.
 * 共申人附件信息
 */
@Data
public class JointFileDto {

    @ItemColumn(keyPath = "#jointIdCardFrontImg", itemDateType = ItemDataTypeEnum.StringType)
    private String jointIdCardFrontImg; //共申人身份证正面

    @ItemColumn(keyPath = "#jointIdCardBehindImg", itemDateType = ItemDataTypeEnum.StringType)
    private String jointIdCardBehindImg;//共申人身份证反面

    @ItemColumn(keyPath = "#jointHandHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String jointHandHoldLetterOfCredit;//共申人手持征信授权书

    @ItemColumn(keyPath = "#jointLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String jointLetterOfCredit;//共申人征信授权书
}
