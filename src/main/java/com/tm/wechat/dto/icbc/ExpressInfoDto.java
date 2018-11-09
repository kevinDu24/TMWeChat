package com.tm.wechat.dto.icbc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpressInfoDto {

    private String COURIERNUMBER; //快递单号
    private String CONSIGNOR; //发货人姓名
    private String POSTTIME; //发货时间
    private String BANKCARDNUM; //银行卡号
    private String CONSIGNEE; //收件人姓名
    private String RECEIVEADDRESSR; //收货地址
    private String PHONENUM;//手机号

}
