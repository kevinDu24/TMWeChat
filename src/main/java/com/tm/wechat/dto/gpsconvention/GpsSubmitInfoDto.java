package com.tm.wechat.dto.gpsconvention;

import lombok.Data;

/**
 * 分单列表dto
 * Created by LEO on 16/9/29.
 */
@Data
public class GpsSubmitInfoDto {

    private String applyNum; //订单编号

    private String installBrand; //安装品牌

    private String installWay; //安装方式

    private String contactsName; //联系人姓名

    private String contactsPhone; //联系人电话

    private String installDate; //安装日期

    private String installAddress; //安装地点

    private String installProvince; //安装省份

    private String installCity; //安装城市

    private String installCounty; //安装县
}
