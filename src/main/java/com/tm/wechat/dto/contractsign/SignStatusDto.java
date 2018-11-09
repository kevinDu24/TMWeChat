package com.tm.wechat.dto.contractsign;

import lombok.Data;

/**
 * Created by pengchao on 2018/3/15.
 */
@Data
public class SignStatusDto {


    private String isICBC; //是否是工行产品 是，否

    private String applyContractStatus; //申请人合同状态 0：未完善, 1:已完善, null: 未完善

    private String jointContractStatus; //共申人合同状态 0：未完善, 1:已完善, null: 无

    private String guaranteeContractStatus; //担保人合同状态 0：未完善, 1:已完善, null: 无

    private String gpsInfoStatus; //gps安装信息状态 0：未完善, 1:已完善, null: 未完善

    private ApplicantContractDto applicantInfo;//申请人信息

    private ApplicantContractDto jointInfo;//共申人信息

    private ApplicantContractDto guaranteeInfo;//担保人信息

    public SignStatusDto() {
    }

    public SignStatusDto(Object[] objs) {
        this.applyContractStatus = objs[0] == null ? "" : objs[0].toString();
        this.isICBC = objs[1] == null ? "" : objs[1].toString();
        this.jointContractStatus = objs[2] == null ? "" : objs[2].toString();
        this.guaranteeContractStatus = objs[3] == null ? "" : objs[3].toString();
        this.applicantInfo = new ApplicantContractDto(objs[4] == null ? "" : objs[4].toString(),objs[5] == null ? "" : objs[5].toString());
        this.jointInfo = new ApplicantContractDto(objs[6] == null ? "" : objs[6].toString(),objs[7] == null ? "" : objs[7].toString());
        this.guaranteeInfo = new ApplicantContractDto(objs[8] == null ? "" : objs[8].toString(),objs[9] == null ? "" : objs[9].toString());
    }

}
