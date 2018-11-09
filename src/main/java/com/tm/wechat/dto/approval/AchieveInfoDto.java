package com.tm.wechat.dto.approval;

import com.tm.wechat.consts.ApprovalType;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by zcHu on 2017/5/11.
 */
@Data
public class AchieveInfoDto {

    private String name;

    private String reason;

    private String status;

    private String uniqueMark;

    private String idCardNum;

    private String phoneNumber;

    private String applyNum;

    private String time;

    private String createUser;

    private String origin;


    public AchieveInfoDto(Object[] objs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.name = objs[0] == null ? "" : objs[0].toString();
        this.reason = objs[1] == null ? "" : objs[1].toString();
        this.status = (ApprovalType.PASS.code().equals(objs[2].toString()) && !ApprovalType.REFUSE_NOREASON.code().equals(objs[10])) ? ApprovalType.PASS.value() : ApprovalType.REFUSE_NOREASON.value();
        this.uniqueMark = objs[3] == null ? "" : objs[3].toString();
        this.time = objs[4] == null ? "" : sdf.format(objs[4]);
        this.applyNum = objs[5] == null ? null : objs[5].toString().length() <= 10 ? objs[5].toString() : null;
        this.idCardNum = objs[6] == null ? "" : objs[6].toString();
        this.phoneNumber = objs[7] == null ? "" : objs[7].toString();
        this.createUser = objs[8].toString();
        this.origin = objs[9] == null ? "" : objs[9].toString();
    }

}
