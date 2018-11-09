package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 合同协议实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractBaseDto {
    @JsonProperty
    private String CONTRACT_NAME; //合同名称
    @JsonProperty
    private String CONTRACT_VER; //合同版本号
    @JsonProperty
    private String CHECK_TIME; //勾选时间

    public ContractBaseDto(String CONTRACT_NAME, String CONTRACT_VER, String CHECK_TIME) {
        this.CONTRACT_NAME = CONTRACT_NAME;
        this.CONTRACT_VER = CONTRACT_VER;
        this.CHECK_TIME = CHECK_TIME;
    }

    public ContractBaseDto(ProtocolInfoDto protocolInfoDto, String CHECK_TIME) {
        this.CONTRACT_NAME = protocolInfoDto.getName();
        this.CONTRACT_VER = protocolInfoDto.getVersion();
        this.CHECK_TIME = CHECK_TIME;
    }

    public ContractBaseDto() {

    }
    @JsonIgnore
    public String getCONTRACT_NAME() {
        return CONTRACT_NAME;
    }
    @JsonIgnore
    public void setCONTRACT_NAME(String CONTRACT_NAME) {
        this.CONTRACT_NAME = CONTRACT_NAME;
    }
    @JsonIgnore
    public String getCONTRACT_VER() {
        return CONTRACT_VER;
    }
    @JsonIgnore
    public void setCONTRACT_VER(String CONTRACT_VER) {
        this.CONTRACT_VER = CONTRACT_VER;
    }
    @JsonIgnore
    public String getCHECK_TIME() {
        return CHECK_TIME;
    }
    @JsonIgnore
    public void setCHECK_TIME(String CHECK_TIME) {
        this.CHECK_TIME = CHECK_TIME;
    }
}
