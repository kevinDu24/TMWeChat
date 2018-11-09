package com.tm.wechat.dto.sysUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.SysUser;
import lombok.Data;

import java.util.List;

/**
 * Created by PengChao on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserCallBackDto {
    List<SysUser> users;

}
