package com.tm.wechat.dto.apply;

import lombok.Data;

/**
 * Created by zcHu on 2017/5/11.
 */
@Data
public class ApplyBackInfoDto {

    private String name;

    private String applyNum;

    private String reason;

    private String status;

    private String createUser;

    private String uniqueMark;


    public ApplyBackInfoDto(Object[] objs){
        this.name = objs[0] == null ? "": objs[0].toString();
        this.applyNum = objs[1] == null ? "": objs[1].toString();
        this.reason = objs[2] == null ? "": objs[2].toString();
        this.status = objs[3] == null ? "": objs[3].toString();
        this.uniqueMark = objs[4] == null ? "": objs[4].toString();
        this.createUser = objs[6].toString();
    }
}
