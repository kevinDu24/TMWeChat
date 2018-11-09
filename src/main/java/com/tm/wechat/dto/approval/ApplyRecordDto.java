package com.tm.wechat.dto.approval;

import com.tm.wechat.domain.ApplyRecord;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class ApplyRecordDto {

    private String approvalUuid; //申请人的唯一标识

    private String applyNum; //申请单号(from主系统)

    private String status; //状态

    private String reason; //退回原因

    private String createTime;

    private String createUser;

    private String origin;

    public ApplyRecordDto(ApplyRecord applyRecord) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.approvalUuid = applyRecord.getApprovalUuid();
        this.applyNum = applyRecord.getApplyNum();
        this.status = applyRecord.getStatus();
        this.reason = applyRecord.getReason();
        this.createTime = simpleDateFormat.format(applyRecord.getCreateTime());
        this.createUser = applyRecord.getCreateUser();
        this.origin = applyRecord.getOrigin();
    }
}
