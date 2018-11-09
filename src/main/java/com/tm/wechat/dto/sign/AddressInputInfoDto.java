package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/6/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressInputInfoDto {

    private String applyNum; //申请编号

    private String name; //客户姓名

    private String phoneNum; //手机号

    private String address; //收货地址

    private String province; //收货省份

    private String city; //收货城市
}
