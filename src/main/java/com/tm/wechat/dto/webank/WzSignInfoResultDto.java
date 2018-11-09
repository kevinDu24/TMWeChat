package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 查询微众电子签约信息
 * Created by pengchao on 2018/5/31.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown =true)
public class WzSignInfoResultDto {

    @JsonProperty("CODE")
    private String code; //状态码
    @JsonProperty("MESSAGE")
    private String message; //消息
    @JsonProperty("DATA")
    private WzSignInfoDto data; //业务数据

}
