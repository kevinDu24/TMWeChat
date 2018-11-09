package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/3.
 */
@Data
public class ApprovalAttachmentDto {

    @ItemColumn(keyPath = "#fingerprintImage", itemDateType = ItemDataTypeEnum.StringType)
    private String fingerprintImage;//按指纹照片

    @ItemColumn(keyPath = "#handHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String handHoldLetterOfCredit;//手持征信授权书

    @ItemColumn(keyPath = "#signImage", itemDateType = ItemDataTypeEnum.StringType)
    private String signImage; //签字照片

    @ItemColumn(keyPath = "#mateFingerprintImage", itemDateType = ItemDataTypeEnum.StringType)
    private String mateFingerprintImage;//配偶按指纹照片

    @ItemColumn(keyPath = "#mateHandHoldLetterOfCredit", itemDateType = ItemDataTypeEnum.StringType)
    private String mateHandHoldLetterOfCredit;//配偶手持征信授权书

    @ItemColumn(keyPath = "#mateSignImage", itemDateType = ItemDataTypeEnum.StringType)
    private String mateSignImage; //配偶签字照片

    @ItemColumn(keyPath = "#mateFrontImg", itemDateType = ItemDataTypeEnum.StringType)
    private String mateFrontImg;//配偶身份证正面

    @ItemColumn(keyPath = "#mateBehindImg", itemDateType = ItemDataTypeEnum.StringType)
    private String mateBehindImg; //配偶身份证反面

    //（新网中增加的 用户大头照） -- By ChengQiChuan 2018/10/19 22:58
    @ItemColumn(keyPath = "#userPhoto", itemDateType = ItemDataTypeEnum.StringType)
    private String userPhoto; //用户大头照


}
