package com.tm.wechat.dto.sysUser;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/3/24.
 */
@Data
public class UserListDto {
    private String type; // 0:经销商，1:分公司，2:团队账号
    private List<BranchOfficeDto> branchOfficeList;
    private List<String> teamList;
}
