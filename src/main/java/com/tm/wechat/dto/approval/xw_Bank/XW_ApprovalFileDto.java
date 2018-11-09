package com.tm.wechat.dto.approval.xw_Bank;

import lombok.Data;


/**
 *  @author ChengQiChuan
 *  @date 2018/10/19 22:47
 *  Description: 新网预审批附件
 */

@Data
public class XW_ApprovalFileDto {
    //身份证正面照
    private String idCardFront;
    //身份证反面照
    private String idCardBack;
    //用户大头照
    private String userPhoto;

}
