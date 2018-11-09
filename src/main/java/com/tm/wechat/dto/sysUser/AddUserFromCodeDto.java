package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by pengchao on 2017/12/27.
 */
@Data
public class AddUserFromCodeDto {
    private String name;//  用户姓名
    private String phoneNum;// 手机号
    private String password;// 登录密码
    private String code;// 邀请码：不为空
    private String msgCode;//短信验证码
    private String userName;//用户名
    private String idCard;//身份证号
    private String weChat;//微信号
    private String bankNum; //银行卡号

}
