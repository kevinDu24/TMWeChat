package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.domain.SysUserRole;
import lombok.Data;

/**
 * Created by pengchao on 2018/3/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResult {
    private String resultCode;
    private String isSuccess;
    private String resultMsg;
    private SysUser zam003Re;
    private SysUserRole zam017Re;
}
