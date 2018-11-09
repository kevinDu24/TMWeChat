package com.tm.wechat.dto.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneAuthDto {
    private String province;//省份
    private String city;//城市
    private String areacode;//区号
    private String zip;// 邮编
    private String company;//运营商
    private String card;//类型
}
