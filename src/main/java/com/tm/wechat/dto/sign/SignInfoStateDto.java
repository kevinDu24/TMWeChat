package com.tm.wechat.dto.sign;

import lombok.Data;

/**
 * Created by pengchao on 2018/4/20.
 */
@Data
public class SignInfoStateDto {

    private String applyNum; //申请编号

    private String signCarInfoState; //签约车辆信息 0,null,""：未完善,1:已完善

    private String signBankCardInfoState; //银行卡验证 0,null,"":未验证, 1:已验证

    public SignInfoStateDto() {
    }

    public SignInfoStateDto(Object[] objs) {
        this.applyNum = objs[0] == null ? "" : objs[0].toString();
        this.signCarInfoState = objs[1] == null ? "" : objs[1].toString();
        this.signBankCardInfoState = objs[2] == null ? "" : objs[2].toString();
    }
}
