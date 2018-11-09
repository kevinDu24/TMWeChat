package com.tm.wechat.dto.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarTypePriceDto {
    private String group; //组别，ABC
    private List<CarTypePriceChildDto> dataRows;
}
