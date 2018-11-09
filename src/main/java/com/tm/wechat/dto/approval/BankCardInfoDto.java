package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class BankCardInfoDto {

    @ItemColumn(keyPath = "#name", itemDateType = ItemDataTypeEnum.StringType)
    private String name;//借记卡户名

    @ItemColumn(keyPath = "#bank", itemDateType = ItemDataTypeEnum.StringType)
    private String bank;//借记卡开户行

    @ItemColumn(keyPath = "#accountNum", itemDateType = ItemDataTypeEnum.StringType)
    private String accountNum;//借记卡卡号

    @ItemColumn(keyPath = "#bankImg", itemDateType = ItemDataTypeEnum.StringType)
    private String bankImg;//银行卡图片

    @ItemColumn(keyPath = "#bankPhoneNum", itemDateType = ItemDataTypeEnum.StringType)
    private String bankPhoneNum;//银行卡预留手机号

    private String ip; //IP地址

//    @ItemColumn(keyPath = "#statement", itemDateType = ItemDataTypeEnum.StringType)
//    private String statement;//纳税声明

    @ItemColumn(keyPath = "#monthlyIncome", itemDateType = ItemDataTypeEnum.StringType)
    private String monthlyIncome;//月收入水平

    @ItemColumn(keyPath = "#appId", itemDateType = ItemDataTypeEnum.StringType)
    private String appId;//应用id
     

}
