package com.tm.wechat.domain.yc;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by HJYang on 2016/12/1.
 */
@Data
@Entity
public class FinanceKind {
    @Id
    @GeneratedValue
    private Long id;
    private String name; //融资产品名称
    private Double downPay; //首付比例
    private String rate; //年利率
    private Double maxFinance; //融资上限
    private Double minFinance; //融资下限
    private Integer minPeriod; //最小融资周期
    private Integer maxPeriod; //最大融资周期
    private Double depositRate; //保证金比例(若有)
    @ManyToOne
    private CarBrand carBrand; //车辆品牌
}
