package com.tm.wechat.service.sign;

import com.tm.wechat.dao.sign.GpsInfoRepository;
import com.tm.wechat.domain.GpsInfo;
import com.tm.wechat.dto.contractsign.GpsInstallInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.consts.ContractSignStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by pengchao on 2018/3/21.
 */
@Service
public class GpsInfoService {

    @Autowired
    GpsInfoRepository gpsInfoRepository;

    /**
     * 提交GPS安装信息
     * @param gpsInstallInfoDto
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> submitGpsInfo(GpsInstallInfoDto gpsInstallInfoDto, String applyNum){
        GpsInfo gpsInfo = gpsInfoRepository.findByApplyNum(applyNum);
        if(gpsInfo != null){
            BeanUtils.copyProperties(gpsInstallInfoDto, gpsInfo);
            //更新信息为已提交
            gpsInfo.setState(ContractSignStatus.SUBMIT.code());
            gpsInfoRepository.save(gpsInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "GPS安装信息提交失败!"), HttpStatus.OK);
    }


    /**
     * 查询GPS安装信息
     * @param applyNum 申请编号
     * @return
     */
    public ResponseEntity<Message> getGpsInfo(String applyNum){
        GpsInstallInfoDto gpsInstallInfoDto = new GpsInstallInfoDto();
        GpsInfo gpsInfo = gpsInfoRepository.findByApplyNum(applyNum);
        if(gpsInfo != null){
            BeanUtils.copyProperties(gpsInfo, gpsInstallInfoDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, gpsInstallInfoDto), HttpStatus.OK);
    }
}
