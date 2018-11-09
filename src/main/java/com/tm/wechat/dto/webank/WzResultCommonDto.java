package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微众结果接受结果集
 * Created by zcHu on 18/1/6.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown =true)
public class WzResultCommonDto {
    @JsonProperty("CODE")
    private String code; //状态码
    @JsonProperty("MESSAGE")
    private String message; //消息
    @JsonProperty("DATA")
    private Object data; //业务数据
}
