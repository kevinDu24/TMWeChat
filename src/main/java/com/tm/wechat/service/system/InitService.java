package com.tm.wechat.service.system;

import com.tm.wechat.dto.init.InitDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.InformationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by LEO on 16/11/2.
 */
@Service
public class InitService {

    @Autowired
    private InformationInterface informationInterface;

    /**
     * app端首页初始化
     * @param bannerNum
     * @param infoNum
     * @return
     */
    public ResponseEntity<Message> init(String headerParam, Integer bannerNum, Integer infoNum, Integer noticeNum){
        ResponseEntity<Message> news = informationInterface.getInfos(headerParam, 1, 1, infoNum);
        ResponseEntity<Message> notice = informationInterface.getInfos(headerParam, 3, 1, noticeNum);
        ResponseEntity<Message> banner = informationInterface.getBanners(headerParam, bannerNum);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, new InitDto(news, notice, banner)), HttpStatus.OK);
    }


    public ResponseEntity<Message> getApplyState(String headerParam){
        String applyState = "false";
//        ResponseEntity<Message> appVersion = informationInterface.getAppVersions(headerParam, 0);
//        String version = (String) ((Map)appVersion.getBody().getData()).get("version");
//        try {
//            int versionValue = Integer.parseInt(version);
//            if(versionValue > 70){
//                applyState = "true";
//            }
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, applyState), HttpStatus.OK);
    }

}
