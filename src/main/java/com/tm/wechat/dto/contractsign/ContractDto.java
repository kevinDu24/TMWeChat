package com.tm.wechat.dto.contractsign;

import com.tm.wechat.dto.sign.FileUrlDto;
import com.tm.wechat.dto.sign.SignFileUrlDto;
import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/4/17.
 */
@Data
public class ContractDto {

    private List<SignFileUrlDto> applicant;//申请人合同信息

    private List<SignFileUrlDto> joint;//共申人合同信息

    private List<SignFileUrlDto> guarantee;//担保人合同信息
}
