package com.tm.wechat.dto.communication;

import lombok.Data;

/**
 * Created by pengchao on 2018/5/14.
 */
@Data
public class OnlineCommunicationListDto {

    private String name;//用户姓名

    private String applyNum;//申请编号

    private String operationTime;//操作时间

    private String operator;//操作人
}
