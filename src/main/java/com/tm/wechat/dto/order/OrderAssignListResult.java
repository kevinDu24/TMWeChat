package com.tm.wechat.dto.order;

import lombok.Data;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrderAssignListResult {

    private String userName; //订单所属人员

    private String name; //客户姓名

    private String applyNum; //申请编号

    private String state; //订单状态

    private String createTime; //创建时间

}
