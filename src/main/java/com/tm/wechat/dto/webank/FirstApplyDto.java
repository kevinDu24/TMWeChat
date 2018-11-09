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
 * 微众一审实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirstApplyDto {

    @JsonProperty
    private String CHANNEL; //提交渠道
    @JsonProperty
    private String NAME; //姓名
    @JsonProperty
    private String ID_TYPE; //证件提交类型
    @JsonProperty
    private String ID_NO; //证件号码
    @JsonProperty
    private String REG_MOBILE; //注册手机号
    @JsonProperty
    private String WX_OPENID; //微信OpenId
    @JsonProperty
    private String APP_TYPE; //应用提交类型
    @JsonProperty
    private String APP_ID; //应用ID
    @JsonProperty
    private String WX_UNION_ID; //微信唯一id
    @JsonProperty
    private String REG_USERNAME; //平台用户名
    @JsonProperty
    private String REG_USERID; //用户平台id(身份证)
    @JsonProperty
    private String CLICK_APPLY_LIMIT_TIME; //点击“申请额度”时间
    @JsonProperty
    private String CLICK_SMS_TIME; //点击“获取验证码”时间
    @JsonProperty
    private String CHECK_SMS_MOBILE; //接收验证码手机号码
    @JsonProperty
    private String SYS_SEND_SMS_TIME; //系统发送验证码时间
    @JsonProperty
    private String CHECK_SMS_SUC_TIME; //验短通过时间
    @JsonProperty
    private String CHECK_PWD_SUC_TIME; //验密通过时间
    @JsonProperty
    private String APPLY_TIME; //提交申请时间
    @JsonProperty
    private String IP; //IP地址
    @JsonProperty
    private String FP_ZHDM; //FP业务员账号代码
    @JsonProperty
    private String BANK_CARD_USAGE; //卡用途
    @JsonProperty
    private String BANK_CARD_NO; //卡号
    @JsonProperty
    private String BANK_CARD_BRNO; //卡开户行联行号
    @JsonProperty
    private String BANK_CARD_BRNAME; //卡开户行名
    @JsonProperty
    private String BANK_CARD_RESERVE_PHONE; //银行预留手机号
    @JsonProperty
    private String TERMINAL_NUM; //终端号码   null
    @JsonProperty
    private String TERMINAL_NAME; //终端名称  null
    @JsonProperty
    private String LOCATION_TYPE; //LBS提交类型 null
    @JsonProperty
    private String LOCATION_DATA; //LBS数据
    @JsonProperty
    private String NETWORT_TYPE; //网络提交类型
    @JsonProperty
    private List<ContractBaseDto> CONTRACT_BASE; //合同协议
    @JsonProperty
    private String WXID; //微信端唯一标识
    @JsonProperty
    private String TXN_ID; //交易类型

    @JsonProperty
    private String TAXABLE_RESIDENTS; //纳税人类型

    @JsonProperty
    private String MONTHLY_INCOME_RANGE; //月收入范围

    @JsonProperty
    private String CLICK_TAXABLE_TIME; //点击纳税声明时间

    @JsonIgnore
    public String getCHANNEL() {
        return CHANNEL;
    }
    @JsonIgnore
    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    @JsonIgnore
    public String getCLICK_TAXABLE_TIME() {
        return CLICK_TAXABLE_TIME;
    }
    @JsonIgnore
    public void setCLICK_TAXABLE_TIME(String CLICK_TAXABLE_TIME) {
        this.CLICK_TAXABLE_TIME = CLICK_TAXABLE_TIME;
    }

    @JsonIgnore
    public String getMONTHLY_INCOME_RANGE() {
        return MONTHLY_INCOME_RANGE;
    }
    @JsonIgnore
    public void setMONTHLY_INCOME_RANGE(String MONTHLY_INCOME_RANGE) {
        this.MONTHLY_INCOME_RANGE = MONTHLY_INCOME_RANGE;
    }

    @JsonIgnore
    public String getTAXABLE_RESIDENTS() {
        return TAXABLE_RESIDENTS;
    }
    @JsonIgnore
    public void setTAXABLE_RESIDENTS(String TAXABLE_RESIDENTS) {
        this.TAXABLE_RESIDENTS = TAXABLE_RESIDENTS;
    }

    @JsonIgnore
    public String getNAME() {
        return NAME;
    }

    @JsonIgnore
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @JsonIgnore
    public String getID_TYPE() {
        return ID_TYPE;
    }

    @JsonIgnore
    public void setID_TYPE(String ID_TYPE) {
        this.ID_TYPE = ID_TYPE;
    }
    @JsonIgnore
    public String getID_NO() {
        return ID_NO;
    }
    @JsonIgnore
    public void setID_NO(String ID_NO) {
        this.ID_NO = ID_NO;
    }
    @JsonIgnore
    public String getREG_MOBILE() {
        return REG_MOBILE;
    }
    @JsonIgnore
    public void setREG_MOBILE(String REG_MOBILE) {
        this.REG_MOBILE = REG_MOBILE;
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
    public String getREG_USERNAME() {
        return REG_USERNAME;
    }
    @JsonIgnore
    public void setREG_USERNAME(String REG_USERNAME) {
        this.REG_USERNAME = REG_USERNAME;
    }
    @JsonIgnore
    public String getREG_USERID() {
        return REG_USERID;
    }
    @JsonIgnore
    public void setREG_USERID(String REG_USERID) {
        this.REG_USERID = REG_USERID;
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
    public String getIP() {
        return IP;
    }
    @JsonIgnore
    public void setIP(String IP) {
        this.IP = IP;
    }
    @JsonIgnore
    public String getFP_ZHDM() {
        return FP_ZHDM;
    }
    @JsonIgnore
    public void setFP_ZHDM(String FP_ZHDM) {
        this.FP_ZHDM = FP_ZHDM;
    }
    @JsonIgnore
    public String getBANK_CARD_USAGE() {
        return BANK_CARD_USAGE;
    }
    @JsonIgnore
    public void setBANK_CARD_USAGE(String BANK_CARD_USAGE) {
        this.BANK_CARD_USAGE = BANK_CARD_USAGE;
    }
    @JsonIgnore
    public String getBANK_CARD_NO() {
        return BANK_CARD_NO;
    }
    @JsonIgnore
    public void setBANK_CARD_NO(String BANK_CARD_NO) {
        this.BANK_CARD_NO = BANK_CARD_NO;
    }
    @JsonIgnore
    public String getBANK_CARD_BRNO() {
        return BANK_CARD_BRNO;
    }
    @JsonIgnore
    public void setBANK_CARD_BRNO(String BANK_CARD_BRNO) {
        this.BANK_CARD_BRNO = BANK_CARD_BRNO;
    }
    @JsonIgnore
    public String getBANK_CARD_BRNAME() {
        return BANK_CARD_BRNAME;
    }
    @JsonIgnore
    public void setBANK_CARD_BRNAME(String BANK_CARD_BRNAME) {
        this.BANK_CARD_BRNAME = BANK_CARD_BRNAME;
    }
    @JsonIgnore
    public String getBANK_CARD_RESERVE_PHONE() {
        return BANK_CARD_RESERVE_PHONE;
    }
    @JsonIgnore
    public void setBANK_CARD_RESERVE_PHONE(String BANK_CARD_RESERVE_PHONE) {
        this.BANK_CARD_RESERVE_PHONE = BANK_CARD_RESERVE_PHONE;
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



    public FirstApplyDto(BasicInfoDto basicInfoDto, WzProperties wzProperties){
        Date nowDate = new Date();
        String date1 = Utils.getStrDate(nowDate,Utils.yyyymmdd);
        String date2 = Utils.getStrDate(nowDate,Utils.yyyymmddhhmmss);
        this.NAME = basicInfoDto.getName();
        this.ID_TYPE = CommonUtils.idType;
        this.ID_NO = basicInfoDto.getIdCard().toUpperCase();
        this.REG_MOBILE = basicInfoDto.getPhoneNum();
        this.WX_OPENID = basicInfoDto.getOpenId();
        this.APP_TYPE = wzProperties.getAppType();
        this.APP_ID = basicInfoDto.getAppId();
        this.REG_USERNAME = basicInfoDto.getName();
        this.REG_USERID = basicInfoDto.getIdCard().toUpperCase();
        this.CLICK_APPLY_LIMIT_TIME = date2;
        this.CLICK_SMS_TIME = date2;
        this.SYS_SEND_SMS_TIME = date2;
        this.CHECK_SMS_MOBILE = date2;
        this.CHECK_SMS_SUC_TIME = date2;
        this.CHECK_PWD_SUC_TIME = date2;
        this.APPLY_TIME = date2;
        this.IP = basicInfoDto.getIp();
        this.FP_ZHDM = basicInfoDto.getFpName();
        this.BANK_CARD_USAGE = wzProperties.getBankCardUsage();
        this.BANK_CARD_NO = basicInfoDto.getBankCardNum();
        this.BANK_CARD_BRNO = basicInfoDto.getBankNum();
        this.BANK_CARD_BRNAME = basicInfoDto.getBank();
        this.BANK_CARD_RESERVE_PHONE = basicInfoDto.getPhoneNum();
        this.LOCATION_DATA = basicInfoDto.getLocationData();
        this.CONTRACT_BASE = buildContract(wzProperties, date2);
        this.TXN_ID = WzSubmitType.NEW.code();
        this.TAXABLE_RESIDENTS = basicInfoDto.getStatement();
        this.MONTHLY_INCOME_RANGE = basicInfoDto.getMonthlyIncome();
        this.CLICK_TAXABLE_TIME = date2;
        this.CHANNEL = CommonUtils.CHANNEL;
        this.NETWORT_TYPE = wzProperties.getNetworkType();
    }

    public FirstApplyDto() {
    }

    private List<ContractBaseDto> buildContract(WzProperties wzProperties, String time) {
        List<ContractBaseDto> contractList = new ArrayList();
        ContractBaseDto dto = new ContractBaseDto();
        ContractBaseDto rh = new ContractBaseDto(wzProperties.getRh(), time);//个人征信授权书
        ContractBaseDto iu = new ContractBaseDto(wzProperties.getIu(), time);//个人信息使用授权
        ContractBaseDto ACCOUNT = new ContractBaseDto(wzProperties.getACCOUNT(), time);//微众银行个人电子账户服务协议
        contractList.add(rh);
        contractList.add(iu);
        contractList.add(ACCOUNT);
        return contractList;
    }
}
