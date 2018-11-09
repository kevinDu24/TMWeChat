package com.tm.wechat.dto.sign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * Created by pengchao on 2018/3/14.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplyContractLocalInfoDto {

    private String uniqueMark;

    private String name;

    private String applyNum;

    private String createTime;

    private String createUser;
    
    private String passTime; //通过时间

    private String userName; //用户名

    private String state; //合同状态

    private String contractState; //合同生成状态（0:未生成,1:已生成）

    private String signState; //签约状态

    private String productType;//产品类型

    private String bankVerifyState; //银行卡验证状态

    public ApplyContractLocalInfoDto() {

    }
}
