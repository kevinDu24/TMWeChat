package com.tm.wechat.service.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dao.ApplyInfoNewRepository;
import com.tm.wechat.dao.ApplyInfoRepository;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.domain.ApplyInfo;
import com.tm.wechat.domain.ApplyInfoNew;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.dto.communication.CommunicationFileDto;
import com.tm.wechat.dto.communication.OnlineCommunicationListDto;
import com.tm.wechat.dto.communication.OnlineMessageListDto;
import com.tm.wechat.dto.communication.PostOnlineCommunicationDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.resu.Resul;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.service.applyOnline.ApplyService;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.ContractSignStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by pengchao on 2018/5/14.
 */
@Service
public class OnlineCommunicationService extends BaseService {

    @Autowired
    private CoreSystemInterface coreSystemInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private ApplyInfoRepository applyInfoRepository;

    @Autowired
    private ApplyInfoNewRepository applyInfoNewRepository;


    /**
     * 查询在线沟通列表
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getOnlineCommunicationJK(String userName, String condition){
        if(condition == null){
            condition = "";
        }
        List<OnlineCommunicationListDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.getOnlineCommunicationJK("getOnlineCommunicationJK", condition, userName);
            if(coreResult == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, "未查询到数据"), HttpStatus.OK);
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getOnLineLys();
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }

    /**
     * 查询在线沟通消息历史
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> historyMessageQuery(String applyNum){
        List<OnlineMessageListDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.historyMessageQuery("HistoryMessageQuery", applyNum);
            String coreResult1 = "{\n" +
                    "\t\"messageObjs\": [{\n" +
                    "\t\t\"fileList\": [{\n" +
                    "\t\t\t\t\"type\": \"驾驶证\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/drivingLicence/20180601/4b6837c0-6d1a-4f69-8658-31da90c11e6f.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"身份证\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/idCard/20180601/12aaceda-5b73-4c88-a87f-0a0fdae1af2e.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"身份证\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/idCard/20180601/95af2d6e-73ef-4802-84a6-aa2feacad280.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"银行卡\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/idCard/20180601/95af2d6e-73ef-4802-84a6-aa2feacad280.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"回家\",\n" +
                    "\t\t\t\t\"url\": \"http://60.168.131.134:6080/files/download/onlineCommunication/20180604/befeb48f-0cee-49d3-98d3-bb91b0f1af1b.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"京津冀\",\n" +
                    "\t\t\t\t\"url\": \"http://60.168.131.134:6080/files/download/onlineCommunication/20180604/f7aecae0-0133-4cbb-ae34-935052fa9f3e.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"哈哈哈\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/onlineCommunication/20180604/0e54ef17-3df8-4f5d-95d0-8b89a3277b61.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\t\t \t\t\t{\n" +
                    "\t\t\t\t\"type\": \"京津冀\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/onlineCommunication/20180604/befeb48f-0cee-49d3-98d3-bb91b0f1af1b.jpg\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t\"type\": \"哈哈哈\",\n" +
                    "\t\t\t\t\"url\": \"http://192.168.1.210:6080/files/download/onlineCommunication/20180604/befeb48f-0cee-49d3-98d3-bb91b0f1af1b.jpg\"\n" +
                    "\t\t\t}\n" +
                    "\t\t],\n" +
                    "\t\t\"operationTime\": \"20180604133456\",\n" +
                    "\t\t\"operator\": \"SH000\",\n" +
                    "\t\t\"text\": \"呵呵\"\n" +
                    "\t}],\n" +
                    "\t\"result\": {\n" +
                    "\t\t\"isSuccess\": \"true\",\n" +
                    "\t\t\"resultCode\": \"000\",\n" +
                    "\t\t\"resultMsg\": \"成功\"\n" +
                    "\t}\n" +
                    "}";
            if(coreResult == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, "未查询到数据"), HttpStatus.OK);
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getMessageObjs();
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        //查询头像及时间转换为14位
        List<OnlineMessageListDto> txResultList = new ArrayList();
        //排序
        Collections.sort(txResultList, (arg0, arg1) -> Long.parseLong(arg0.getOperationTime()) < Long.parseLong(arg1.getOperationTime()) ? 1 : -1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = null;
        String dateString = "";
        SysUser sysUser;
        for(OnlineMessageListDto onlineMessageListDto : resultList){
            sysUser = getSysUser(onlineMessageListDto.getOperator(), "taimeng");
            if(sysUser != null){
                onlineMessageListDto.setTxUrl(sysUser.getTXURL());
            }
            //主系统时间10点之前时间为13位无法排序  2018050591200
            if(onlineMessageListDto.getOperationTime().length() == 13){
                String date = onlineMessageListDto.getOperationTime().substring(0,8);
                String time = onlineMessageListDto.getOperationTime().substring(8,13);
                onlineMessageListDto.setOperationTime(date + "0" + time);
            }
            try {
                dateTime = dateFormat.parse(onlineMessageListDto.getOperationTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
             dateString = simpleDateFormat.format(dateTime);
             onlineMessageListDto.setOperationTime(dateString);
             txResultList.add(onlineMessageListDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, txResultList), HttpStatus.OK);
    }

    /**
     * 查询在线沟通附件历史
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> historyAttachmentQuery(String applyNum){
        List<OnlineMessageListDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        try {
            String coreResult = coreSystemInterface.historyAttachmentQuery("HistoryAttachmentQuery", applyNum);
            if(coreResult == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, "未查询到数据"), HttpStatus.OK);
            }
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getAttachmentObjs();
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
//        //查询APP本身的附件
//        List<OnlineMessageListDto> appResultList = applyService.getFileInfo(applyNum);
//        for(OnlineMessageListDto onlineMessageListDto : appResultList){
//            resultList.add(onlineMessageListDto);
//        }

        List<OnlineMessageListDto> txResultList = new ArrayList();
        //查询头像及时间转换为14位
        for(OnlineMessageListDto onlineMessageListDto : resultList){
            SysUser sysUser = getSysUser(onlineMessageListDto.getOperator(), "taimeng");
            if(sysUser != null){
                onlineMessageListDto.setTxUrl(sysUser.getTXURL());
            }
            //主系统时间10点之前时间为13位无法排序  2018050591200
            if(onlineMessageListDto.getOperationTime().length() == 13){
                String date = onlineMessageListDto.getOperationTime().substring(0,8);
                String time = onlineMessageListDto.getOperationTime().substring(8,13);
                onlineMessageListDto.setOperationTime(date + "0" + time);
            }
            txResultList.add(onlineMessageListDto);
        }
        //排序
        Collections.sort(txResultList, (arg0, arg1) -> Long.parseLong(arg0.getOperationTime()) < Long.parseLong(arg1.getOperationTime()) ? 1 : -1);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, txResultList), HttpStatus.OK);
    }

    /**
     * 在线沟通提交接口
     * @return
     */
    public ResponseEntity<Message> postOnlineCommunication(PostOnlineCommunicationDto postOnlineCommunicationDto){
        //查询APP本身的附件
        if(CommonUtils.isNull(postOnlineCommunicationDto.getApplyNum())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, "申请编号不可为空"), HttpStatus.OK);
        }
        //预审批附件
        List<CommunicationFileDto> applyFileList = applyService.getApprovalFileInfo(postOnlineCommunicationDto.getApplyNum());
        //在线沟通附件
        List<CommunicationFileDto> onlineCommunicationFileList = postOnlineCommunicationDto.getFileList();
        //若预审批附件未提交到主系统，此时提交
        if(!applyFileList.isEmpty()){
            onlineCommunicationFileList.addAll(applyFileList);
        }
        postOnlineCommunicationDto.setFileList(onlineCommunicationFileList);
        Resul res = new Resul();
        try {
            String result = coreSystemInterface.postOnlineCommunication("PostOnlineCommunication", postOnlineCommunicationDto);
            res = objectMapper.readValue(result, Resul.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(res.getResult().getIsSuccess().toString())){
            if(!applyFileList.isEmpty()){
                ApplyInfoNew applyInfo = applyInfoNewRepository.findByApplyNum(postOnlineCommunicationDto.getApplyNum());
                applyInfo.setApprovalFileState(ContractSignStatus.SUBMIT.code());
                applyInfoNewRepository.save(applyInfo);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, res.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

}
