package com.tm.wechat.dto.baidu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by pengchao on 2018/7/20.
 */
@Data
public class ShortUrlResultDto {

    /**
     * LongUrl : http://wx.xftm.com/#/app/factorVerification?applyNum=38476604
     * ShortUrl : http://dwz.cn/a4JhhGGP
     * ErrMsg :
     * Code : 0
     */
    @JsonProperty("LongUrl")
    private String LongUrl;
    @JsonProperty("ShortUrl")
    private String ShortUrl;
    @JsonProperty("ErrMsg")
    private String ErrMsg;
    @JsonProperty("Code")
    private String Code;

}
