package com.tm.wechat.dto.order;

import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 组织架构dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrganizationDto implements Serializable {

    private Res result; //状态相应结果集

    private List<OrganizationResult> userLowers; //组织架构用户信息


}
