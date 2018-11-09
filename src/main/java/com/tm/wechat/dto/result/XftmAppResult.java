package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/20.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XftmAppResult {
    private String success;
    private String msg;
    private String obj;

}
