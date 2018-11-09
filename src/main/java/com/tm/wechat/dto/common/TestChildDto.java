package com.tm.wechat.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestChildDto {
    @JsonProperty("BADLMC")
    private String badlmc;
}
