package com.tm.wechat.dto.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.core.result.CarTypePriceChildResult;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarTypePriceChildDto {
    private String id;
    private String baclmc;//车型
    private BigDecimal cxzdjg;//参考价格
    private BigDecimal backgz;// 参考购置税
    private BigDecimal bapail;//排量
    private BigDecimal bazwsl;//座位数

    public CarTypePriceChildDto(CarTypePriceChildResult row) {
        this.id = row.getId();
        this.baclmc = row.getBaclmc();
        this.cxzdjg = row.getCxzdjg();
        this.bapail = row.getBapail();
        this.bazwsl = row.getBazwsl();
    }
}
