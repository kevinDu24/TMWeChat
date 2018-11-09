package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2017/7/13.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductTypeListDto {
    private List<ProductTypeDto> cpfabycxlist;
    private String cpxlBycx;
}
