package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by LEO on 16/9/12.
 */
@Entity
@Data
public class CarType {

    @Id
    @GeneratedValue
    private Long id;
    private String name; // 车型名称
    private Integer used; // 新车/二手车,0为新车,1为二手车

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "carType")
    private List<FinanceProduct> financeProducts;
}
