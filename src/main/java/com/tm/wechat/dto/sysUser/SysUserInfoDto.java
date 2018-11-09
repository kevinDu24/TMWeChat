package com.tm.wechat.dto.sysUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/9/5.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserInfoDto {
    /**
     * XTJGMC : 测试账号
     * XTJGID : 6247
     * XTJGDM : SH000
     *
     */
    private String XTJGMC;
    private String XTJGID;
    private String XTJGDM;

}
