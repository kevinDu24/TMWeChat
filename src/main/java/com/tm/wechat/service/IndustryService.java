package com.tm.wechat.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tm.wechat.dao.RedisRepository;
import com.tm.wechat.dto.industry.IndustryDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEO on 16/11/2.
 */
@Service
public class IndustryService {
    @Value("classpath:static/json/industry.json")
    private Resource industry;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private Gson gson;

    public ResponseEntity<Message> getIndustryList(){
        List<IndustryDto> industryDtos = (List<IndustryDto>) redisRepository.get("industries");
        if(null == industryDtos || industryDtos.isEmpty()){
            Type listType = new TypeToken<ArrayList<IndustryDto>>(){}.getType();
            try {
                industryDtos = gson.fromJson(IOUtils.toString(industry.getInputStream()), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            redisRepository.save("industries", industryDtos);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, industryDtos), HttpStatus.OK);
    }
}
