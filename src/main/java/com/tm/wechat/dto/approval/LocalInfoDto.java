package com.tm.wechat.dto.approval;

import com.tm.wechat.domain.Approval;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by zcHu on 2017/5/10.
 */
@Data
public class LocalInfoDto {

    private String name;

    private String createUser;

    private String createTime;

    private String uniqueMark;

    private String applyNum;

    private String origin;

    private String wxState;

    public LocalInfoDto(Approval approval){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.name = approval.getItemStringValue();
        this.createTime = sdf.format(approval.getCreateTime());
        this.uniqueMark = approval.getUniqueMark();
    }


    public LocalInfoDto(Object[] objs){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.uniqueMark = objs[0] == null ? "" : objs[0].toString();
        this.createUser = objs[1].toString();
        this.createTime = sdf.format(objs[2]);
        this.name = objs[3] == null ? "" : objs[3].toString();
        this.origin = objs[4] == null ? "" : objs[4].toString();
        this.wxState = objs[5] == null ? "" : objs[5].toString();
    }
}
