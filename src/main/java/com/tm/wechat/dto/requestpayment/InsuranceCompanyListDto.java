package com.tm.wechat.dto.requestpayment;

import com.tm.wechat.dto.result.Res;
import lombok.Data;

import java.util.List;

/**
 * @program: TMWeChat
 * @description: 保险公司列表
 * @author: ChengQC
 * @create: 2018-09-25 10:04
 **/
@Data
public class InsuranceCompanyListDto {

    private Res result; //状态相应结果集

    private List<InsuranceCompanyInfoDto> insuranceCompanyInfoList; //公司列表

}