package com.tm.wechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/9/6.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractApplyStateDto {

    private String tq; //天启预审批状态

    private String icbc; //工行预审批状态

    private String webank; //微众银行一审状态
}
