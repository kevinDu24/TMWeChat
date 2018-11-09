package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarTypePriceChildResult {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("BACLMC")
    private String baclmc; // 车型

    @JsonProperty("CXZDJG")
    private BigDecimal cxzdjg;//参考价格

    @JsonProperty("BAPAIL")
    private BigDecimal bapail;//排量

    @JsonProperty("BAZWSL")
    private BigDecimal bazwsl;//座位数

}
