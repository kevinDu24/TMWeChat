package com.tm.wechat.dto.requestpayment;

import com.tm.wechat.dto.approval.ApprovalCountDto;
import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 首页任务列表数量
 * @author: ChengQC
 * @create: 2018-09-27 18:16
 **/
@Data
public class HomeListNumDto {

    /**
     * 在线申请数量
     */
    ApprovalCountDto approvalCountDto;

    /**
     * GPS邀约数量
     */
    private int inviteNum;

    /**
     * 签约数量
     */
    private int signedNum;

    /**
     * 请款数量
     */
    private int requestPayoutNum;


}