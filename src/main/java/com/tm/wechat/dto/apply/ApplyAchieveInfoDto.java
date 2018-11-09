package com.tm.wechat.dto.apply;

import com.tm.wechat.consts.ApprovalType;
import lombok.Data;

/**
 * Created by zcHu on 2017/5/11.
 */
@Data
public class ApplyAchieveInfoDto {

    private String name;

    private String reason;

    private String status;

    private String uniqueMark;

    private String applyNum;

    private String createUser;

    public ApplyAchieveInfoDto(Object[] objs){
        this.name = objs[0] == null ? "": objs[0].toString();
        this.reason = objs[1] == null ? "": objs[1].toString();
        this.status = ApprovalType.APPROVAL_PASS.code().equals(objs[2].toString()) ? ApprovalType.APPROVAL_PASS.value() : ApprovalType.APPROVAL_REFUSE.value();
        this.uniqueMark = objs[3] == null ? "": objs[3].toString();
        this.applyNum = objs[5] == null ? null : objs[5].toString().length() <= 10 ? objs[5].toString() : null;
        this.createUser = objs[8].toString();
    }
}
