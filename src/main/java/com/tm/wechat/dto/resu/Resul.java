package com.tm.wechat.dto.resu;

import com.tm.wechat.dto.approval.FinancePreApplyResultDto;
import lombok.Data;

/**
 * Created by HJYang on 2016/10/11.
 */
@Data
public class Resul {

    private Resu result;
    private FinancePreApplyResultDto financePreApplyResultDto;
}