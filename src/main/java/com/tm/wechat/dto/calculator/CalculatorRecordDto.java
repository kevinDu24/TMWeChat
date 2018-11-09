package com.tm.wechat.dto.calculator;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by LEO on 16/9/14.
 */
@Data
public class CalculatorRecordDto {
    private Long id;
    private Map condition;
    private Map record;
    private Date time;

    public CalculatorRecordDto(){}

    public CalculatorRecordDto(Long id, Map condition, Map record, Date time){
        this.id = id;
        this.condition = condition;
        this.record = record;
        this.time = time;
    }
}
