package com.tm.wechat.dto.approval;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/6/1.
 */
@Data
public class FinancingPreApplyDto {
    //人工预审批
    private IdCardInfoDto idCardInfoDto;//身份证信息

    private DriveLicenceInfoDto driveLicenceInfoDto;//驾驶证信息

    private BankCardInfoDto bankCardInfoDto;//银行卡信息

    private OtherInfoDto otherInfoDto;//其他信息

    private List<AttachmentInfoDto> attachmentInfoDto;
}
