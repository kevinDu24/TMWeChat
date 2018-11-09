package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class AppUserBasicInfoDto {

    private String fpName;//经销商编号(用户名)

    private String uniqueMark ;//唯一标识

    private String name;//申请人姓名

    private String idCard;//申请人身份证号

    private String otherPhoneNum;//申请人手机号

    private String bankCardNum;//申请人银行卡卡号

    private String bank;//申请人银行名称

    private String phoneNum;//银行预留手机号(需带入一审页面)
    
    private String homeNumber; //住宅电话
    
    private String vicePhoneNumber;//副手机号;

    private String origin;//来源


    public AppUserBasicInfoDto(ApplyFromDto applyFromDto) {
        this.name = applyFromDto.getIdCardInfoDto().getName() == null ? "" : applyFromDto.getIdCardInfoDto().getName();
        this.idCard = applyFromDto.getIdCardInfoDto().getIdCardNum() == null ? "" : applyFromDto.getIdCardInfoDto().getIdCardNum();
        this.otherPhoneNum = applyFromDto.getOtherInfoDto().getPhoneNumber() == null ? "" : applyFromDto.getOtherInfoDto().getPhoneNumber();
        this.bankCardNum = applyFromDto.getBankCardInfoDto().getAccountNum() == null ? "" : applyFromDto.getBankCardInfoDto().getAccountNum();
        this.bank = applyFromDto.getBankCardInfoDto().getBank() == null ? "" : applyFromDto.getBankCardInfoDto().getBank();
        this.phoneNum = applyFromDto.getBankCardInfoDto().getBankPhoneNum() == null ? "" : applyFromDto.getBankCardInfoDto().getBankPhoneNum();
        this.homeNumber = applyFromDto.getOtherInfoDto().getHomeNumber() == null ? "" : applyFromDto.getOtherInfoDto().getHomeNumber();;
        this.vicePhoneNumber = applyFromDto.getOtherInfoDto().getVicePhoneNumber() == null ? "" : applyFromDto.getOtherInfoDto().getVicePhoneNumber();;
    }

    public AppUserBasicInfoDto() {
    }
}
