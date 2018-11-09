package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LEO on 16/9/13.
 */
@Data
@Entity
public class CalculatorRecord {

    @Id
    @GeneratedValue
    private Long id;
    private String condition;
    private String record;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private String operator;
    private String customer;  //客户标识 : taimeng or yachi

    public CalculatorRecord(){}

    public CalculatorRecord(String condition, String record, String userName, String customer){
        this.condition = condition;
        this.record = record;
        this.time = new Date();
        this.operator = userName;
        this.customer = customer;
    }
}
