package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.util.List;

/**
 * @program: TMWeChat
 * @description: 首保上传详情
 * @author: ChengQC
 * @create: 2018-09-21 11:18
 **/
@Data
public class FirstInformationDto {

    /**
     * 申请编号
     */
    private String applyNum;

    /**
     *  GPS方案
     */
    private String gpsScheme;

    /**
     *  车辆销售价
     */
    private String salesPrice;

    /**
     *  SN码
     */
    private String sn;

    /**
     *  登记日期
     */
    private String recordDate;

    /**
     *  车牌号
     */
    private String plateNumber;

    /**
     *  是否退回过   1 是  0 否
     */
    private String isReturn = "0";

    /**
     *  退回原因
     */
    private String returnReason;

    /**
     *  商业险保单信息是否提交  1 是  0 否
     */
    private String commerceIsCommit = "1";

    /**
     *  交强险保单信息是否提交  1 是  0 否
     */
    private String saliIsCommit = "1";

    /**
     * 是否所有必传文件都已上传  1 是  0 否
     */
    private String isAllFileUpload = "0";

    /**
     * 文件列表
     */
    private List<SingleFileDto> fileList;

    /**
     * 客户姓名
     */
    private String name;
}