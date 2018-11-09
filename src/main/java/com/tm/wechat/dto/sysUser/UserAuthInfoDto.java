package com.tm.wechat.dto.sysUser;

import lombok.Data;

/**
 * Created by pengchao on 2018/5/3.
 */
@Data
public class UserAuthInfoDto {

    private UserProductInfoResultDto userProductInfoResultDto;

    private SysUserFunctionResultDto sysUserFunctionResultDto;

    private String loginAuth; //

}
