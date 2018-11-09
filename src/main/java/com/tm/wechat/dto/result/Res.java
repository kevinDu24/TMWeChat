package com.tm.wechat.dto.result;

import lombok.Data;

/**
 * Created by LEO on 16/9/29.
 */
@Data
public class Res {
    private String resultCode;
    private String msg;
    private Boolean isSuccess;
    private String resultMsg;
}
