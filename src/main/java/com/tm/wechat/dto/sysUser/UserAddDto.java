package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by pengchao on 2018/4/17.
 */
@Data
public class UserAddDto {
    private String status;
    private String password;
    private String username;//要给谁添加
    private String loginuser; //登录用户名
    private String name;//系统操作代码

}
