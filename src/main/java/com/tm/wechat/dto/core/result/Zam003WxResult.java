package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.domain.SysUserRole;
import com.tm.wechat.dto.result.CoreRes;
import lombok.Data;


/**
 * Created by yuanzhenxia on 2017/8/7.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Zam003WxResult {

     SysUser zam003Re;

    private CoreRes result;

    private SysUserRole zam017Re;

    private String xtczdm; //系统操作代码

}
