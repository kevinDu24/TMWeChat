package com.tm.wechat.service;


import com.tm.wechat.dao.RedisRepository;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sysUser.UserLoginInfoDto;
import com.tm.wechat.utils.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * app系统service
 * Created by zcHu on 2017/8/28.
 */
@Service
public class SystemService {

    @Autowired
    private RedisRepository redisRepository;


    /**
     * 验证接口
     * @param deviceToken
     * @return
     */
    public ResponseEntity<Message> verify(String deviceToken, String userName){
        String key = CommonUtils.loginkey + userName;
        Object result = redisRepository.get(key);
        //账号第一次登录
        if(result == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        UserLoginInfoDto appUserDto = (UserLoginInfoDto)redisRepository.get(key);
        if (appUserDto.getDeviceToken().equals(deviceToken)) {
            redisRepository.save(key, appUserDto, 7 * 24 * 3600);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorCode), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, appUserDto), HttpStatus.OK);
    }


    /**
     * 退出登录
     * @param uniqueMark
     * @return
     */
    public ResponseEntity<Message> loginout(String uniqueMark){
        String [] values = uniqueMark.split(":");
        if(values.length != 2){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorCode), HttpStatus.OK);
        }
        String phoneNum = values[0];
        String uuid = values[1];
        if("".equals(uuid)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorCode), HttpStatus.OK);
        }
        String key = CommonUtils.loginkey + phoneNum;
        Object result = redisRepository.get(key);
//        if(result == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorCode), HttpStatus.OK);
//        } else {
//            redisRepository.delete(key);
//        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

}
