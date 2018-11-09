package com.tm.wechat.dto.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.core.result.ProductTypeDto;
import com.tm.wechat.dto.core.result.SpecificTypeDto;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2017/7/13.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecificProductListDto {
    private List<SpecificProductDto> specificProductDtos;
}
