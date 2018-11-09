package com.tm.wechat.dto.order;

import lombok.Data;

import java.util.List;

/**
 * 组织架构及数量dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrganizationAndCountDto {

    private String userName; //用户名

    private String applyCount; //申请数量

    private String parentId; //上级id

    private List<OrganizationAndCountDto> users; //下级用户信息

}
