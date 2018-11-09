package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.util.List;

/**
 * @program: TMWeChat
 * @description: 提交合同请款
 * @author: ChengQC
 * @create: 2018-10-08 14:31
 **/
@Data
public class SubmitRequestPaymentInfoDto {

    /**
     * 申请编号
     */
    private String applyNum;

    /**
     * 项目列表
     */
    private List<SingleItemDto> itemListJsonStr;

    /**
     * 经销商姓名
     */
    private String name;

}