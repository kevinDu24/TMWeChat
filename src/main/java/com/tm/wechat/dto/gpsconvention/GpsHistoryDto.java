package com.tm.wechat.dto.gpsconvention;

import lombok.Data;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class GpsHistoryDto {

    private String applyNum; //订单编号

    private String contactsName; //联系人姓名

    private String createTime; //创建时间

    private String createUser; //提交人

}
