package com.tm.wechat.dto.usedcar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/26.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentifyDriverCardDto {

    private String token;
    private String oper;
    private String driveData;
}
