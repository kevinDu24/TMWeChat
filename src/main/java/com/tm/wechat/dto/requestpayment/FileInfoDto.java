package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.util.List;


/**
 * Created by pengchao on 2018/3/23.
 */
@Data
public class FileInfoDto {
//
//    private String bankCardImg; //银行卡照片
//
//    private String invoice; // 发票照片
//
//    private String invoiceQRCodeImg; // 发票二维码
//
//    private String cardListImg; //工行卡单
//
//    private String cardListSignImg; //工行卡单面签
//
//    private String bankDetailedImg; //工行细则
//
//    private String bankDetailedSignImg; //工行细则面签
//
//    private String paymentAgreementImg; //工行代扣协议

    private String applyNum; //申请编号

    private List<SingleFileDto> fileList; //文件信息附件集合

    /**
     * 车辆发票号
     */
    private String invoiceNum;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 纳税人识别号
     */
    private String taxpayerID;

    /**
     * 车辆发票电话
     */
    private String invoiceTel;

    /**
     * 请款备注
     */
    private String remark;

}
