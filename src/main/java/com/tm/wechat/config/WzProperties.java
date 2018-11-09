package com.tm.wechat.config;

import com.tm.wechat.dto.webank.ProtocolInfoDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * Created by zcHu on 18/1/8.
 */
@ConfigurationProperties(prefix = "wz")
@Data
public class WzProperties {
    private String bankCardUsage; //卡用途
    private String appType; //应用提交类型
    private String networkType;//网络类型
    private ProtocolInfoDto rh; //个人征信授权书
    private ProtocolInfoDto iu; //个人信息使用授权
    private ProtocolInfoDto ACCOUNT; //微众银行个人电子账户服务协议
    private ProtocolInfoDto sign; //汽车金融借款合同
    private ProtocolInfoDto dk; //委托扣款协议书
    private ProtocolInfoDto sign_dk; //签约页委托扣款协议书
}
