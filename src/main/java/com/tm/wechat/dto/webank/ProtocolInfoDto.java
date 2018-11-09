package com.tm.wechat.dto.webank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 合同协议配置类
 * Created by zcHu on 18/1/6.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProtocolInfoDto {
    private String name; //合同名称
    private String version; //合同版本号
}
