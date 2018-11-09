package com.tm.wechat.dto.sysUser;

import com.tm.wechat.domain.SysUser;
import lombok.Data;

/**
 * Created by PengChao on 16/9/1.
 */
@Data
public class SysUserDto {
    private String name;
    private String companyName;
    private String role;
    private String username;
    private String phoneNum;
    private String email;
    private String headImg;
    private String code;
    private Boolean isHplUser;

    public SysUserDto(){}

    public SysUserDto(SysUser sysUser){
        this.name = sysUser.getXTCZMC();
        this.companyName = sysUser.getXTJGMC();
        this.role = sysUser.getXTJSMC();
        this.username = sysUser.getXtczdm();
        this.phoneNum = sysUser.getXTYHSJ();
        this.email = sysUser.getXTYHYX();
        this.headImg = sysUser.getTXURL();
    }

}
