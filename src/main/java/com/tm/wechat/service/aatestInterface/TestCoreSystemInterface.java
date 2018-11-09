package com.tm.wechat.service.aatestInterface;

import com.tm.wechat.dto.approval.xw_Bank.XW_QueryFinancePreApplyResultDto;
import com.tm.wechat.dto.approval.xw_Bank.XW_ApprovalSubmitDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *  @author ChengQiChuan
 *  @date 2018/10/22 10:43
 *  Description: 主系统调用测试接口
 */

@FeignClient(name = "coreSystemInterface", url = "http://116.236.234.246:18080/XFTM_ZL/")
//@FeignClient(name = "coreSystemInterface", url = "${request.coreApplyServerUrl}")
public interface TestCoreSystemInterface {


}
