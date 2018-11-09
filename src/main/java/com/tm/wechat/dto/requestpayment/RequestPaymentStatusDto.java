package com.tm.wechat.dto.requestpayment;

import lombok.Data;

/**
 * Created by pengchao on 2018/4/16.
 */
@Data
public class RequestPaymentStatusDto {


    private String gpsStatus; //gps激活信息 0,null,""：未激活, 1:已激活

    private String insuranceInfoState; //保单信息是否已保存 0,null,""：未完善, 1:已完善

    private String fileInfoState; //文件信息是否已保存  0,null,""：未完善, 1:已完善, null: 无


    public RequestPaymentStatusDto() {
    }

    public RequestPaymentStatusDto(Object[] objs) {
        this.gpsStatus = objs[0] == null ? "" : objs[0].toString();
        this.insuranceInfoState = objs[1] == null ? "" : objs[1].toString();
        this.fileInfoState = objs[2] == null ? "" : objs[2].toString();
    }

}
