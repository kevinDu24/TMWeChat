package com.tm.wechat.dto.approval;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/24.
 */
@Data
public class SubmitProveDto {
    private String[] caseProveUrl;//法院结案证明
    private String[] courtVerdictUrl; //法院判决书
}

