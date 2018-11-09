package com.tm.wechat.dto.sysUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.domain.SysUserRole;
import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2017/8/10.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserRoleCallBackDto {

    List<SysUserRole> zam017;
}
