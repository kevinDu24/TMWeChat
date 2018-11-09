package com.tm.wechat.dto.communication;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/5/14.
 */
@Data
public class PostOnlineCommunicationDto {
    
    private String applyNum; //申请编号
    
    private String loginUser; //当前登录人员

    private String text; //消息内容

    private List<CommunicationFileDto> fileList;//附件list

}
