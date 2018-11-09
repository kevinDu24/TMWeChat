package com.tm.wechat.dto.order;

import lombok.Data;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class OrderHistoryDto {

    private String name; //客户姓名

    private String targetUser; //分单至XXX

    private String sourceUser;//分单操作人

    private String applyNum;//申请编号

    private String createTime;

}
