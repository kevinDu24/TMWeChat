package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class ApprovalCountDto {
    /**
     * 待提交
     */
    private int toSubmitListCount;

    /**
     * 工行退回
     */
    private int backListCount;

    /**
     * 审批中
     */
    private int approvalListCount;

    /**
     * 审批已完成
     */
    private int passListCount;


    public ApprovalCountDto(int toSubmitListCount, int backListCount, int approvalListCount, int passListCount) {
        this.toSubmitListCount = toSubmitListCount;
        this.backListCount = backListCount;
        this.approvalListCount = approvalListCount;
        this.passListCount = passListCount;
    }

    public ApprovalCountDto(Object[] objs) {
        this.toSubmitListCount = Integer.valueOf(objs[0].toString());
        this.backListCount = Integer.valueOf(objs[1].toString());
        this.approvalListCount = Integer.valueOf(objs[2].toString());
        this.passListCount = Integer.valueOf(objs[3].toString());
    }


    public ApprovalCountDto() {
    }
}
