package com.tm.wechat.dto.approval;

import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by zcHu on 2017/5/11.
 */
@Data
public class BackInfoDto {
    //申请用户
    private String name;
    //申请编号
    private String applyNum;
    //退回原因
    private String reason;
    //状态
    private String status;
    //唯一编号
    private String uniqueMark;
    //创建用户
    private String createUser;
    //修改时间
    private String updateTime;
    //产品类型
    private String origin;


    public BackInfoDto(Object[] objs){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.name = objs[0] == null ? "": objs[0].toString();
        this.applyNum = objs[1] == null ? "": objs[1].toString();
        this.reason = objs[2] == null ? "": objs[2].toString();
        this.status = objs[3] == null ? "": objs[3].toString();
        this.uniqueMark = objs[4] == null ? "": objs[4].toString();
        this.updateTime = objs[5] == null ? "" : sdf.format(objs[5]);
        this.createUser = objs[6].toString();
        this.origin = objs[7].toString();
    }
}
