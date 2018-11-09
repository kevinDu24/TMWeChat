package com.tm.wechat.service.statistics;

import com.tm.wechat.consts.ApprovalType;
import com.tm.wechat.dao.ApprovalRepository;
import com.tm.wechat.dto.approval.AchieveInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.order.OrganizationResult;
import com.tm.wechat.dto.statistics.UserApplyInfoResult;
import com.tm.wechat.dto.statistics.UserResult;
import com.tm.wechat.dto.sysUser.SysUserInfoDto;
import com.tm.wechat.dto.sysUser.UserListDto;
import com.tm.wechat.service.applyOnline.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private ApprovalRepository approvalRepository;

    /**
     * 根据经销商账号获取到下面的子账号进件列表统计
     * @param fpName
     * @return
     */
    public ResponseEntity<Message> getChildApplyInfoByFpName(String fpName) {
        //获取 fpName 下面的所有子账号
        ResponseEntity<Message> responseEntity = applyService.getUserInfoList(fpName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<OrganizationResult> userResultList = (List<OrganizationResult>) responseEntity.getBody().getData();
        //获取到用户名列表
        List<String> userNameList = userResultList.stream().map(OrganizationResult::getUserName).collect(Collectors.toList());

        //统计子账号提交预审批的单子
        List<Object> dataList = approvalRepository.findAchieveInfoCountGroupByFpName(userNameList, ApprovalType.PASS.code(), ApprovalType.REFUSE_NOREASON.code());

        List<UserApplyInfoResult> resultList = new ArrayList();
        UserApplyInfoResult userApplyInfoResult;
        Object[] objs;
        for (Object object : dataList) {
            objs = (Object[]) object;
            userApplyInfoResult = new UserApplyInfoResult(objs);
            resultList.add(userApplyInfoResult);
        }

        List<UserApplyInfoResult> allResultList = new ArrayList();
        for (OrganizationResult organizationResult : userResultList) {
            UserApplyInfoResult userApplyInfoResult1 = new UserApplyInfoResult();
            userApplyInfoResult1.setFpName(organizationResult.getUserName());
            userApplyInfoResult1.setRealName(organizationResult.getXtczmc());
            int haveData = 0;
            for (UserApplyInfoResult applyInfoResult : resultList) {
                if (organizationResult.getUserName().equals(applyInfoResult.getFpName())){
                    userApplyInfoResult1.setNum(applyInfoResult.getNum());
                    haveData = 1;
                    break;
                }
            }
            if (haveData == 0){
                userApplyInfoResult1.setNum(0);
            }

            allResultList.add(userApplyInfoResult1);
        }

        //倒序排序
        allResultList.sort(Comparator.naturalOrder());
//        Collections.reverse(allResultList);
        
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, allResultList), HttpStatus.OK);
    }
}
