package com.tm.wechat.dto.sysUser;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/3/24.
 */
@Data
public class BranchOfficeDto {
    private String userName;
    private List<String> teamList;
}
