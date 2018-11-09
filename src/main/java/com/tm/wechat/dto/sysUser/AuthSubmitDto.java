package com.tm.wechat.dto.sysUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by pengchao on 2018/4/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthSubmitDto {
    @JsonProperty
    private String userName; //用户名
    @JsonProperty
    private String activationState; //认证状态
    @JsonProperty
    private String XTCZMC; //姓名
    @JsonProperty
    private String XTSJHM; //手机号
    @JsonProperty
    private String idCardNum; //身份证号码
    @JsonProperty
    private String name; //
    @JsonProperty
    private String phoneNum; //
    @JsonIgnore
    public String getUserName() {
        return userName;
    }
    @JsonIgnore
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @JsonIgnore
    public String getActivationState() {
        return activationState;
    }
    @JsonIgnore
    public void setActivationState(String activationState) {
        this.activationState = activationState;
    }
    @JsonIgnore
    public String getXTCZMC() {
        return XTCZMC;
    }
    @JsonIgnore
    public void setXTCZMC(String XTCZMC) {
        this.XTCZMC = XTCZMC;
    }
    @JsonIgnore
    public String getXTSJHM() {
        return XTSJHM;
    }
    @JsonIgnore
    public void setXTSJHM(String XTSJHM) {
        this.XTSJHM = XTSJHM;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
