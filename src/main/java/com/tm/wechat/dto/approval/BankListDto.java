package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * 银行列表实体类
 *
 * Created by huzongcheng on 18/1/9.
 */
@Data
public class BankListDto {
    private String bank; //银行名称

    public BankListDto(String objs) {
        this.bank = objs == null ? "" : objs;
    }
}
