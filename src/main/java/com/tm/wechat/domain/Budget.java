package com.tm.wechat.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by LEO on 16/9/7.
 *销售计划表
 */
@Entity
@Data
public class Budget {
    @Id
    @GeneratedValue
    private Long id;
    private Long levelId;
    private String area;
    @Temporal(TemporalType.DATE)
    private Date planDate;
    private Long budget;
    private Integer type;
    private Long parentId;
    private String customer;  //客户标识 : taimeng or yachi
}
