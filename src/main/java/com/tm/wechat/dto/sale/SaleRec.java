package com.tm.wechat.dto.sale;

import lombok.Data;

/**
 * Created by LEO on 16/9/8.
 */
@Data
public class SaleRec {
    private Long parentId;
    private String startDate;
    private String endDate;
    private Integer planType;
}
