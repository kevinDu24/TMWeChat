package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by yuanzhenxia on 2017/7/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerListDto {
    private List<SellerChildDto> dataRows;
}
