package com.tm.wechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2017/7/24.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractMsgDto {
    private ContractStateDto contractinfo;
}
