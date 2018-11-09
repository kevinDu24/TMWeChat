package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * Created by pengchao on 2017/7/13.
 */
@Data
public class SpecificTypeDto {

    @JsonProperty("BACPBZ")
    private String bacpbz;//产品描述

    @JsonProperty("BACPMC")
    private String bacpmc;//产品名称

    @JsonProperty("ID")
    private String id;//产品ID

}
