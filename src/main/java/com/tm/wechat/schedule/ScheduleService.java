package com.tm.wechat.schedule;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.dto.sysUser.SysUserCallBackDto;
import com.tm.wechat.service.TMInterface;
import com.tm.wechat.service.YCInterface;
import com.tm.wechat.service.approval.ApprovalService;
import com.tm.wechat.service.sysUser.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by LEO on 16/9/1.
 */
@Service
public class ScheduleService {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TMInterface tmInterface;

    @Autowired
    private YCInterface ycInterface;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private SysUserService sysUserService;

    @Scheduled(cron = "59 59 23 * * ?")
    @Transactional
    public void getToken() throws IOException {
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        List<SysUser> tmSysUsers = null;
//        List<SysUser> ycSysUsers = null;
        try {
//            ycSysUsers = objectMapper.readValue(ycInterface.getSysUsers("syncUsers", "adminuser", "12940581fbbf2df3b9739fe34a3344b8", "1429604397531"), SysUserCallBackDto.class).getUsers();
            tmSysUsers = objectMapper.readValue(tmInterface.getSysUsers("syncUsers", "adminuser", "12940581fbbf2df3b9739fe34a3344b8", "1429604397531"), SysUserCallBackDto.class).getUsers();
            tmSysUsers.forEach(sysUser -> {
                sysUser.setCustomer("taimeng");
            });
//            ycSysUsers.forEach(sysUser -> {
//                sysUser.setCustomer("yachi");
//            });
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(tmSysUsers != null && tmSysUsers.size() != 0 && ycSysUsers != null && ycSysUsers.size() != 0){
        if(tmSysUsers != null && tmSysUsers.size() != 0){
            sysUserRepository.deleteAll();
            sysUserRepository.save(tmSysUsers);
//            sysUserRepository.save(ycSysUsers);
        }
    }
    @Scheduled(cron = "00 30 */1 * * ?")
    @Transactional
    public void queryFinancePreApplyResult() {
        approvalService.queryFinancePreApplyResult();
    }

    @Scheduled(cron = "00 40 */1 * * ?")
    public void queryICBCFinancePreApplyResult() {
        approvalService.queryICBCFinancePreApplyResult();
        approvalService.queryXWBankFinancePreApplyResult();
    }


    @Scheduled(cron = "00 20 */1 * * ?")
    @Transactional
    public void queryApplyStates() {
        approvalService.queryApplyStates();
    }

    @Scheduled(cron = "59 10 00 * * ?")
    @Transactional
    public void syncRole() {
        sysUserService.syncRole();
    }

    @Scheduled(cron = "* */10 * * * ?")
    public void changeAdminMsgCode(){
        sysUserService.changeAdminMsgCode();
    }
}
