package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/8/28.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoSignStateDto {

    private String sfxymq;//是否需要签约

    private String signState;//视频面签状态


}
