package com.tm.wechat.service;

import com.tm.wechat.dao.LoginRecordRepository;
import com.tm.wechat.dao.PushRecordRepository;
import com.tm.wechat.domain.LoginRecord;
import com.tm.wechat.domain.PushRecord;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.push.PushMessageDto;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.push.PushCastEnum;
import com.tm.wechat.utils.push.UmengPushUtils;
import com.tm.wechat.utils.push.android.UPushResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by pengchao on 2018/6/21.
 */
@Service
public class PushMessageService {

    @Autowired
    private LoginRecordRepository loginRecordRepository;

    @Autowired
    private PushRecordRepository pushRecordRepository;


    /**
     * 消息推送
     * @return
     */
    public ResponseEntity<Message> push(PushMessageDto pushMessageDto) {
        String deviceToken = "";
        String client = "";
        if(CommonUtils.isNull(pushMessageDto.getMessageType()) || CommonUtils.isNull(pushMessageDto.getTitle()) ||  CommonUtils.isNull(pushMessageDto.getContent())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "推送失败,缺少必要参数"), HttpStatus.OK);
        }
        try {
            //单播查找此用户名和客户标识下最新的登录历史记录的数据。
            if(PushCastEnum.UNICAST.getCode().equals(pushMessageDto.getMessageType())){
                if((CommonUtils.isNull(pushMessageDto.getUserName()))){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "推送失败,单播用户名不可为空"), HttpStatus.OK);
                }
                LoginRecord newestLoginRecord =
                        loginRecordRepository.findTop1ByUserNameAndCustomerOrderByTimeDesc(pushMessageDto.getUserName(), "taimeng");
                if(newestLoginRecord != null){
                    client = newestLoginRecord.getDeviceType();
                    deviceToken = newestLoginRecord.getDeviceToken();
                }
                if((CommonUtils.isNull(client) || CommonUtils.isNull(deviceToken))){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "推送失败,未查询到设备"), HttpStatus.OK);
                }
            }
            UPushResultDto uPushResultDto = UmengPushUtils.push(pushMessageDto.getMessageType(),client,
                    deviceToken,null,pushMessageDto.getTitle(),pushMessageDto.getContent());
            PushRecord pushRecord = new PushRecord(uPushResultDto, pushMessageDto);
            pushRecord.setClientType(client);
            pushRecord.setDeviceToken(deviceToken);
            pushRecordRepository.save(pushRecord);
            if("SUCCESS".equals(uPushResultDto.getRet())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  uPushResultDto.getData().getError_msg()), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "推送异常"), HttpStatus.OK);
        }
    }


    /**
     * 消息推送状态查询
     * @param
     * @return
     */
    public ResponseEntity<Message> querySendStatus(String iosTaskId, String androidTaskId) {
        try {
            UPushResultDto uPushResultDto = UmengPushUtils.queryStatus(iosTaskId, androidTaskId);
            //如果是返回状态200，设为成功、否则失败
            if("SUCCESS".equals(uPushResultDto.getRet())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, uPushResultDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "查询推送状态失败", uPushResultDto), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,  "查询推送状态异常"), HttpStatus.OK);
        }
    }
}
