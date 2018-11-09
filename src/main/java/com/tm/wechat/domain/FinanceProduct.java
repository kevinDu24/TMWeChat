package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by LEO on 16/9/12.
 * 融资产品表
 */
@Entity
@Data
public class FinanceProduct {

    @Id
    @GeneratedValue
    private Long id;
    private String name; // 融资产品名称
    private Double downPay; // 首付比例
    private String rate; // 年利率
    private Double maxFinance; // 融资上限
    private Integer minPeriod; // 最小融资周期
    private Integer maxPeriod; // 最大融资周期
    private Double depositRate; // 保证金比例(若有)
    @ManyToOne
    private CarType carType; // 适用车型
}
