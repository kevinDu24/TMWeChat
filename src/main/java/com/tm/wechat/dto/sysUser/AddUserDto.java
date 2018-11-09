package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by yuanzhenxia on 2017/8/9.
 */
@Data
public class AddUserDto {

    private String xTCZDM; // 用户代码（新增）
    private String xTCZMC; // 用户名称（新增）
    private String pWD1;// 用户密码（新增）
    private String xTJGDM;// APP操作人
    private String xTBMMC; // 经销商部门
    private String xTYHSJ;// 用户手机
    private String xTCZRQ; // 操作日期
    private String xTCZSJ;// 操作时间
    private String xTCZRY;// 操作员
    private Long sJBMDM;//上级部门代码
}
