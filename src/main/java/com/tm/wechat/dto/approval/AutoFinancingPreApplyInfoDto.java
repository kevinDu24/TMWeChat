package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/24.
 */
@Data
public class AutoFinancingPreApplyInfoDto {

    private String userid;//经销商编号(用户名)

    private String applyId;//申请编号

    private String name;//姓名

    private String idty;//身份证号

    private String mobile;//手机号

    private String contact1Name;//联系人1姓名

    private String contact1Mobile;//联系人1手机

    private String contact1Relationship;//联系人1关系

    private String contact2Name;//联系人2姓名

    private String contact2Mobile;//联系人2手机

    private String contact2Relationship;//联系人2关系

    private String firstCommitTime;//首次提交时间

    private String mateName;//配偶姓名

    private String mateMobile;//配偶手机

    private String mateIdty;//配偶证件号码

    private String company;//配偶工作单位

    private String address;//配偶地址

    private String bahyzk; //婚姻状况

    private String wxId; //在线助力融提交微众唯一标识

    private String lon;//预审批提交经度

    private String lat;//预审批提交纬度



}
