package com.tm.wechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.wechat.dto.push.PushMessageDto;
import com.tm.wechat.utils.push.android.UPushResultDto;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2018/6/21.
 * 友盟消息推送表（目前没用）
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushRecord {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String state; //推送状态

    private String clientType;//3:android,4:ios

    private String userName;//推送的用户名

    private String deviceToken;//推送的设备

    private String messageType;//推送类型；1：单播，2：广播

    private String title;//推送标题

    private String content;//推送内容

    private String errorMsg; //错误msg

    private String errorCode; //错误码

    private String androidTaskId; //推送id

    private String iosTaskId; //推送id

    private String msgId; //消息id


    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;


    public PushRecord() {
    }
    public PushRecord(UPushResultDto uPushResultDto, PushMessageDto pushMessageDto){
        this.state = uPushResultDto.getRet();
        this.errorCode = uPushResultDto.getData().getError_code();
        this.errorMsg = uPushResultDto.getData().getError_msg();
        this.androidTaskId = uPushResultDto.getData().getAndroidTaskId();
        this.iosTaskId = uPushResultDto.getData().getIosTaskId();
        this.msgId = uPushResultDto.getData().getMsg_id();
        this.messageType = pushMessageDto.getMessageType();
        this.title = pushMessageDto.getTitle();
        this.content = pushMessageDto.getContent();
        this.userName = pushMessageDto.getUserName();
    }
}
