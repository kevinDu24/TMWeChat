package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.usedcar.UsedCarCityDto;
import com.tm.wechat.dto.usedcar.UsedCarCityListDto;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/7/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarCityListResultDto {
    private String status;
    private String error_msg;
    private String prov_id;
    private String city_id;
    private List<UsedCarCityListDto> city_list;
    private List<UsedCarCityDto> cityListResult;
}
