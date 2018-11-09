package com.tm.wechat.dto.requestpayment;

import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by pengchao on 2018/4/16.
 * 待请款列表中的请款数据
 */
@Data
public class RequestPaymentListDto {

    private String name;

    private String applyNum;

    private String createTime;

    private String userName;

    private String state; //请款状态（文字）

    private String applyResult; //请款状态

    private String applyResultReason; //原因

    private String carType; //车辆类型

    private String signState;//是否已签约

    // 增加参数是否为退回,这里的退回指的是否之前已经提交过一次，现在被退回到这个步  0  否    1 是
    private String isReturn = "0";





    public RequestPaymentListDto(Object[] objs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.name = objs[0] == null ? "" : objs[0].toString();
        this.applyNum = objs[1] == null ? "" : objs[1].toString();
        this.createTime = sdf.format(objs[5]);
    }

    public RequestPaymentListDto() {
    }
}
