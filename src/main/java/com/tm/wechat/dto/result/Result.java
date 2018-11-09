package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/29.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private Res res;
    private ExpressInfoResult result;
}
