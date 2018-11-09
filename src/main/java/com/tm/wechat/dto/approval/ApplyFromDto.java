package com.tm.wechat.dto.approval;

import com.tm.wechat.utils.commons.ItemColumn;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/5/9.
 */
@Data
public class ApplyFromDto {
//    @ItemColumn(keyPath = "#productSourceDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = ProductSourceDto.class)
//    private ProductSourceDto productSourceDto;

    @ItemColumn(keyPath = "#idCardInfoDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = IdCardInfoDto.class)
    private IdCardInfoDto idCardInfoDto;//身份证信息

    @ItemColumn(keyPath = "#driveLicenceInfoDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = DriveLicenceInfoDto.class)
    private DriveLicenceInfoDto driveLicenceInfoDto;//驾驶证信息

    @ItemColumn(keyPath = "#bankCardInfoDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = BankCardInfoDto.class)
    private BankCardInfoDto bankCardInfoDto;//银行卡信息

    @ItemColumn(keyPath = "#otherInfoDto", itemDateType = ItemDataTypeEnum.ClassType,ClassType = OtherInfoDto.class)
    private OtherInfoDto otherInfoDto;//其他信息

    @ItemColumn(keyPath = "#attachmentInfoDto", itemDateType = ItemDataTypeEnum.ListType, listInnerClassType = AttachmentInfoDto.class )
    private List<AttachmentInfoDto> attachmentInfoDto;

    //自动预审批
    @ItemColumn(keyPath = "#mateInfoDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = MateInfoDto.class )
    private MateInfoDto mateInfoDto;//配偶信息

    @ItemColumn(keyPath = "#contactInfoDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = ContactInfoDto.class )
    private ContactInfoDto contactInfoDto;//联系人信息

    @ItemColumn(keyPath = "#maritalStatusDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = MaritalStatusDto.class )
    private MaritalStatusDto maritalStatusDto;//婚姻状况

    @ItemColumn(keyPath = "#approvalAttachmentDto", itemDateType = ItemDataTypeEnum.ClassType, ClassType = ApprovalAttachmentDto.class )
    private ApprovalAttachmentDto approvalAttachmentDto;//附件信息
}
