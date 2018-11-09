package com.tm.wechat.dto.apply;

import com.tm.wechat.consts.ApprovalType;
import lombok.Data;

/**
 * Created by zcHu on 2017/5/11.
 */
@Data
public class ApplySubmitInfoDto {

    private String name;

    private String applyNum;

    private String status;

    private String createUser;

    private String uniqueMark;


    public ApplySubmitInfoDto(Object[] objs){
        this.name = objs[0] == null ? "": objs[0].toString();
        this.applyNum = objs[1] == null ? "": objs[1].toString();
        this.uniqueMark = objs[3] == null ? "": objs[3].toString();
        this.status = ApprovalType.WAIT_APPROVAL.value();
        this.createUser = objs[5].toString();
    }
}
