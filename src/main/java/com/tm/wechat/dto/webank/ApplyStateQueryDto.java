package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子签约查询实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyStateQueryDto {
    @JsonProperty
    private String WXID; //客户端唯一标识
    @JsonProperty
    private String TXN_ID; //交易类型

    @JsonIgnore
    public String getWXID() {
        return WXID;
    }
    @JsonIgnore
    public void setWXID(String WXID) {
        this.WXID = WXID;
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
