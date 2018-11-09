package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/7/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarCityDto {
    private String group; //组别，ABC
    private List<UsedCarCityListDto> dataRows;
}
