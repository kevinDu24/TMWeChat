package com.tm.wechat.dto.requestpayment;

import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 主系统返回数据
 * @author: ChengQC
 * @create: 2018-09-27 12:05
 **/
@Data
public class CoreSystemResultDto {

    /**
     * 调用是否成功
     */
    Boolean success;

    /**
     * 信息
     */
    String msg;

    /**
     * 数据
     */
    Object obj;

}