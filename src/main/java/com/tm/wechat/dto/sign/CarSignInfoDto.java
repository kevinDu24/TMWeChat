package com.tm.wechat.dto.sign;

import lombok.Data;

/**
 * Created by pengchao on 2018/4/10.
 */
@Data
public class CarSignInfoDto {

    private String vehicleIdentifyNum; //车架号

    private String dateOfManufacture; //出厂日期

    private String colour; //颜色

    private String engineNo; // 发动机号

    private String vehicleLicensePlate; // 车牌号

    private String vehicleConfigDescription; // 车辆配置描述

    private String signAuthBookImg; //签署电子签章授权书照片

    private String authBookImg; //电子签章授权书照片

    private String holdAuthBookImg; //手持电子签章授权

    private String applyNum;//申请编号

    private String submitState;//提交状态
}
