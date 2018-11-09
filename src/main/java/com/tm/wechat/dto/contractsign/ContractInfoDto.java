package com.tm.wechat.dto.contractsign;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/3/19.
 */
@Data
public class ContractInfoDto {


    private String isICBC; //是否是工行产品

    private List<ApplicantContractDto> applicant;//申请人信息

    private List<ApplicantContractDto> joint;//共申人信息

    private List<ApplicantContractDto> guarantee;//担保人信息

}
