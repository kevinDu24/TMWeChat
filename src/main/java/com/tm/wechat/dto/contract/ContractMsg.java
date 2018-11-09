package com.tm.wechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractMsg {
    private Object contractinfo;
    private Object contractrepayplaninfo;
}
