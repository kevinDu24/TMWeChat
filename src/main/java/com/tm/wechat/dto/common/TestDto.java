package com.tm.wechat.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by LEO on 16/10/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDto{
    private List<TestChildDto> hqlhyhlist;
    private String cpxlBycx;
}
