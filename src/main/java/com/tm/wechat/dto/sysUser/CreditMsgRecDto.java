package com.tm.wechat.dto.sysUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by LEO on 16/9/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditMsgRecDto {
    private String userId;
    private Integer type;
    private String idCardNum;
    private String name;
    private String phone;
    private String idCardPostiveImg;
    private String idCardOppositeImg;
    private String idCardComposeImg;
    private String faceImg;
    private String authBookImg;
    private String holdAuthBookImg;
}
