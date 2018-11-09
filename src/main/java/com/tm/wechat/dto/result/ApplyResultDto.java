package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/1/8.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown =true)
public class ApplyResultDto {
    
    private String type; //推送类型
    
    private String uniqueMark; //唯一标识

    private String applyNum; //主系统申请编号

    private String resultReason; //原因

     
}
