package com.tm.wechat.dto.calculator.yc;

import lombok.Data;

@Data
public class CalculatorRespDto {
    private Double financeAmount; // 融资金额
    private Double monthPay; // 月供
    private Double dayPay; // 日供
    private Double downPay; // 首付
    private Double deposit; // 保证金
    private Double totalInterest; // 利息总额
    private Double yearInterest; // 年均利息
    private Double yearEarnings; // 年投资收益

    public CalculatorRespDto(){}

    public CalculatorRespDto(Double financeAmount, Double monthPay, Double dayPay, Double downPay, Double deposit,
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
