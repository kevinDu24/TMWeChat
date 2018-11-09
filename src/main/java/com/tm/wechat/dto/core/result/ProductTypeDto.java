package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by pengchao on 2017/7/13.
 */
@Data
public class ProductTypeDto {

    @JsonProperty("BADLMC")
    private String badlmc;//产品类型名称

    @JsonProperty("ID")
    private String id;//产品类型id
}
