package com.tm.wechat.dto.gps;

import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/9/19.
 */
@Data
public class ActivateDeviceDto {
    private String type; // 安装设备类型：无线GPS追踪器;有线GPS追踪器
    private String vin; // 车架号后6位
    private String carOwnerName; // 车主姓名
    private String simCardNum; // sim卡号
    private String addr; // 安装地址
    private String lat; // 纬度
    private String lon; // 经度
    private InstallPerson installPerson; // 安装人员
    private List<String> fileIds; // 图片ID
    private String applyNum; // 申请编号

    private String access_token; // 微信公众号access_token
}
