package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/26.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifyDriverCardResultDto {

    /**
     * engine_num : 416098
     * owner : 钱川
     * plate_num : 沪A0M084
     * vin : LSVFF66R8C2116280
     * model : 大众汽车牌SVW71611KS
     * register_date : 20121127
     */
    private String engine_num;//发动机号
    private String plate_num;//车牌号码
    private String vin;//车架号
    private String model;//厂牌型号
    private String register_date;//注册日期
}
