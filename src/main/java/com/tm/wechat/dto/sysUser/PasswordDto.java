package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2017/8/8.
 */
@Data
public class PasswordDto {
    private String userCode; // 用户代码
    private String newPwd; // 新密码
    private String oldPwd; // 原密码
}
