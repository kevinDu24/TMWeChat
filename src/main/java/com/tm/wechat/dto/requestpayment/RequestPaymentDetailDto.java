package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.util.List;

/**
 *  合同请款详细信息
 *  2018/9/17 10:05 By ChengQiChuan
 */
@Data
public class RequestPaymentDetailDto {

    /**
     * 申请编号
     */
    private String applyNum;

    /**
     * 需要填写的项目列表
     */
    private List<SingleItemDto> ItemList;

    /**
     * 是否被退回过  1 是  0 否
     */
    private String isReturn;

    /**
     * 退回原因
     */
    private String returnReason;

    /**
     * 是否所有必传文件都已上传  1 是  0 否
     */
    private String isAllFileUpload = "0";

    /**
     * 文件列表
     */
    private List<SingleFileDto> singleFileDtos;
}
