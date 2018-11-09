package com.tm.wechat.service;

import com.google.common.collect.Maps;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LEO on 16/10/28.
 */
@Service
public class JsonDecoder {

    /**
     * 多属性json解析器
     * @param data
     * @param map
     * @return
     */
    public ResponseEntity<Message> multiPropertiesDecoder(String data, Map<String ,String> map){
        Map<String, String> result = Maps.newHashMap();
        if(!JsonPath.from(data).get("result.isSuccess").equals("true")){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, JsonPath.from(data).get("result.resultMsg")), HttpStatus.OK);
        }
        map.forEach((key, position) -> result.put(key, JsonPath.from(data).get(position)));
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, result), HttpStatus.OK);
    }

    /**
     * 单属性json解析器
     * @param data
     * @param property
     * @return
     */
    public ResponseEntity<Message> singlePropertyDecoder(String data, String property){
        if(!JsonPath.from(data).get("result.isSuccess").equals("true")){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, JsonPath.from(data).get("result.resultMsg")), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, (Object) JsonPath.from(data).get(property)), HttpStatus.OK);
    }
}
