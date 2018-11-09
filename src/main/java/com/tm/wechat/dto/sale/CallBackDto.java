package com.tm.wechat.dto.sale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by PengChao on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallBackDto<Object> {
    Object data;
    private Object bam054;
}
