package com.tm.wechat.service.usedcar;

import com.tm.wechat.dao.UsedCarAnalysisFileMaintainRepository;
import com.tm.wechat.domain.UsedCarAnalysisFileMaintain;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.usedcar.UsedCarAnalysisMaintainDto;
import com.tm.wechat.utils.commons.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by pengchao on 2018/8/3.
 */
@Service
public class UsedCarAnalysisMaintainService {

    @Autowired
    private UsedCarAnalysisFileMaintainRepository usedCarAnalysisFileMaintainRepository;


    /**
     *  添加二手车评估附件维护
     * @return
     */
    public ResponseEntity<Message> addUsedCarAnalysisFileMaintain(String userName, UsedCarAnalysisMaintainDto usedCarAnalysisMaintainDto){
        if(CommonUtils.isNull(usedCarAnalysisMaintainDto.getFileName()) || CommonUtils.isNull(usedCarAnalysisMaintainDto.getIsMust())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        UsedCarAnalysisFileMaintain usedCarAnalysisFileMaintain = usedCarAnalysisFileMaintainRepository.findTop1ByFileName(usedCarAnalysisMaintainDto.getFileName());
        if(null != usedCarAnalysisFileMaintain){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该附件名称已存在"), HttpStatus.OK);
        }
        UsedCarAnalysisFileMaintain newUsedCarAnalysisFileMaintain = new UsedCarAnalysisFileMaintain();
        newUsedCarAnalysisFileMaintain.setCreateUser(userName);
        newUsedCarAnalysisFileMaintain.setFileName(usedCarAnalysisMaintainDto.getFileName());
        newUsedCarAnalysisFileMaintain.setIsMust(usedCarAnalysisMaintainDto.getIsMust());
        newUsedCarAnalysisFileMaintain.setFileType(usedCarAnalysisMaintainDto.getFileType());
        usedCarAnalysisFileMaintainRepository.save(newUsedCarAnalysisFileMaintain);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 分页查询二手车评估附件维护
     * @param page
     * @param size
     * @return
     */
    public ResponseEntity<Message> getUsedCarAnalysisFileMaintainList(Integer page, Integer size){
        Page<UsedCarAnalysisFileMaintain> usedCarAnalysisFileMaintainPage = usedCarAnalysisFileMaintainRepository.findByOrderByUpdateTimeDesc(new PageRequest(page-1, size));
//        List<UsedCarAnalysisFileMaintain> usedCarAnalysisFileMaintains = usedCarAnalysisFileMaintainPage.getContent();
//        List<UsedCarAnalysisMaintainDto> usedCarAnalysisMaintainDtos = new ArrayList<>();
//        for(UsedCarAnalysisFileMaintain usedCarAnalysisFileMaintain : usedCarAnalysisFileMaintains){
//            usedCarAnalysisMaintainDtos.add(new UsedCarAnalysisMaintainDto(usedCarAnalysisFileMaintain));
//        }
//        PageDto pageDto = new PageDto(usedCarAnalysisFileMaintainPage.getTotalElements(), usedCarAnalysisMaintainDtos);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarAnalysisFileMaintainPage),HttpStatus.OK);
    }

    /**
     * 更新二手车评估附件维护
     * @param
     * @return
     */
    public ResponseEntity<Message> updateUsedCarAnalysisFileMaintain(String id, UsedCarAnalysisMaintainDto usedCarAnalysisMaintainDto, String userName){
        if(CommonUtils.isNull(usedCarAnalysisMaintainDto.getFileName()) || CommonUtils.isNull(usedCarAnalysisMaintainDto.getIsMust())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "缺少必要参数"), HttpStatus.OK);
        }
        UsedCarAnalysisFileMaintain usedCarAnalysisFileMaintain = usedCarAnalysisFileMaintainRepository.findOne(id);
        if(usedCarAnalysisFileMaintain == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到该附件类型"), HttpStatus.OK);
        }
        UsedCarAnalysisFileMaintain oldUsedCarAnalysisFileMaintain = usedCarAnalysisFileMaintainRepository.findTop1ByFileName(usedCarAnalysisMaintainDto.getFileName());
        if(oldUsedCarAnalysisFileMaintain != null && !usedCarAnalysisFileMaintain.getId().equals(oldUsedCarAnalysisFileMaintain.getId())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该附件名称已存在"), HttpStatus.OK);
        }
        usedCarAnalysisFileMaintain.setFileType(usedCarAnalysisMaintainDto.getFileType());
        usedCarAnalysisFileMaintain.setFileName(usedCarAnalysisMaintainDto.getFileName());
        usedCarAnalysisFileMaintain.setIsMust(usedCarAnalysisMaintainDto.getIsMust());
        usedCarAnalysisFileMaintain.setUpdateTime(new Date());
        usedCarAnalysisFileMaintain.setUpdateUser(userName);
        usedCarAnalysisFileMaintainRepository.save(usedCarAnalysisFileMaintain);
        return new ResponseEntity(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 删除二手车评估附件维护
     * @param id
     * @return
     */
    public ResponseEntity<Message> deleteUsedCarAnalysisFileMaintain(String id){
        usedCarAnalysisFileMaintainRepository.delete(id);
        return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询一条二手车评估附件维护
     * @param id
     * @return
     */
    public  ResponseEntity<Message> getUsedCarAnalysisFileMaintain(String id){
        UsedCarAnalysisFileMaintain usedCarAnalysisFileMaintain = usedCarAnalysisFileMaintainRepository.findOne(id);
        if(usedCarAnalysisFileMaintain == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到该附件类型"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarAnalysisFileMaintain), HttpStatus.OK);
    }

}
