package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/4/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicInfoAuthDto {

    private String applyNum; //申请编号

    private String phoneNum; //手机号

    private String msgCode; //短信验证码

    private String uniqueMark; //唯一标识

}
