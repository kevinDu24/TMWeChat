package com.tm.wechat.dto.order;

import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;

/**
 * 组织架构及数量dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrganizationAndCountResult {

    private Res result; //状态相应结果集

    private List<OrganizationAndCountListResult> userApplylist; //下级用户及申请数量集合

}
