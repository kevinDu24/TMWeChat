package com.tm.wechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by pengchao on 2017/7/24.
 */
@Data
public class ContractStateDto {

    @JsonProperty("BASQBH")
    private String basqbh;

    @JsonProperty("BASQXM")
    private String basqxm;

    private List<Map> contractstatelist;
}
