package com.tm.wechat.dto.approval.xw_Bank;

import lombok.Data;

import java.util.List;

/**
 * @program: TMWeChat
 * @description: 新网查询预审批结果类
 * @author: ChengQC
 * @create: 2018-10-22 10:49
 **/
@Data
public class XW_QueryFinancePreApplyResultDto {
        private List<String> queryList;//新网查询预审批结果列表
}