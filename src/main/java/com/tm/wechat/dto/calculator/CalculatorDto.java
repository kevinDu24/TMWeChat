package com.tm.wechat.dto.calculator;

import lombok.Data;

/**
 * Created by LEO on 16/9/13.
 */
@Data
public class CalculatorDto {
    private Double financeAmount; // 融资金额
    private Double monthPay; // 月供
    private Double dayPay; // 日供
    private Double downPay; // 首付
    private Double deposit; // 保证金
    private Double totalInterest; // 利息总额
    private Double yearInterest; // 年均利息
    private Double yearEarnings; // 年投资收益

    public CalculatorDto(){}

    public CalculatorDto(Double financeAmount, Double monthPay, Double dayPay, Double downPay, Double deposit,
                         Double totalInterest, Double yearInterest, Double yearEarnings){
        this.financeAmount = financeAmount;
        this.monthPay = monthPay;
        this.dayPay = dayPay;
        this.downPay = downPay;
        this.deposit = deposit;
        this.totalInterest = totalInterest;
        this.yearInterest = yearInterest;
        this.yearEarnings = yearEarnings;
    }
}
