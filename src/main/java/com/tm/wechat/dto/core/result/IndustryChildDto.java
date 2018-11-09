package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2017/7/17.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndustryChildDto {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("BASJDM")
    private String basjdm;
}
