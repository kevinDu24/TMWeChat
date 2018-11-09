package com.tm.wechat.dto.approval;

import com.tm.wechat.consts.ApprovalType;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by zcHu on 2017/5/11.
 */
@Data
public class SubmitInfoDto {

    private String name;

    private String createUser;

    private String applyNum;

    private String status;

    private String uniqueMark;

    private String approvalSubmitTime;

    public SubmitInfoDto(Object[] objs){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.name = objs[0] == null ? "": objs[0].toString();
//        this.applyNum = objs[1] == null ? "": objs[1].toString();
        this.status = ApprovalType.SUBMIT.value();
        this.uniqueMark = objs[3] == null ? "": objs[3].toString();
        this.createUser = objs[5].toString();
        this.approvalSubmitTime = objs[6] == null ? "" : sdf.format(objs[6]);
    }
}
