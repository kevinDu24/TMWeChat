package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarCityListDto {
    /**
     * admin_code : 654300
     * city_name : 阿勒泰
     * prov_id : 32
     * initial : A
     * prov_name : 新疆
     * city_id : 390
     */
    private String city_name;
    private String prov_id;
    private String prov_name;
    private String city_id;
}
