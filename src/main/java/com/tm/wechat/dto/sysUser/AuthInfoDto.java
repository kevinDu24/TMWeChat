package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by pengchao on 2018/4/14.
 */
@Data
public class AuthInfoDto {

    private String phoneState;//手机认证状态

    private String idCardState; //身份验证状态

    private String userInfoState;//用户信息
}
