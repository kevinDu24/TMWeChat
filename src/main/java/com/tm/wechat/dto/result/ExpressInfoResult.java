package com.tm.wechat.dto.result;

import com.tm.wechat.dto.icbc.ExpressInfoDto;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/7/17.
 */
@Data
public class ExpressInfoResult {
    private String resultMsg;
    private String isSuccess;
    private List<ExpressInfoDto> resultCode;
}
