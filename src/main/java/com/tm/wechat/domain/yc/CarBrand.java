package com.tm.wechat.domain.yc;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by HJYang on 2016/12/1.
 */
@Data
@Entity
public class CarBrand {
    @Id
    @GeneratedValue
    private Long id;
    private String name; //车辆品牌名称

    @OneToMany(mappedBy = "carBrand")
    private List<FinanceKind> financeKinds;
}
