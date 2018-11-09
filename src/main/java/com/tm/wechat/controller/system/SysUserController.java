package com.tm.wechat.controller.system;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.AccountProperties;
import com.tm.wechat.dao.SysUserRepository;
import com.tm.wechat.dao.SysUserRoleRepository;
import com.tm.wechat.domain.SysUser;
import com.tm.wechat.dto.approval.BankCardVerificationDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sms.SendMessageDto;
import com.tm.wechat.dto.sysUser.*;
import com.tm.wechat.security.PermissionService;
import com.tm.wechat.service.CreateQRCodeService;
import com.tm.wechat.service.TMInterface;
import com.tm.wechat.service.YCInterface;
import com.tm.wechat.service.addressinput.AddressInputService;
import com.tm.wechat.service.approval.ApprovalService;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.service.sysUser.CustomerFeedbackInterface;
import com.tm.wechat.service.sysUser.LeaseWeChatInterface;
import com.tm.wechat.service.sysUser.LoginRecordInterface;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.SendMsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Created by PengChao on 16/9/1.
 */
@RestController
@RequestMapping("sysUsers")
public class SysUserController {
    @Autowired
    private SysUserRepository sysUserRepository;


    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TMInterface tmInterface;
    @Autowired
    private YCInterface ycInterface;

    @Autowired
    private CustomerFeedbackInterface customerFeedbackInterface;

    @Autowired
    private LoginRecordInterface loginRecordInterface;

    @Autowired
    private LeaseWeChatInterface leaseWeChatInterface;

    @Autowired
    private AccountProperties accountProperties;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AddressInputService addressInputService;

    @Autowired
    private CreateQRCodeService createQRCodeService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private BaseService baseService;

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    /**
     * 查询用户信息
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getSysUserInfo(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam, Principal user){
        String customer = JsonPath.from(headerParam).get("systemflag");
        SysUser sysUser = baseService.getSysUser(user.getName(), customer);
        String code = sysUserService.getCode(sysUser.getXtczdm(),customer);
        Boolean isHplUser = permissionService.isHplUser(user.getName(), headerParam);
        SysUserDto sysUserDto =  new SysUserDto(sysUser);
        sysUserDto.setCode(code);
        sysUserDto.setIsHplUser(isHplUser);
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, sysUserDto);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    /**
     * 更新个人资料
     * @param userName
     * @param sysUserDto
     * @return
     */
    @PreAuthorize("@permission.isSameUser(#userName, authentication.principal.username)")
    @RequestMapping(value = "/{userName}/userInfo", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateSysUserInfo(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam, @PathVariable String userName, @RequestBody SysUserDto sysUserDto){
        sysUserDto.setUsername(userName);
        return sysUserService.updateSysUserInfo(sysUserDto, headerParam);
    }

    /**
     * 新增用户信息反馈
     * @param feedbackRecDto
     * @return
     */
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public  ResponseEntity<Message> addFeedback(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam, @RequestBody FeedbackRecDto feedbackRecDto, Principal principal){
        return customerFeedbackInterface.addFeedback(accountProperties.getAuth(), headerParam, feedbackRecDto,principal.getName());
    }

    /**
     * 保存太盟宝用户登录信息
     * @param userName 用户名
     * @param lat 经度
     * @param lon 纬度
     * @param address 中文地址
     * @return
     */
    @RequestMapping(value = "/saveLoginInfo", method = RequestMethod.POST)
    public  ResponseEntity<Message> saveLoginInfo(@RequestParam(value = "userName", required = true) String userName,
                                                  @RequestParam(value = "lat", required = true) Double lat,
                                                  @RequestParam(value = "lon", required = true) Double lon,
                                                  @RequestParam(value = "address", required = false) String address){
        SysUser sysuser = sysUserRepository.findByXtczdm(userName);
        String companyName = sysuser.getXTJGMC();
        return loginRecordInterface.saveLoginInfo(accountProperties.getAuth(), userName, lat, lon, address, companyName);
    }

    /**
     * 上传征信拍照信息
     * @param creditMsgRecDtos
     * @param user
     * @return
     */
    @RequestMapping(value = "/uploadCreditMsg", method = RequestMethod.POST)
    public ResponseEntity<Message> uploadCreditMsg(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                                   @RequestBody List<CreditMsgRecDto> creditMsgRecDtos, Principal user){
        return sysUserService.uploadCreditMsg(headerParam, creditMsgRecDtos, user);
    }

    /**
     * 用户同步
     */
    @RequestMapping(method = RequestMethod.POST)
    public void sync(){
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
            sysUserRepository.deleteAllInBatch();
            sysUserRepository.save(tmSysUsers);
//            sysUserRepository.save(ycSysUsers);
        }
    }

    @RequestMapping(value = "/syncOne",method = RequestMethod.POST)
    public ResponseEntity<Message> syncOne(@RequestParam(value = "userName", required = true) String userName){
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        List<SysUser> tmSysUsers = null;
        try {
            tmSysUsers = objectMapper.readValue(tmInterface.getSysUsers("syncUsers", "adminuser", "12940581fbbf2df3b9739fe34a3344b8", "1429604397531"), SysUserCallBackDto.class).getUsers();
            for(SysUser sysUser : tmSysUsers){
                if(userName.equals(sysUser.getXtczdm())){
                    sysUser.setCustomer("taimeng");
                    SysUser user = baseService.getSysUser(userName, "taimeng");
                    if(user != null){
                        sysUserRepository.delete(user);
                    }
                        sysUserRepository.save(sysUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "同步用户失败"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 新增賬戶
     * @param user:获取当前登录人信息
     * @param userDto:
     * @return
     */
    @RequestMapping(value = "/addUserWx", method = RequestMethod.POST)
    public ResponseEntity<Message> addUserWx(@RequestBody UserDto userDto, Principal user){
        return sysUserService.addUserWx(userDto,user.getName());
    }
    /**
     * 开设账号
     * @return
     */
    @RequestMapping(value = "/addSysUser", method = RequestMethod.POST)
    public ResponseEntity<Message> addUser(@RequestBody UserAddDto userAddDto, Principal user){
        try {
            return sysUserService.addUser(userAddDto, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("开设账号异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     *通过邀请码新增賬戶
     * @param addUserFromCodeDto:
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResponseEntity<Message> addUserFromCode(@RequestBody AddUserFromCodeDto addUserFromCodeDto){
        return sysUserService.addUserFromCode(addUserFromCodeDto);
    }

    /**
     * 刪除賬戶
     * @param user:获取当前登录人信息
     * @param userCode
     * @return
     */
    @RequestMapping(value = "/deleteUserWx", method = RequestMethod.GET)
    public ResponseEntity<Message> deleteUserWx(@RequestParam(value = "userCode", required = true) String userCode, Principal user){
        return sysUserService.deleteUserWx(userCode,user.getName());
    }

    /**
     * 禁止用户登录
     * @param user:获取当前登录人信息
     * @param userCode 被禁用户名
     * @return
     */
    @RequestMapping(value = "/banUserWx", method = RequestMethod.GET)
    public ResponseEntity<Message> banUserWx(@RequestParam(value = "userCode", required = true) String userCode, Principal user){
        return sysUserService.banLogin(userCode,user.getName());
    }

    /**
     * 重置下级密码
     * @param user:获取当前登录人信息
     * @param passwordDto:
     * @return
     */
    @RequestMapping(value = "/updateUserWx", method = RequestMethod.POST)
    public ResponseEntity<Message> updateUserWx(@RequestBody PasswordDto passwordDto,
                                                Principal user){
        return sysUserService.updateUserWx(passwordDto,user.getName());
    }


    /**
     * 修改密码
     * @param passwordDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public ResponseEntity<Message> updateUser(@RequestBody PasswordDto passwordDto,
                                                Principal user){
        return sysUserService.updateUser(passwordDto,user.getName());
    }



    /**
     * 賬戶展示
     * @param user
     * @return
     */
    @RequestMapping(value = "/userWxList", method = RequestMethod.GET)
    public ResponseEntity<Message> userWxList(Principal user){
        return sysUserService.userWxList(user.getName());
    }

    /**
     * 角色同步
     */
    @RequestMapping(value = "/syncRole",method = RequestMethod.GET)
    public void syncRole(){
        sysUserService.syncRole();
    }

    /**
     * 单个指定角色同步
     */
    @RequestMapping(value = "/syncOneRole",method = RequestMethod.GET)
    public ResponseEntity<Message> syncOneRole(@RequestParam(value = "userName", required = true) String userName){
        return sysUserService.syncOneRole(userName);
    }

    /**
     * 上传太盟宝用户联系人信息
     * @param userName 用户名
     * @return
     */
    @RequestMapping(value = "/uploadContacts", method = RequestMethod.POST)
    public  ResponseEntity<Message> uploadContacts(@RequestBody List<ContactDto> contacts,
                                                   @RequestParam(value = "userName", required = true) String userName){
        return loginRecordInterface.uploadContacts(accountProperties.getAuth(), contacts, userName);
    }

    /**
     * 通过手机号将验证码将录单消息推送到客户微信号上
     * @param pushOrderDto 用户名
     * @return
     */
    @RequestMapping(value = "/pushOrder", method = RequestMethod.POST)
    public  ResponseEntity<Message> pushOrder(@RequestBody PushOrderDto pushOrderDto){
        return leaseWeChatInterface.pushOrder(pushOrderDto);
    }


    /**
     * 发送短信验证码（先锋太盟）
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/sendRandomCode", method = RequestMethod.POST)
    public ResponseEntity<Message> sendRandomCode(String phoneNum, @RequestParam(value="type", defaultValue="") String type,
                                                  @RequestParam(value="name", defaultValue="") String name,
                                                  @RequestParam(value="uniqueMark", defaultValue="") String uniqueMark){
        return sysUserService.sendRandomCode(phoneNum, type, name, uniqueMark);
    }

    /**
     * 发送微众一审短信
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/sendWeBankApply", method = RequestMethod.GET)
    public ResponseEntity<Message> sendWeBankApply(String phoneNum,
                                                  @RequestParam(value="name", defaultValue="") String name,
                                                  @RequestParam(value="uniqueMark", defaultValue="") String uniqueMark){
        return approvalService.sendWeBankApply(phoneNum, uniqueMark, name);
    }

    /**
     * 发送短信验证码（先锋太盟）（主系统发送四要素）
     * @return
     */
    @RequestMapping(value = "/coreSystemSendCode", method = RequestMethod.POST)
    public ResponseEntity<Message> coreSystemSendCode(@RequestBody SendMessageDto sendMessageDto){
        return sysUserService.sendRandomCode(sendMessageDto.getPhoneNum(), SendMsgType.FOUR_ELEMENTS.code(), "", sendMessageDto.getApplyNum());
    }

    /**
     * 发送短信验证码（先锋太盟）（主系统发送还款卡收货地址录入短信）
     * @return
     */
    @RequestMapping(value = "/addressInputSendCode", method = RequestMethod.POST)
    public ResponseEntity<Message> addressInput(@RequestBody SendMessageDto sendMessageDto){
        try {
            return sysUserService.sendRandomCode(sendMessageDto.getPhoneNum(), SendMsgType.ADDRESS_INPUT.code(), "", sendMessageDto.getApplyNum());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("发送还款卡收货地址录入短信异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 更新用户角色
     */
    @RequestMapping(value = "/updateRole",method = RequestMethod.POST)
    public ResponseEntity<Message> updateRole(@RequestBody String[] resources, String userCode){
        return sysUserService.updateRole(userCode, resources);
    }

    /**
     * 获取用户角色
     */
    @RequestMapping(value = "/getRole",method = RequestMethod.GET)
    public ResponseEntity<Message> getRole(Principal user){
        return sysUserService.getRole(user.getName());
    }


    /**
     * 核实工行放款卡提交
     * @param
     * @return
     */
    @RequestMapping(value = "/submitVerificationInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitVerificationInfo(@RequestBody BankCardVerificationDto bankCardVerificationDto, Principal user){
        try {
            return addressInputService.submitVerificationInfo(bankCardVerificationDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("核实工行放款卡提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取放款卡信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getVerificationInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getVerificationInfo(String bankCardNum, Principal user){
        try {
            return addressInputService.getVerificationInfo(bankCardNum, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取放款卡信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取放款卡物流信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getExpressInfoByCardNO", method = RequestMethod.GET)
    public ResponseEntity<Message> getExpressInfoByCardNO(String bankCardNum, Principal user){
        try {
            return addressInputService.getExpressInfoByCardNO(bankCardNum, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取放款卡物流信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取放款卡物流列表信息
     * @param
     * @return
     */
    @RequestMapping(value = "/listCardStatusByCondition", method = RequestMethod.GET)
    public ResponseEntity<Message> listCardStatusByCondition(String queryType, String startTime, String endTime, String verificationCount,String condition, String pageIndex, String pageSize, Principal user){
        try {
            return addressInputService.listCardStatusByCondition(queryType, user.getName(), startTime, endTime, verificationCount, condition, pageIndex, pageSize );
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取放款卡物流列表信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 获取放款卡快递详细信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getExpress", method = RequestMethod.GET)
    public ResponseEntity<Message> getExpress(String courierNumber){
        try {
            return addressInputService.getExpress(courierNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取放款卡快递详细信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/redisSave", method = RequestMethod.POST)
    public ResponseEntity<Message> redisSave(String key, String value, int time){
        return sysUserService.redisSave(key,value,time);
    }

    @RequestMapping(value = "/getRedisValue", method = RequestMethod.POST)
    public ResponseEntity<Message> getRedisValue(String key){
        return sysUserService.getRedisValue(key);
    }



    @RequestMapping(value = "/getQRCode", method = RequestMethod.GET)
    public ResponseEntity<Message> getQRCode(){
        try {
            return createQRCodeService.getQRCode();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("批量生成FP二维码异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/getQRCodeByUserName", method = RequestMethod.GET)
    public ResponseEntity<Message> getQRCodeByUserName(String userName){
        try {
            return createQRCodeService.getQRCodeByUserName(userName);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取二维码异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
