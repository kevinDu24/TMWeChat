package com.tm.wechat.dto.communication;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2018/5/14.
 */
@Data
public class OnlineMessageListDto {

    private String text;//消息文本

    private String operationTime;//操作时间

    private String operator;//操作人

    private String type;//附件类型

    private String url; //附件url

    private String txUrl;//头像

    private List<CommunicationFileDto> fileList;//附件list

}
