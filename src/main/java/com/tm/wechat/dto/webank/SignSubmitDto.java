package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tm.wechat.config.WzProperties;
import com.tm.wechat.utils.Utils;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.WzSubmitType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 电子签约提交实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignSubmitDto {
    @JsonProperty
    private String BASQBH; //申请编号
    @JsonProperty
    private String TXN_ID; //交易类型
    @JsonProperty
    private String WX_OPENID; //微信OpenId
    @JsonProperty
    private String APP_TYPE; //应用提交类型
    @JsonProperty
    private String APP_ID; //应用ID
    @JsonProperty
    private String WX_UNION_ID; //微信唯一id
    @JsonProperty
    private String CHECK_TYPE; //核查方式
    @JsonProperty
    private String SIGN_DATE; //签约日
    @JsonProperty
    private String SIGN_CHANNEL; //签署渠道（新增）
    @JsonProperty
    private String CONFIRM_LOAN_TIME; //确认借据时间
    @JsonProperty
    private String CLICK_APPLY_LIMIT_TIME; //点击“申请额度”时间
    @JsonProperty
    private String CLICK_SMS_TIME; //点击“获取验证码”时间
    @JsonProperty
    private String SYS_SEND_SMS_TIME; //系统发送验证码时间
    @JsonProperty
    private String CHECK_SMS_MOBILE; //接收验证码手机号码
    @JsonProperty
    private String CHECK_SMS_SUC_TIME; //验短通过时间
    @JsonProperty
    private String CHECK_PWD_SUC_TIME; //验密通过时间
    @JsonProperty
    private String APPLY_TIME; //提交申请时间
    @JsonProperty
    private String TERMINAL_NUM; //终端号码
    @JsonProperty
    private String TERMINAL_NAME; //终端名称
    @JsonProperty
    private String LOCATION_TYPE; //LBS类型
    @JsonProperty
    private String LOCATION_DATA; //LBS数据
    @JsonProperty
    private String NETWORT_TYPE; //网络类型
    @JsonProperty
    private List<ContractBaseDto> CONTRACT_BASE; //合同协议
    @JsonProperty
    private String IP; // ip
    @JsonProperty
    private String CHANNEL;//提交渠道



    public SignSubmitDto(SignSubmitWebDto signSubmitWebDto, WzProperties wzProperties) {
        Date nowDate = new Date();
        String date1 = Utils.getStrDate(nowDate,Utils.yyyymmdd);
        String date2 = Utils.getStrDate(nowDate,Utils.yyyymmddhhmmss);
        this.BASQBH = signSubmitWebDto.getApplyNum();
        this.TXN_ID = WzSubmitType.SUBMIT.code();
        this.APP_TYPE = wzProperties.getAppType();
        this.APP_ID = signSubmitWebDto.getAppId();
        this.SIGN_DATE = date1;
        this.SIGN_CHANNEL = CommonUtils.signChannel;
        this.CONFIRM_LOAN_TIME = date2;
        this.CLICK_APPLY_LIMIT_TIME = date2;
        this.CLICK_SMS_TIME = date2;
        this.SYS_SEND_SMS_TIME = date2;
        this.CHECK_SMS_MOBILE = signSubmitWebDto.getPhoneNum();
        this.CHECK_SMS_SUC_TIME = date2;
        this.CHECK_PWD_SUC_TIME = date2;
        this.APPLY_TIME = date2;
        this.LOCATION_TYPE = signSubmitWebDto.getLocationType();
        this.LOCATION_DATA = signSubmitWebDto.getLocationData();
        this.IP = signSubmitWebDto.getIp();
        this.CONTRACT_BASE = buildContract(wzProperties, date2);
        this.CHANNEL = CommonUtils.CHANNEL;
        this.NETWORT_TYPE = wzProperties.getNetworkType();
    }

    private List<ContractBaseDto> buildContract(WzProperties wzProperties, String time) {
        List<ContractBaseDto> contractList = new ArrayList();
        ContractBaseDto dto = new ContractBaseDto(wzProperties.getSign(), time);
        ContractBaseDto sign_dk = new ContractBaseDto(wzProperties.getSign_dk(), time);//微众委托扣款协议
        contractList.add(sign_dk);
        contractList.add(dto);
        return contractList;
    }
    @JsonIgnore
    public String getCHANNEL() {
        return CHANNEL;
    }
    @JsonIgnore
    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }
    @JsonIgnore
    public String getBASQBH() {
        return BASQBH;
    }
    @JsonIgnore
    public void setBASQBH(String BASQBH) {
        this.BASQBH = BASQBH;
    }
    @JsonIgnore
    public String getTXN_ID() {
        return TXN_ID;
    }
    @JsonIgnore
    public void setTXN_ID(String TXN_ID) {
        this.TXN_ID = TXN_ID;
    }
    @JsonIgnore
    public String getWX_OPENID() {
        return WX_OPENID;
    }
    @JsonIgnore
    public void setWX_OPENID(String WX_OPENID) {
        this.WX_OPENID = WX_OPENID;
    }
    @JsonIgnore
    public String getAPP_TYPE() {
        return APP_TYPE;
    }
    @JsonIgnore
    public void setAPP_TYPE(String APP_TYPE) {
        this.APP_TYPE = APP_TYPE;
    }
    @JsonIgnore
    public String getAPP_ID() {
        return APP_ID;
    }
    @JsonIgnore
    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }
    @JsonIgnore
    public String getWX_UNION_ID() {
        return WX_UNION_ID;
    }
    @JsonIgnore
    public void setWX_UNION_ID(String WX_UNION_ID) {
        this.WX_UNION_ID = WX_UNION_ID;
    }
    @JsonIgnore
    public String getCHECK_TYPE() {
        return CHECK_TYPE;
    }
    @JsonIgnore
    public void setCHECK_TYPE(String CHECK_TYPE) {
        this.CHECK_TYPE = CHECK_TYPE;
    }
    @JsonIgnore
    public String getSIGN_DATE() {
        return SIGN_DATE;
    }
    @JsonIgnore
    public void setSIGN_DATE(String SIGN_DATE) {
        this.SIGN_DATE = SIGN_DATE;
    }
    @JsonProperty
    public String getSIGN_CHANNEL() {
        return SIGN_CHANNEL;
    }
    @JsonIgnore
    public void setSIGN_CHANNEL(String SIGN_CHANNEL) {
        this.SIGN_CHANNEL = SIGN_CHANNEL;
    }
    @JsonIgnore
    public String getCONFIRM_LOAN_TIME() {
        return CONFIRM_LOAN_TIME;
    }
    @JsonIgnore
    public void setCONFIRM_LOAN_TIME(String CONFIRM_LOAN_TIME) {
        this.CONFIRM_LOAN_TIME = CONFIRM_LOAN_TIME;
    }
    @JsonIgnore
    public String getCLICK_APPLY_LIMIT_TIME() {
        return CLICK_APPLY_LIMIT_TIME;
    }
    @JsonIgnore
    public void setCLICK_APPLY_LIMIT_TIME(String CLICK_APPLY_LIMIT_TIME) {
        this.CLICK_APPLY_LIMIT_TIME = CLICK_APPLY_LIMIT_TIME;
    }
    @JsonIgnore
    public String getCLICK_SMS_TIME() {
        return CLICK_SMS_TIME;
    }
    @JsonIgnore
    public void setCLICK_SMS_TIME(String CLICK_SMS_TIME) {
        this.CLICK_SMS_TIME = CLICK_SMS_TIME;
    }
    @JsonIgnore
    public String getSYS_SEND_SMS_TIME() {
        return SYS_SEND_SMS_TIME;
    }
    @JsonIgnore
    public void setSYS_SEND_SMS_TIME(String SYS_SEND_SMS_TIME) {
        this.SYS_SEND_SMS_TIME = SYS_SEND_SMS_TIME;
    }
    @JsonIgnore
    public String getCHECK_SMS_MOBILE() {
        return CHECK_SMS_MOBILE;
    }
    @JsonIgnore
    public void setCHECK_SMS_MOBILE(String CHECK_SMS_MOBILE) {
        this.CHECK_SMS_MOBILE = CHECK_SMS_MOBILE;
    }
    @JsonIgnore
    public String getCHECK_SMS_SUC_TIME() {
        return CHECK_SMS_SUC_TIME;
    }
    @JsonIgnore
    public void setCHECK_SMS_SUC_TIME(String CHECK_SMS_SUC_TIME) {
        this.CHECK_SMS_SUC_TIME = CHECK_SMS_SUC_TIME;
    }
    @JsonIgnore
    public String getCHECK_PWD_SUC_TIME() {
        return CHECK_PWD_SUC_TIME;
    }
    @JsonIgnore
    public void setCHECK_PWD_SUC_TIME(String CHECK_PWD_SUC_TIME) {
        this.CHECK_PWD_SUC_TIME = CHECK_PWD_SUC_TIME;
    }
    @JsonIgnore
    public String getAPPLY_TIME() {
        return APPLY_TIME;
    }
    @JsonIgnore
    public void setAPPLY_TIME(String APPLY_TIME) {
        this.APPLY_TIME = APPLY_TIME;
    }
    @JsonIgnore
    public String getTERMINAL_NUM() {
        return TERMINAL_NUM;
    }
    @JsonIgnore
    public void setTERMINAL_NUM(String TERMINAL_NUM) {
        this.TERMINAL_NUM = TERMINAL_NUM;
    }
    @JsonIgnore
    public String getTERMINAL_NAME() {
        return TERMINAL_NAME;
    }
    @JsonIgnore
    public void setTERMINAL_NAME(String TERMINAL_NAME) {
        this.TERMINAL_NAME = TERMINAL_NAME;
    }
    @JsonIgnore
    public String getLOCATION_TYPE() {
        return LOCATION_TYPE;
    }
    @JsonIgnore
    public void setLOCATION_TYPE(String LOCATION_TYPE) {
        this.LOCATION_TYPE = LOCATION_TYPE;
    }
    @JsonIgnore
    public String getLOCATION_DATA() {
        return LOCATION_DATA;
    }
    @JsonIgnore
    public void setLOCATION_DATA(String LOCATION_DATA) {
        this.LOCATION_DATA = LOCATION_DATA;
    }
    @JsonIgnore
    public String getNETWORT_TYPE() {
        return NETWORT_TYPE;
    }
    @JsonIgnore
    public void setNETWORT_TYPE(String NETWORT_TYPE) {
        this.NETWORT_TYPE = NETWORT_TYPE;
    }
    @JsonIgnore
    public List<ContractBaseDto> getCONTRACT_BASE() {
        return CONTRACT_BASE;
    }
    @JsonIgnore
    public void setCONTRACT_BASE(List<ContractBaseDto> CONTRACT_BASE) {
        this.CONTRACT_BASE = CONTRACT_BASE;
    }
    @JsonIgnore
    public String getIP() {
        return IP;
    }
    @JsonIgnore
    public void setIP(String IP) {
        this.IP = IP;
    }
}
