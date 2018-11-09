package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子签约查询实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInfoQueryDto {
    @JsonProperty
    private String applyNum; //客户端唯一标识
    @JsonProperty
    private String TXN_ID; //交易类型

    public String getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(String applyNum) {
        this.applyNum = applyNum;
    }

    @JsonIgnore
    public String getTXN_ID() {
        return TXN_ID;
    }
    @JsonIgnore
    public void setTXN_ID(String TXN_ID) {
        this.TXN_ID = TXN_ID;
    }

}
