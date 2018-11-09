package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/8/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoSignAccountDto {

    private String applyNum;//申请编号

    private String signNum;//签约单号

    private String userName; //用户名

    private String createTime; //创建时间

    private String longitude; //经度

    private String latitude; //纬度

}
