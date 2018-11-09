package com.tm.wechat.dto.contractsign;

import lombok.Data;

/**
 * Created by pengchao on 2018/3/21.
 */
@Data
public class GpsInstallInfoDto {

    private String installBrand; //安装品牌 鲁诺、车晓

    private String installWay;//安装方式 自行安装、厂商安装

    private String contactsName;//联系人姓名

    private String contactsPhone;//联系人电话

    private String installDate;//安装日期

    private String installAddress;//安装地点

}
