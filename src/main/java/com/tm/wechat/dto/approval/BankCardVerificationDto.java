package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2018/6/30.
 */
@Data
public class BankCardVerificationDto {

    private String bankCardNum; //银行卡号

    private String userName;//用户姓名

}
