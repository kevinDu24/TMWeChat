package com.tm.wechat.dto.approval.icbc;

import lombok.Data;


/**
 * 工行预审批信息提交
 * Created by pengchao on 2018/7/11.
 */
@Data
public class ApprovalSubmitDto {

    private String userid;//经销商编号(用户名)

    private String applyId;//申请编号

    private String productType;//产品类型:工行

    private String basqxm;//姓名

    private String bazjhm;//身份证号

    private String baqfjg; //签发机构

    private String bahjdz;//户籍地址

    private String baksrq; //起始日期

    private String bajsrq; //截止日期

    private String basjhm;//手机号

    private String mateName;//配偶姓名

    private String mateMobile;//配偶手机

    private String mateIdty;//配偶证件号码

    private String bahyzk; //婚姻状况

    private String lon;//预审批提交经度

    private String lat;//预审批提交纬度

    private ApprovalFileDto zdr_file; //主贷人附件

    private ApprovalFileDto po_file; //配偶附件

}
