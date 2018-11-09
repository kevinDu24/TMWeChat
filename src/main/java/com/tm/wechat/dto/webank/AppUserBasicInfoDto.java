package com.tm.wechat.dto.webank;

import com.tm.wechat.dto.approval.ApplyFromDto;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class AppUserBasicInfoDto {

    private String name;//申请人姓名

    private String idCard;//申请人身份证号

    private String bankCardNum;//申请人银行卡卡号

    private String bank;//申请人银行名称

    private String phoneNum;//银行预留手机号(需带入一审页面)

    private String monthlyIncome;//月收入

    private String appId; // app应用id

    private String fpName;//经销商



    public AppUserBasicInfoDto(ApplyFromDto applyFromDto) {
        this.name = applyFromDto.getIdCardInfoDto().getName() == null ? "" : applyFromDto.getIdCardInfoDto().getName();
        this.idCard = applyFromDto.getIdCardInfoDto().getIdCardNum() == null ? "" : applyFromDto.getIdCardInfoDto().getIdCardNum();
        this.bankCardNum = applyFromDto.getBankCardInfoDto().getAccountNum() == null ? "" : applyFromDto.getBankCardInfoDto().getAccountNum();
        this.bank = applyFromDto.getBankCardInfoDto().getBank() == null ? "" : applyFromDto.getBankCardInfoDto().getBank();
        this.phoneNum = applyFromDto.getBankCardInfoDto().getBankPhoneNum() == null ? "" : applyFromDto.getBankCardInfoDto().getBankPhoneNum();
        this.monthlyIncome = applyFromDto.getBankCardInfoDto().getMonthlyIncome();
        this.appId = applyFromDto.getBankCardInfoDto().getAppId();
    }

    public AppUserBasicInfoDto() {
    }

}
