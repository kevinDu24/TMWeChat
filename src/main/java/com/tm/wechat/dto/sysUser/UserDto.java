package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created yuanzhenxia 37399 on 2017/8/7.
 */
@Data
public class UserDto {
    private String userCode;
    private String userType;
    private String password;
    private String phoneNum;
    private String sjbmdm;
}
