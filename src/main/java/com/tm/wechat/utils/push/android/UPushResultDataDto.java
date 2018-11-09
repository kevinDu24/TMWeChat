package com.tm.wechat.utils.push.android;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/6/19.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UPushResultDataDto {
        /**
         * error_msg : xx
         * task_id : xx
         * error_code : xx
         * msg_id : xx
         */
        private String error_msg;
        private String task_id;
        private String error_code;
        private String msg_id;
        private String androidTaskId; //安卓推送id
        private String iosTaskId; //ios推送id
        private String total_count;
        private String open_count;
        private String sent_count;
        private String status;
        private String dismiss_count;

}
