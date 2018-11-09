package com.tm.wechat.dto.requestpayment;

import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 保险公司列表
 * @author: ChengQC
 * @create: 2018-09-21 16:42
 **/
@Data
public class InsuranceCompanyInfoDto {

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    public InsuranceCompanyInfoDto() {
        super();
    }

    public InsuranceCompanyInfoDto(String i, String str) {
        companyId = i;
        companyName = str;
    }
}