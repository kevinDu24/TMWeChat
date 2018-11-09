package com.tm.wechat.utils.push.android;

import lombok.Data;

/**
 * Created by pengchao on 2018/6/19.
 */
@Data
public class UPushResultDto {

    /**
     * ret : SUCCESS/FAIL
     * data : {"error_msg":"xx","task_id":"xx","error_code":"xx","msg_id":"xx"}
     */
    private String ret;
    private UPushResultDataDto data;

}
