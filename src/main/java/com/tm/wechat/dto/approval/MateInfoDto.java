package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/27.
 */
@Data
public class MateInfoDto {

    @ItemColumn(keyPath = "#mateName", itemDateType = ItemDataTypeEnum.StringType)
    private String mateName;//配偶姓名

    @ItemColumn(keyPath = "#mateMobile", itemDateType = ItemDataTypeEnum.StringType)
    private String mateMobile;//配偶手机

    @ItemColumn(keyPath = "#mateIdty", itemDateType = ItemDataTypeEnum.StringType)
    private String mateIdty;//配偶证件号码

//    @ItemColumn(keyPath = "#company", itemDateType = ItemDataTypeEnum.StringType)
//    private String company;//配偶工作单位
//
//    @ItemColumn(keyPath = "#address", itemDateType = ItemDataTypeEnum.StringType)
//    private String address;//配偶地址
}
