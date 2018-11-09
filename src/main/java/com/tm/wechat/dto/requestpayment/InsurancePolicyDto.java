package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2018/3/23.
 */
@Data
public class InsurancePolicyDto {

//    private String commercialInsurance ;//商业险
//
//    private String compulsoryInsurance ;//交强险
//
//    private String registration; // 登记证
//
//    private String drivingLicense; // 行驶证
//
//    private String applyNum; //申请编号

    private String applyNum; //申请编号

    private List<SingleFileDto> policyList; //文件信息附件集合

    /**
     * gps方案
     */
    private String gpsScheme;

    /**
     * 车辆销售价
     */
    private Float vehicleSalesPrice;

    /**
     * SN号
     */
    private String sn;

    /**
     * 车辆登记日期
     */

    private Date VehicleRegDate;

    /**
     * 车牌号
     */
    private String plateNumber;
}
