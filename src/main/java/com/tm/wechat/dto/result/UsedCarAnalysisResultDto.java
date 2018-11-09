package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.usedcar.IdentifyDriverCardResultDto;
import com.tm.wechat.dto.usedcar.UsedCarCityDto;
import com.tm.wechat.dto.usedcar.UsedCarCityListDto;
import com.tm.wechat.dto.usedcar.UsedCarInfoDto;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/7/18.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsedCarAnalysisResultDto {

    private UsedCarEvalPricesDto eval_prices;
    private List<UsedCarInfoDto> modelInfo;
    private String report_url;
    private String status;
    private String error_msg;
    private String prov_id;
    private String city_id;
    private List<UsedCarCityListDto> city_list;
    private List<UsedCarCityDto> cityListResult;
    private IdentifyDriverCardResultDto data;
}
