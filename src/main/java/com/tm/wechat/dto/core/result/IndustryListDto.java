package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2017/7/17.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndustryListDto {
    private List<IndustryChildDto> dataRows;
}
