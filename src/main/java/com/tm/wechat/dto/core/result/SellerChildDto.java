package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2017/7/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerChildDto {

    @JsonProperty("BADMID")
    private String badmid;
    @JsonProperty("BADMMC")
    private String badmmc;
}
