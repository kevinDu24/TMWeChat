package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by pengchao on 2018/4/23.
 */
@Data
public class SysUserFunctionDto {

    private Long functionId; //功能id

    private String description; //功能描述

    private String state; //0:禁用状态 1,"",null 未禁用状态
}
