package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2018/2/24.
 */
@Data
public class ApprovalImagesDto {

    private String frontImg; //正面图

    private String behindImg;//反面图

    private String driveLicenceImg;//驾驶证件图片

    private String bankImg;//银行卡图片

    public ApprovalImagesDto() {
    }

    public ApprovalImagesDto(ApplyFromDto applyFromDto) {
        this.frontImg = applyFromDto.getIdCardInfoDto().getFrontImg();
        this.behindImg = applyFromDto.getIdCardInfoDto().getBehindImg();
        this.driveLicenceImg = applyFromDto.getDriveLicenceInfoDto().getLicenceImg();
        this.bankImg = applyFromDto.getBankCardInfoDto().getBankImg();
    }
}
