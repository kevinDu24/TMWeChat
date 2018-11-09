package com.tm.wechat.dto.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerInfoDto {
    private String realDriverName;//实际用车人
    private String realDriverMobile;//实际用车人手机号
    private String marriage;//婚姻状况

}
