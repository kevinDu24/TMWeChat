package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/9/5.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XftmAppServiceResult {

    private String success;
    private String msg;
    private Object obj;

}
