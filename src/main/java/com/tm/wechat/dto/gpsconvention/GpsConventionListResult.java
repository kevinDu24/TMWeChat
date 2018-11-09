package com.tm.wechat.dto.gpsconvention;

import lombok.Data;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class GpsConventionListResult {

    private String userName; //订单所属人员

    private String name; //客户姓名

    private String applyNum; //申请编号

    private String createTime; //通过时间
    
    private String state; //邀约状态（已邀约，未邀约）
     

}
