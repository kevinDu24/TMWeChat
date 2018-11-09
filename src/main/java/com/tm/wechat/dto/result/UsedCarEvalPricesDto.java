package com.tm.wechat.dto.result;

import lombok.Data;

/**
 * Created by pengchao on 2018/7/18.
 */
@Data
public class UsedCarEvalPricesDto {
    private String c2b_price;
    private String b2b_price;
    private String b2c_price;
    private String report_url;//评估报告
    private String analysis_id;//评估id
}
