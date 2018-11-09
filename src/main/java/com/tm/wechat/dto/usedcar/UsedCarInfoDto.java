package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarInfoDto {
    /**
     * series_group_name : 一汽奥迪
     * min_reg_year : 2009
     * model_liter : 2.0T
     * model_year : 2010
     * brand_name : 奥迪
     * model_id : 82
     * max_reg_year : 2012
     * brand_id : 1
     * series_id : 3
     * model_emission_standard : 国4
     * model_name : 2010款 奥迪A6L 2.0 TFSI 自动标准型
     * model_price : 39.99
     * model_gear : 自动
     * series_name : 奥迪A6L
     * ext_model_id : 82
     */
    private String model_id;//车型id
    private String brand_id;//品牌id
    private String series_id;//车系id
    private String brand_name;//品牌名称
    private String model_name;//车型名称
    private String series_name;//车系名称
    private String min_reg_year; //最小上牌年份
    private String max_reg_year;//最大上牌年份
    private String model_year;//车型年款
    private String model_price;//指导价
    private String ext_model_id;//如果合作伙伴的车型在车300这边做了映射的话，该字段会返回合作伙伴的车型ID，该字段为0就表示没有映射上去；如果双方没有映射那么该字段就是车300的车型ID。
}
