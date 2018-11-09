package com.tm.wechat.dto.approval.xw_Bank;

import lombok.Data;



/**
 *  @author ChengQiChuan
 *  @date 2018/10/19 22:43
 *  Description: 新盟贷（新网银行）预审批提交信息
 */
@Data
public class XW_ApprovalSubmitDto {
    //申请人姓名
    private String basqxm;
    //申请编号
    private String applyid;
    //身份证号码
    private String bazjhm;
    //手机号码
    private String basjhm;
    //银行卡号
    private String bayhkh;
    //申请人ip地址
    private String xwsqip;
    //新网需要提交的附件信息
    private XW_ApprovalFileDto files;

}
