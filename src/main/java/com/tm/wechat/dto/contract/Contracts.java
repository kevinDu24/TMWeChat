package com.tm.wechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contracts {
    private Object page;
    private List<Object> contractstatelist;
    private List<Map> contractrepayplanlist;
}
