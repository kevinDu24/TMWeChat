package com.tm.wechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/7.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductInfo {
    private Object contractInfo;
}
