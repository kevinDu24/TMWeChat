package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 电子签约提交实体类(页面用)
 * Created by zcHu on 18/1/6.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignSubmitWebDto {
    private String applyNum; //申请编号
    private String phoneNum; //接收验证码手机号码
    private String terminalNum; //终端号码
    private String terminalName; //终端名称
    private String locationType; //LBS类型
    private String locationData; //LBS数据
    private String netWortType; //网络类型
    private String ip; //ip
    private String appId; //app应用id
    private String msgCode;//短信验证码
}
