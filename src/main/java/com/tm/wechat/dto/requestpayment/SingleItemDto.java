package com.tm.wechat.dto.requestpayment;

import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 请款详情信息项目，合同请款中需要填写的内容，为系统动态生成。
 * @author: ChengQC
 * @create: 2018-09-20 17:31
 **/
@Data
public class SingleItemDto {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目内容
     */
    private String value;

    /**
     * 是否必填  1 是  0 否
     */
    private String required;

}