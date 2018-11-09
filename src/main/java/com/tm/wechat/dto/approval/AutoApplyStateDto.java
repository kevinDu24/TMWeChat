package com.tm.wechat.dto.approval;

import lombok.Data;


/**
 * Created by pengchao on 2018/5/10.
 */
@Data
public class AutoApplyStateDto {


    private String idCardInfoState;//身份证信息

    private String driveLicenceInfoState;//驾驶证信息

    private String bankCardInfoState;//银行卡信息

    private String otherInfoState;//其他信息

    private String mateInfoState;//配偶信息

    private String contactInfoState;//联系人信息

    private String maritalStatusState;//婚姻状况

    private String approvalAttachmentState;//附件信息

}
