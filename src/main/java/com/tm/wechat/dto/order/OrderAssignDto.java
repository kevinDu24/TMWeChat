package com.tm.wechat.dto.order;

import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrderAssignDto {

    private Res result; //状态相应结果集

    private List<OrderAssignListResult> orderList; //订单列表


}
