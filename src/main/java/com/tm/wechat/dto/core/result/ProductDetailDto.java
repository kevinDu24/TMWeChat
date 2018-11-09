package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by pengchao on 2017/12/22.
 */
@Data
public class ProductDetailDto {
    @JsonProperty("BZJLQJ")
    private String bzjlqj;

    @JsonProperty("FWFLQJ")
    private String fwflqj;

    @JsonProperty("RSYWDWSZ")
    private String rsywdwsz;

    @JsonProperty("RZQXSZ")
    private String rzqxsz;

    @JsonProperty("RZXMSZ")
    private String rzxmsz;

    @JsonProperty("SFBLQJ")
    private String sfblqj;

    @JsonProperty("SFJEQJ")
    private String sfjeqj;

    @JsonProperty("SXFLQJ")
    private String sxflqj;

    @JsonProperty("WFBLQJ")
    private String wfblqj;

    @JsonProperty("WFJEQJ")
    private String wfjeqj;

    @JsonProperty("ZLGLFLQJ")
    private String zlglflqj;
}
