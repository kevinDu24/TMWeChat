package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by yuanzhenxia on 2017/7/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaseBackChildDto {

    @JsonProperty("BADYID")
    private String badyid;

    @JsonProperty("BASZCS")
    private String baszcs;//抵押城市

    @JsonProperty("BADYGS")
    private String badygs;//抵押公司


}
