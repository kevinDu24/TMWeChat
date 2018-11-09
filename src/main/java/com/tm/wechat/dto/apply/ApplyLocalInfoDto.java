package com.tm.wechat.dto.apply;

import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by zcHu on 2017/5/10.
 */
@Data
public class ApplyLocalInfoDto {

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 申请编号
     */
    private String applyNum;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建申请时的时间唯一值
     */
    private String uniqueMark;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 状态
     */
    private String status;

    public ApplyLocalInfoDto(Object[] objs){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.name = objs[0] == null ? "" : objs[0].toString();
        this.applyNum = objs[1] == null ? "" : objs[1].toString();
        this.status = objs[2] == null ? "" : objs[2].toString();
        this.createTime = sdf.format(objs[5]);
        this.uniqueMark = objs[3] == null ? "" : objs[3].toString();
        this.createUser = objs[6].toString();
    }
}
