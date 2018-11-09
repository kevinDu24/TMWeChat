package com.tm.wechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tm.wechat.dto.approval.BankCardVerificationInfoDto;
import com.tm.wechat.dto.communication.OnlineCommunicationListDto;
import com.tm.wechat.dto.communication.OnlineMessageListDto;
import com.tm.wechat.dto.contractsign.ContractDto;
import com.tm.wechat.dto.contractsign.ContractInfoDto;
import com.tm.wechat.dto.core.result.ProductDetailDto;
import com.tm.wechat.dto.requestpayment.GetPleaseDocumentDto;
import com.tm.wechat.dto.requestpayment.RequestPaymentListDto;
import com.tm.wechat.dto.sign.*;
import com.tm.wechat.dto.sysUser.UserProductInfoDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/9/29.
 */
@Data
public class CoreResult {

    @JsonIgnore
    private String modifyUserInfo;

    private CoreRes result;

    private List<Map> applyResults;

    private ProductDetailDto rzcpxxRe;

    private ContractInfoDto orderInfo;

    private List<ApplyContractLocalInfoDto> signList;//待签约列表

    private List<RequestPaymentListDto> payoutList;//请款列表
    
    private ContractDto contractrInfo; //合同信息

    private List<UserProductInfoDto> productInfoLylist; //可选产品列表

    private CarSignInfoDto signedVehicle;//查询的补全车辆信息

    private BankCardSignInfoDto bankCard;//查询的还款卡信息

    private List<OnlineCommunicationListDto> onLineLys;//在线沟通列表

    private List<OnlineMessageListDto> messageObjs;//在线沟通消息列表

    private List<OnlineMessageListDto> attachmentObjs;//在线沟通附件列表

    private List<GetPleaseDocumentDto> fileList;//请款所需附件列表

    private FourFactorVerificationInfoDto elements;//查询用户四要素信息

    private AddressInputInfoDto clientInfo;//查询用户收货地址信息

    private BankCardVerificationInfoDto cardVerification;//放款卡核实信息

    private String sfxymq;//是否需要签约

    private String signState;//视频面签状态

}
