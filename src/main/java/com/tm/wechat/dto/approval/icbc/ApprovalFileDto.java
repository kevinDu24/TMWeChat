package com.tm.wechat.dto.approval.icbc;

import lombok.Data;

/**
 * 工行预审批附件列表
 * Created by pengchao on 2018/7/11.
 */
@Data
public class ApprovalFileDto {
    
//    private String type; //主贷人或配偶
//
//    private String name; //身份证正面照片、反面...
//
//    private String url; //url

    private String idCardFrontImg;//正面身份证照片
    private String idCardBehindImg; //反面照片
    private String fingerprintImage;//按指纹照片
    private String handHoldLetterOfCredit;//手持征信授权书
    private String signImage; //签字照片

}
