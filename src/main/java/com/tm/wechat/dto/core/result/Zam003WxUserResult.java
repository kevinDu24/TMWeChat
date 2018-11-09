package com.tm.wechat.dto.core.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.dto.result.CoreRes;
import lombok.Data;
import java.util.List;

/**
 * Created by yuanzhenxia on 2017/8/7.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Zam003WxUserResult {

    private List<Zam003FirstResult> zam003FirstRe;

    private CoreRes result;
}
