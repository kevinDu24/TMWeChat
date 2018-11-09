package com.tm.wechat.service.sysUser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.MessageProperties;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dao.*;
import com.tm.wechat.domain.*;
import com.tm.wechat.dto.core.result.Zam003WxResult;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.result.CoreRes;
import com.tm.wechat.dto.result.Result;
import com.tm.wechat.dto.sms.SendShortMessageDto;
import com.tm.wechat.dto.sysUser.*;
import com.tm.wechat.service.BaiduShortUrlService;
import com.tm.wechat.service.TMInterface;
import com.tm.wechat.service.addressinput.AddressInputService;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.utils.MessageUtil;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.SendMsgType;
import com.tm.wechat.consts.ContractSignStatus;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LEO on 16/9/28.
 */
@Service
public class SysUserService extends BaseService {

    @Autowired
    private SysUserInterface sysUserInterface;

    @Autowired
    private Gson gson;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    private SysUserYCInterface sysUserYCInterface;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TMInterface tmInterface;

    @Autowired
    private ApplyInfoNewRepository applyInfoNewRepository;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private MessageUtil messageUtil;

    @Autowired
    private MessageLogsRepository messageLogsRepository;

    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysResourceRepository sysResourceRepository;

    @Autowired
    private SysUserInfoRepository sysUserInfoRepository;

    @Autowired
    private CoreSystemInterface coreSystemInterface;

    @Autowired
    private BaiduShortUrlService baiduShortUrlService;

    @Autowired
    private AddressInputService addressInputService;

    @Autowired
    private WechatProperties wechatProperties;


    public static String fourElementsUrl = "http://wx.xftm.com/#/app/factorVerification?applyNum=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; //签署渠道
    public static String addressInputUrl = "http://wx.xftm.com/#/app/addressInput?applyNum=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    public static String confirmMsgUrl =  "http://wx.xftm.com/#/app/wzApply?uniqueMark=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final Logger logger = LoggerFactory.getLogger(SysUserService.class);

    /**
     * 发送短信验证码
     * @param phoneNum
     * @return
     */
    public ResponseEntity<Message> sendRandomCode(String phoneNum, String type, String name, String uniqueMark){
        if(CommonUtils.isNull(phoneNum)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "手机号不可为空"), HttpStatus.OK);
        }
        String code = "" + (int)(Math.random()*900000+100000);
        redisRepository.save("TmWEChatSendCode_" + phoneNum, code, 300);
        //调用外部的发送短信接口
        String send = "";
        String pszMsg = "";
        String origin = "1";
        String shortUrl = "";
        if(redisRepository.get("tmwechat_shortMessageOrigin") != null){
            origin = (String) redisRepository.get("tmwechat_shortMessageOrigin");
        }
        try {
            //在线助力融提交微众预审，用户确认信息页面
            if(SendMsgType.CONFIRM_MSG.code().equals(type)){
                shortUrl = baiduShortUrlService.getShortUrl(confirmMsgUrl.replace("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", uniqueMark));
                pszMsg = messageProperties.getConfirmMsg().replace("***",name).replace("http://dwz.cn",shortUrl);
            //四要素验证
            }else if(SendMsgType.FOUR_ELEMENTS.code().equals(type)){
                shortUrl = baiduShortUrlService.getShortUrl(fourElementsUrl.replace("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", uniqueMark));
                pszMsg = messageProperties.getFourElements().replace("***","用户").replace("http://dwz.cn",shortUrl);
            //放款卡地址录入
            }else if(SendMsgType.ADDRESS_INPUT.code().equals(type)){
                shortUrl = baiduShortUrlService.getShortUrl(addressInputUrl.replace("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", uniqueMark));
                pszMsg = messageProperties.getAddressInput().replace("***","用户").replace("http://dwz.cn",shortUrl);
            //其他通用短信
            }else {
                pszMsg =messageProperties.getPszMsg().replace("xxxxxx",code);
            }
            SendShortMessageDto sendShortMessageDto = new SendShortMessageDto();
            sendShortMessageDto.setPhoneNum(phoneNum);
            sendShortMessageDto.setText(pszMsg);

            //梦网科技发送
            if("0".equals(origin)){
                send = messageUtil.senRadomCode(phoneNum, pszMsg);
                //解析后的返回值不为空且长度不大于10，则提交失败，交给主系统发送
                if(send == null || "".equals(send) || send.length()< 10){
                    ResponseEntity<Message> responseEntity = addressInputService.sendShortMessage(sendShortMessageDto);
                    send = responseEntity.getBody().getStatus();
                }
            } else {
                //主系统发送
                ResponseEntity<Message> responseEntity = addressInputService.sendShortMessage(sendShortMessageDto);
                send = responseEntity.getBody().getStatus();
                //主系统发送失败，再由梦网科技发送
                if("ERROR".equals(send)){
                    send = messageUtil.senRadomCode(phoneNum, pszMsg);
                }
            }
            saveMessageLog(phoneNum, pszMsg, send);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }
        if(send != null && !"".equals(send) && ("SUCCESS".equals(send) || send.length()>10)){
            //解析后的返回值不为空且长度大于10，则是提交成功
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else{//解析后的返回值不为空且长度不大于10，则提交失败，返回错误码
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }
    }

    /**
     * 修改超级短信验证码
     */
    public void changeAdminMsgCode(){
        String code = "" + (int)(Math.random()*900000+100000);
        redisRepository.save("adminMsgCode",code);
    }

    /**
     * 获取超级验证码
     * @return
     */
    public ResponseEntity<Message> getAdminMagCode(){
        if(CommonUtils.isNull((String) redisRepository.get("adminMsgCode"))){
            changeAdminMsgCode();
        }
        return getRedisValue("adminMsgCode");
    }

    /**
     * 短信验证码校验
     * @return
     */
    public String checkMsgCode( String phoneNum, String msgCode) {
        String message = null;
        //超级管理员验证码（方便测试使用）
        if(CommonUtils.isNotNull((String) redisRepository.get("adminMsgCode")) && redisRepository.get("adminMsgCode").equals(msgCode)){
            return message;
        }
        if (redisRepository.get("TmWEChatSendCode_" + phoneNum) == null) {
            message = "验证码已过期,请重新获取验证码";
            return message;
        }
        if (!redisRepository.get("TmWEChatSendCode_" + phoneNum).equals(msgCode)) {
            message = "验证码错误";
        }
       return message;
    }

    /**
     * 短信验证码校验
     * @return
     */
    public ResponseEntity<Message> checkMessageCode(String phoneNum, String msgCode) {
        String result = checkMsgCode(phoneNum, msgCode);
        if(result == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result), HttpStatus.OK);
    }

    /**
     * 保存发送短信log
     * @param phoneNum
     * @param content
     */
    public void saveMessageLog(String phoneNum, String content, String sendStatus){
        Date nowDate = new Date();
        MessageLogs messageLog = new MessageLogs();
        messageLog.setPhone(phoneNum);
        messageLog.setContent(content);
        messageLog.setSendTime(nowDate);
        messageLog.setUpdateTime(nowDate);
        messageLog.setStatus(sendStatus);
        messageLogsRepository.save(messageLog);
    }


    public ResponseEntity<Message> redisSave(String key, String value, int time){
        redisRepository.save(key, value, time);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,  redisRepository.get(key)), HttpStatus.OK);
    }

    public ResponseEntity<Message> getRedisValue(String key){
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,  redisRepository.get(key)), HttpStatus.OK);
    }

    /**
     * 修改个人资料
     *
     * @param sysUserDto
     * @return
     */
    public ResponseEntity<Message> updateSysUserInfo(SysUserDto sysUserDto, String headerParam) {
        String customer = JsonPath.from(headerParam).get("systemflag");
        if ("yachi".equals(customer)) {
            sysUserYCInterface.updateSysUserInfo("ModifyUser", sysUserDto.getUsername(), wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), sysUserDto.getHeadImg(), sysUserDto.getEmail(), sysUserDto.getPhoneNum());
        } else {
            sysUserInterface.updateSysUserInfo("ModifyUser", sysUserDto.getUsername(), wechatProperties.getTimestamp(),
                    wechatProperties.getSign(), sysUserDto.getHeadImg(), sysUserDto.getEmail(), sysUserDto.getPhoneNum());
        }
        SysUser sysUser = getSysUser(sysUserDto.getUsername(), customer);
        sysUser.setTXURL(sysUserDto.getHeadImg());
        sysUser.setXTYHYX(sysUserDto.getEmail());
        sysUser.setXTYHSJ(sysUserDto.getPhoneNum());
        sysUserRepository.save(sysUser);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 上传征信拍照
     *
     * @param creditMsgRecDtos
     * @param user
     * @return
     */
    public ResponseEntity<Message> uploadCreditMsg(String headerParam, List<CreditMsgRecDto> creditMsgRecDtos, Principal user) {
        final Boolean[] containApplicant = {false};
        creditMsgRecDtos.forEach(creditMsgRecDto -> {
            if (creditMsgRecDto.getType().equals(1)) {
                containApplicant[0] = true;
            }
            creditMsgRecDto.setUserId(user.getName());
        });
        if (!containApplicant[0]) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "申请人信息为必填项"), HttpStatus.OK);
        }
        String result;
        String customer = JsonPath.from(headerParam).get("systemflag");
        if ("taimeng".equals(customer)) {
            result = sysUserInterface.uploadCreditMsg("uploadZXImg", gson.toJson(creditMsgRecDtos));
        } else {
            result = sysUserYCInterface.uploadCreditMsg("uploadZXImg", gson.toJson(creditMsgRecDtos));
        }
        Result res = gson.fromJson(result, Result.class);
        if (!res.getRes().getMsg().equals("成功")) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, res.getRes().getMsg()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 获取用户邀请码（只获取总经理、区域经理的邀请码）
     * @param userName 用户名
     * @param customer 所属客户
     * @return
     */
    public String getCode(String userName, String customer){
        SysUser sysUser = getSysUser(userName, customer);
        if(sysUser == null){
            return null;
        }
        Long xtjgid = sysUser.getXTJGID(); //系统机构id
        String code = "";
        SysUserRole sysUserRole;
        String xtbmmc = sysUser.getXTBMMC() == null ||
                sysUser.getXTBMMC().isEmpty() ? "ZJL" : sysUser.getXTBMMC(); //经销商部门，为空默认经销商（即总经理）
        //经销商和区域经理可分享邀请码,销售经理不可分享
        if("ZJL".equals(xtbmmc)){
            sysUserRole = sysUserRoleRepository.getZJL(xtjgid);
            if(sysUserRole != null){
                code = sysUserRole.getXtyqhm();
            }
        }else {
            sysUserRole = sysUserRoleRepository.findByXtczdm(userName);
            if(sysUserRole != null){
                code = sysUserRole.getXtyqhm();
            }
        }
        return code;
    }
    /**
     * 新增账户
     *
     * @param userDto
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addUserWx(UserDto userDto, String userName) {
        if(userDto.getUserCode() == null || "".equals(userDto.getUserCode().trim())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "新增失败，用户名不能为空"), HttpStatus.OK);
        }
        List<ApplyInfoNew> applyInfoList = applyInfoNewRepository.findByCreateUser(userDto.getUserCode());
        //创建过的账号且已经提交过申请，不能再被创建
        if(!applyInfoList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "新增失败，该用户已被创建"), HttpStatus.OK);
        }
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        SysUser sysUser = getSysUser(userName, "taimeng");
        SysUserRole userRole = null;
        if("1".equals(userDto.getUserType())){
            userRole = sysUserRoleRepository.findByXtbmdmAndXtjgid("ZJL",sysUser.getXTJGID());
        }else{
            userRole = sysUserRoleRepository.findByXtczdm(userDto.getSjbmdm());
        }
        if(userRole==null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "组织架构有误,请联系客服"), HttpStatus.OK);
        }
        Zam003WxResult codeResult = new Zam003WxResult();
        try {
            AddUserParamDto addUserParamDto = new AddUserParamDto();
            AddUserDto addUserDto = new AddUserDto();
            addUserDto.setXTCZDM(userDto.getUserCode());
            addUserDto.setXTCZMC(userDto.getUserCode());
            addUserDto.setPWD1(userDto.getPassword());
            addUserDto.setXTJGDM(userName);
            addUserDto.setXTBMMC(("1".equals(userDto.getUserType()) ? "QYJL" : "XSJL"));
            addUserDto.setXTYHSJ(userDto.getPhoneNum());
            addUserDto.setXTCZRQ(CommonUtils.getStrDate(new Date(), CommonUtils.simpleDateFormat_14).substring(0, 8));
            addUserDto.setXTCZSJ(CommonUtils.getStrDate(new Date(), CommonUtils.simpleDateFormat_14).substring(8, 14));
            addUserDto.setXTCZRY(userDto.getSjbmdm());
            addUserDto.setSJBMDM(userRole.getXTBCID());
            addUserParamDto.setParam(addUserDto);
//            JSONObject jsonObject = JSONObject.fromObject(addUserParamDto);
//            String param = jsonObject.toString();
            String param = gson.toJson(addUserParamDto);
            String result = tmInterface.addUserWx("addUserWx", param);
            codeResult = objectMapper.readValue(result, Zam003WxResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            codeResult.getZam003Re().setCustomer("taimeng");
            sysUserRepository.save(codeResult.getZam003Re());
            SysUserRole sysUserRole =new SysUserRole();
            sysUserRole.setXTCZRY(codeResult.getZam003Re().getXTCZRY());
            sysUserRole.setXTCZSJ(String.valueOf(codeResult.getZam003Re().getXTCZSJ()));
            sysUserRole.setXTCZRQ(String.valueOf(codeResult.getZam003Re().getXTCZRQ()));
            sysUserRole.setSJBMDM(userRole.getXTBCID());
            sysUserRole.setXTBCID(codeResult.getZam003Re().getXTBMID());
            sysUserRole.setXtbmdm(codeResult.getZam003Re().getXTBMMC());
            sysUserRole.setXtbmmc(("1".equals(userDto.getUserType()) ? "分公司" : "团队"));
            sysUserRole.setXtczdm(codeResult.getZam003Re().getXtczdm());
            sysUserRole.setXtjgid(codeResult.getZam003Re().getXTJGID());
            sysUserRole.setXTQYBZ("Y");
            sysUserRoleRepository.save(sysUserRole);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 新增账户
     *
     * @param
     * @param
     * @return
     */
    public ResponseEntity<Message> addUser(UserAddDto userAddDto, String loginUser) {
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        Zam003WxResult codeResult = new Zam003WxResult();
        userAddDto.setStatus(ContractSignStatus.NEW.code());
        userAddDto.setLoginuser(loginUser);
        try {
            logger.info("userAddDto={}", JSONObject.fromObject(userAddDto).toString(2));
            String result = coreSystemInterface.superiorOpeningAccount("SuperiorOpeningAccount", userAddDto);
            logger.info("addUserResult={}", result);
            codeResult = objectMapper.readValue(result, Zam003WxResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            String userCode = codeResult.getXtczdm();
            if(userCode == null || userCode.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "账号开设失败，用户名错误" + userCode), HttpStatus.OK);
            }
            SysUserInfo oldSysUserInfo = sysUserInfoRepository.findByXtczdm(userCode);
            if(oldSysUserInfo != null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "账号开设失败，用户名已存在" + userCode), HttpStatus.OK);
            }
            SysUserInfo sysUserInfo = new SysUserInfo();
            sysUserInfo.setActivationState(ContractSignStatus.NEW.code());
            sysUserInfo.setIdCardState(ContractSignStatus.NEW.code());
            sysUserInfo.setPhoneState(ContractSignStatus.NEW.code());
            sysUserInfo.setUserInfoState(ContractSignStatus.NEW.code());
            sysUserInfo.setXtczdm(userCode);
            sysUserInfo.setCreateUser(userAddDto.getUsername());
            sysUserInfoRepository.save(sysUserInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, userCode), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 更新用户登录权限
     * @param
     * @param userCode 被修改的用户代码
     * @return
     */
    public ResponseEntity<Message> updateLoginAuth(String userCode,String loginAuth) {
            if(userCode == null || userCode.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "用户名不能为空"), HttpStatus.OK);
            }
            SysUserInfo oldSysUserInfo = sysUserInfoRepository.findByXtczdm(userCode);
            if(oldSysUserInfo == null){
                SysUserInfo sysUserInfo = new SysUserInfo();
                sysUserInfo.setLoginAuth(loginAuth);
                sysUserInfo.setXtczdm(userCode);
                sysUserInfoRepository.save(sysUserInfo);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            }
            oldSysUserInfo.setLoginAuth(loginAuth);
            sysUserInfoRepository.save(oldSysUserInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 获取用户登录权限
     * @param
     * @param userCode 要获取的用户代码
     * @return
     */
    public ResponseEntity<Message> getLoginAuth(String userCode) {
        SysUserInfo sysUserInfo = sysUserInfoRepository.findByXtczdm(userCode);
        //默认账号是有权限的，查询不到是因为sysUserInfo未保存该用户信息
        if(sysUserInfo == null || sysUserInfo.getLoginAuth() == null || sysUserInfo.getLoginAuth().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, ContractSignStatus.SUBMIT.code()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,null, sysUserInfo.getLoginAuth()), HttpStatus.OK);
    }


    /**
     * 认证用户手机号
     * @param
     * @return
     */
    public ResponseEntity<Message> authUserPhone(AddUserFromCodeDto addUserFromCodeDto) {
        if(addUserFromCodeDto.getPhoneNum() == null || addUserFromCodeDto.getPhoneNum().trim().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "认证失败，手机号不能为空！"), HttpStatus.OK);
        }
        if(addUserFromCodeDto.getMsgCode() == null || addUserFromCodeDto.getMsgCode().trim().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "认证失败，短信验证码不能为空！"), HttpStatus.OK);
        }
        SysUserInfo phoneNumUser = sysUserInfoRepository.findByPhoneNum(addUserFromCodeDto.getPhoneNum());
        if(phoneNumUser != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "认证失败，当前身手机号已被另一账号绑定！"), HttpStatus.OK);
        }
        //校验短信验证码
        String message = checkMsgCode(addUserFromCodeDto.getPhoneNum(), addUserFromCodeDto.getMsgCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        SysUserInfo sysUserInfo =sysUserInfoRepository.findByXtczdm(addUserFromCodeDto.getUserName());
        if(sysUserInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到该账号"), HttpStatus.OK);
        }
        sysUserInfo.setPhoneNum(addUserFromCodeDto.getPhoneNum());
        sysUserInfo.setPhoneState(ContractSignStatus.SUBMIT.code());
        sysUserInfoRepository.save(sysUserInfo);
        return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 认证用户身份证，验证通过后提交到主系统
     * @param
     * @return
     */
    public ResponseEntity<Message> authUserIdCard(AddUserFromCodeDto addUserFromCodeDto) {
        SysUserInfo sysUserInfo = sysUserInfoRepository.findByXtczdm(addUserFromCodeDto.getUserName());
        if(sysUserInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到该账号"), HttpStatus.OK);
        }
        SysUserInfo idCardNumUser = sysUserInfoRepository.findByIdCardNum(addUserFromCodeDto.getIdCard());
        if(idCardNumUser != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "认证失败，当前身份证号已被另一账号绑定！"), HttpStatus.OK);
        }
        if(ContractSignStatus.NEW.code().equals(sysUserInfo.getPhoneState())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先完成手机号认证"), HttpStatus.OK);
        }
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        AuthSubmitDto authSubmitDto = new AuthSubmitDto();
        authSubmitDto.setXTCZMC(addUserFromCodeDto.getName());
        authSubmitDto.setIdCardNum(addUserFromCodeDto.getIdCard());
        authSubmitDto.setUserName(addUserFromCodeDto.getUserName());
        authSubmitDto.setName(addUserFromCodeDto.getName());
        Zam003WxResult codeResult = new Zam003WxResult();
        try {
            //调用主系统实名认证接口
            String result = coreSystemInterface.identityVerificationJK("IdentityVerificationJK", authSubmitDto);
            logger.info("IdentityVerificationJK={}", result);
            logger.info("authSubmitDto={}", authSubmitDto);
            codeResult = objectMapper.readValue(result, Zam003WxResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            //主系统认证通过
            authSubmitDto.setXTSJHM(sysUserInfo.getPhoneNum());
            authSubmitDto.setPhoneNum(sysUserInfo.getPhoneNum());
            authSubmitDto.setActivationState(ContractSignStatus.SUBMIT.code());
            Zam003WxResult coreResult = new Zam003WxResult();
            try {
                //用户验证信息提交
                String result = coreSystemInterface.userActivationSubmit("UserActivationSubmit", authSubmitDto);
                logger.info("authSubmitDto={}", authSubmitDto);
                logger.info("UserActivationSubmit={}", result);
                codeResult = objectMapper.readValue(result, Zam003WxResult.class);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            if ("true".equals(codeResult.getResult().getIsSuccess())) {
                //提交后更新认证状态
                //认证通过后保存信息并提交到主系统
                sysUserInfo.setIdCardNum(addUserFromCodeDto.getIdCard());
                sysUserInfo.setName(addUserFromCodeDto.getName());
                sysUserInfo.setIdCardState(ContractSignStatus.SUBMIT.code());
                sysUserInfo.setActivationState(ContractSignStatus.SUBMIT.code());
                sysUserInfoRepository.save(sysUserInfo);
                return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            } else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }



    /**
     * 其他信息保存
     * @param
     * @return
     */
    public ResponseEntity<Message> addUserInfo(AddUserFromCodeDto addUserFromCodeDto) {
        SysUserInfo sysUserInfo =sysUserInfoRepository.findByXtczdm(addUserFromCodeDto.getUserName());
        if(sysUserInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到该账号"), HttpStatus.OK);
        }
        if(!ContractSignStatus.SUBMIT.code().equals(sysUserInfo.getActivationState())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先完成手机号认证和身份认证"), HttpStatus.OK);
        }
        sysUserInfo.setWeChat(addUserFromCodeDto.getWeChat());
        sysUserInfo.setBankNum(addUserFromCodeDto.getBankNum());
        sysUserInfo.setUserInfoState(ContractSignStatus.SUBMIT.code());
        sysUserInfoRepository.save(sysUserInfo);
        return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 获取用户认证状态
     * @param
     * @return
     */
    public ResponseEntity<Message> getUserAuthState(String userName) {
//        SysUserInfo sysUserInfo =sysUserInfoRepository.findByXtczdm(userName);
        SysUserInfo sysUserInfo =getSysUserInfo(userName);
        if(sysUserInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到该账号"), HttpStatus.OK);
        }
        AuthInfoDto authInfoDto = new AuthInfoDto();

        //默认是认证通过的，只有通过账号开设接口开设的账号才需要认证
        if(sysUserInfo.getPhoneState() == null || sysUserInfo.getPhoneState().isEmpty()){
            authInfoDto.setPhoneState(ContractSignStatus.SUBMIT.code());
        }else {
            authInfoDto.setPhoneState(sysUserInfo.getPhoneState());
        }
        if(sysUserInfo.getIdCardState() == null || sysUserInfo.getIdCardState().isEmpty()){
            authInfoDto.setIdCardState(ContractSignStatus.SUBMIT.code());
        }else {
            authInfoDto.setIdCardState(sysUserInfo.getIdCardState());
        }

        if(sysUserInfo.getUserInfoState() == null || sysUserInfo.getUserInfoState().isEmpty()){
            authInfoDto.setUserInfoState(ContractSignStatus.SUBMIT.code());
        }else {
            authInfoDto.setUserInfoState(sysUserInfo.getUserInfoState());
        }

        return  new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,authInfoDto), HttpStatus.OK);
    }



    //通过邀请码注册账号
    @Transactional
    public ResponseEntity<Message> addUserFromCode(AddUserFromCodeDto addUserFromCodeDto) {
        String code = addUserFromCodeDto.getCode();
        if(code == null || code.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "注册失败，邀请码不能为空！"), HttpStatus.OK);
        }
        if(addUserFromCodeDto.getPhoneNum() == null || addUserFromCodeDto.getPhoneNum().trim().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "注册失败，手机号不能为空！"), HttpStatus.OK);
        }
        if(addUserFromCodeDto.getMsgCode() == null || addUserFromCodeDto.getMsgCode().trim().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "注册失败，短信验证码不能为空！"), HttpStatus.OK);
        }
        //校验短信验证码
        String message = checkMsgCode(addUserFromCodeDto.getPhoneNum(), addUserFromCodeDto.getMsgCode());
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        SysUserRole userRole = sysUserRoleRepository.findByXtyqhm(code);
        if(userRole == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "邀请码错误，请重新输入！"), HttpStatus.OK);
        }
        //只有总经理和区域经理可以注册下一级
        if(!"QYJL".equals(userRole.getXtbmdm()) && !"ZJL".equals(userRole.getXtbmdm()) && !"XSJL".equals(userRole.getXtbmdm())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该邀请码不能用于注册账号！"), HttpStatus.OK);
        }
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Zam003WxResult codeResult = new Zam003WxResult();
        try {
            String result = tmInterface.addUserFromCode("addUserWx_new", addUserFromCodeDto.getName(), addUserFromCodeDto.getPassword(),
                    addUserFromCodeDto.getPhoneNum(), code);
            codeResult = objectMapper.readValue(result, Zam003WxResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if ("true".equals(codeResult.getResult().getIsSuccess())) {
            SysUser sysUser = codeResult.getZam003Re();
            SysUserRole sysUserRole = codeResult.getZam017Re();
            if(sysUser.getXtczdm() != null && !sysUser.getXtczdm().isEmpty() && sysUserRole.getXtczdm() != null && !sysUserRole.getXtczdm().isEmpty()){
                sysUser.setCustomer("taimeng");
                if(sysUser.getXTCZID() != null && "".equals(sysUser.getXTCZID().toString())){
                    sysUserRepository.save(sysUser);
                }
                if(sysUserRole.getXTBCID() != null && "".equals(sysUserRole.getXTBCID().toString()) && "0".equals(sysUserRole.getXTBCID().toString())){
                    sysUserRoleRepository.save(sysUserRole);
                }
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, sysUser.getXtczdm()), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 刪除账户
     *
     * @param operator
     * @param userCode
     * @return
     */
    @Transactional
    public ResponseEntity<Message> deleteUserWx(String userCode,String operator) {
        CoreRes codeResult = new CoreRes();
        try {
            String result = tmInterface.deleteUserWx(
                    "deleteUserWx",
                    wechatProperties.getTimestamp(),
                    wechatProperties.getSign(),
                    userCode,
                    operator
            );
            codeResult = objectMapper.readValue(result, CoreRes.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getIsSuccess())){
            SysUser sysUser = getSysUser(userCode, "taimeng");
            if(sysUser != null){
                sysUserRepository.delete(sysUser);
            }
            SysUserRole sysUserRole = sysUserRoleRepository.findByXtczdm(userCode);
            if(sysUserRole != null){
                sysUserRoleRepository.delete(sysUserRole);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 禁止用户登录
     * @param userCode 用户名
     * @param operator 操作人
     * @return
     */
    @Transactional
    public ResponseEntity<Message> banLogin(String userCode,String operator) {
        Message codeResult = new Message();
        try {
             String result = tmInterface.banUserWx(
                     "forbidLogin",
                     wechatProperties.getTimestamp(),
                     wechatProperties.getSign(),
                     userCode,
                     operator
                 );

             codeResult = objectMapper.readValue(result, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("SUCCESS".equals(codeResult.getStatus())){
            SysUser sysUser = getSysUser(userCode, "taimeng");
            if(sysUser != null){
                sysUser.setXTQYBZ("否");
                sysUserRepository.save(sysUser);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"禁用失败，该用户已不存在"), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getError()), HttpStatus.OK);
        }
    }

    /**
     * 重置下级密码
     *
     * @param passwordDto
     * @param operator
     * @return
     */
    public ResponseEntity<Message> updateUserWx(PasswordDto passwordDto, String operator) {
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        Zam003WxResult codeResult = new Zam003WxResult();
        try {
            String result = tmInterface.updateUserWx(
                    "updateUserWx", wechatProperties.getTimestamp(),wechatProperties.getSign(),
                    passwordDto.getUserCode(),
                    passwordDto.getNewPwd(),
                    operator
            );
            codeResult = objectMapper.readValue(result, Zam003WxResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
//            SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(codeResult.getZam003Re().getXtczdm(),"taimeng");
//            if(sysUser == null){
//                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//            }
//            sysUser.setXTCZMM(codeResult.getZam003Re().getXTCZMM());
//            sysUserRepository.save(sysUser);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }


    /**
     * 修改自身密码
     * @param passwordDto
     * @param operator
     * @return
     */
    public ResponseEntity<Message> updateUser(PasswordDto passwordDto, String operator) {
        SysUser sysUser = getSysUser(operator, "taimeng");
        Message message;
        //原密码校验
        if(passwordDto.getNewPwd() != null && !passwordDto.getNewPwd().isEmpty()){
            if(!passwordEncoder.isPasswordValid(sysUser.getXTCZMM().toLowerCase(), passwordDto.getOldPwd(), null)){
                message = new Message(MessageType.MSG_TYPE_ERROR, "原密码错误");
                return new ResponseEntity<Message>(message, HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "新密码不能为空"), HttpStatus.OK);
        }
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        Zam003WxResult codeResult = new Zam003WxResult();
        try {
            String result = tmInterface.updateUserWx(
                    "updateUserWx", wechatProperties.getTimestamp(),wechatProperties.getSign(),
                    operator,
                    passwordDto.getNewPwd(),
                    operator
            );
            codeResult = objectMapper.readValue(result, Zam003WxResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            SysUser user = getSysUser(codeResult.getZam003Re().getXtczdm(), "taimeng");
            if(sysUser == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
            }
            user.setXTCZMM(codeResult.getZam003Re().getXTCZMM());
            sysUserRepository.save(user);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 账户展示(已停用)
     * @param userName
     * @return
     */
    public ResponseEntity<Message> userWxList(String userName) {
        SysUser sysUser = getSysUser(userName, "taimeng");
        if(sysUser == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账号不存在"), HttpStatus.OK);
        }
        Long xtjgid = sysUser.getXTJGID(); //系统机构id
        String xtbmmc = sysUser.getXTBMMC() == null ||
                sysUser.getXTBMMC().isEmpty() ? "ZJL" : sysUser.getXTBMMC(); //经销商部门，为空默认经销商（即总经理）
        UserListDto userListDto = new UserListDto();
        userListDto.setBranchOfficeList(new ArrayList());
        userListDto.setTeamList(new ArrayList());
        String name  = userName;
        if("ZJL".equals(xtbmmc)){
            SysUserRole zjl = sysUserRoleRepository.getZJL(xtjgid);
            if(zjl == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账号暂无法开设下级账号，请联系客服"), HttpStatus.OK);
            } else {
                name = zjl.getXtczdm();
            }
        }
        List<SysUserRole> roleList = sysUserRoleRepository.getGroupList(name);
        if ("XSJL".equals(xtbmmc)){
            userListDto.setType("2");
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, userListDto), HttpStatus.OK);
        } else if("ZJL".equals(xtbmmc)){
            userListDto.setType("0");
            if(roleList == null || roleList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, userListDto), HttpStatus.OK);
            }
            List<BranchOfficeDto> branchOfficeList = new ArrayList();
            for(SysUserRole item : roleList){
                if("QYJL".equals(item.getXtbmdm())){
                    BranchOfficeDto dto = new BranchOfficeDto();
                    dto.setUserName(item.getXtczdm());
                    List<String> teamList = new ArrayList();
                    for(SysUserRole role : roleList){
                        if(item.getXTBCID().equals(role.getSJBMDM())){
                            teamList.add(role.getXtczdm());
                        }
                    }
                    dto.setTeamList(teamList);
                    branchOfficeList.add(dto);
                }
            }
            userListDto.setBranchOfficeList(branchOfficeList);
        } else if ("QYJL".equals(xtbmmc)){
            userListDto.setType("1");
            if(roleList == null || roleList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, userListDto), HttpStatus.OK);
            }
            List<String> teamList = new ArrayList();
            for(SysUserRole item : roleList){
                if("XSJL".equals(item.getXtbmdm())){
                    teamList.add(item.getXtczdm());
                }
            }
            userListDto.setTeamList(teamList);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, userListDto), HttpStatus.OK);
    }

    /**
     * 单一特定角色同步
     *
     * @param userName
     * @return
     */
    public ResponseEntity<Message> syncOneRole(String userName) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        List<SysUserRole> tmSysUserRole = null;
        try {
            tmSysUserRole = objectMapper.readValue(tmInterface.getSysUserRole("UserRelationList", "adminuser",wechatProperties.getSign(), wechatProperties.getTimestamp()), SysUserRoleCallBackDto.class).getZam017();
            SysUserRole userRole = null;
            for(SysUserRole sysUserRole : tmSysUserRole){
                if(userName.equals(sysUserRole.getXtczdm())){
                    userRole = sysUserRoleRepository.findByXtczdm(userName);
                    if(userRole != null){
                        sysUserRoleRepository.delete(userRole);
                    }
                    sysUserRoleRepository.save(sysUserRole);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "同步角色失败"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }
    /**
     * 角色同步
     *
     * @return
     */
    public void syncRole(){
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        List<SysUserRole> tmSysUserRole = null;
        try {
            tmSysUserRole = objectMapper.readValue(tmInterface.getSysUserRole("UserRelationList", "adminuser",wechatProperties.getSign(), wechatProperties.getTimestamp()), SysUserRoleCallBackDto.class).getZam017();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(tmSysUserRole != null && tmSysUserRole.size() != 0){
            sysUserRoleRepository.deleteAllInBatch();
            sysUserRoleRepository.save(tmSysUserRole);
        }
    }


    public List<String> getUserList(String userName, SysUser sysUser) {
        List<String> userNameList = new ArrayList();
        Long xtjgid = sysUser.getXTJGID(); //系统机构id
        userNameList.add(userName);
        String xtbmmc = sysUser.getXTBMMC() == null ||
                sysUser.getXTBMMC().isEmpty() ? "ZJL" : sysUser.getXTBMMC(); //经销商部门，为空默认经销商（即总经理）
        String name  = userName;
        if("ZJL".equals(xtbmmc)){
            SysUserRole zjl = sysUserRoleRepository.getZJL(xtjgid);
            if(zjl == null){
                return userNameList;
            } else {
                name = zjl.getXtczdm();
            }
        }
        List<SysUserRole> roleList = sysUserRoleRepository.getApplyGroupList(name);
        if(roleList != null){
            for(SysUserRole item : roleList){
                userNameList.add(item.getXtczdm());
            }
        }
        return userNameList;
    }


    public ResponseEntity<Message> updateRole(String userCode,String[] resources) {
        if(userCode == null || userCode.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "修改角色失败"), HttpStatus.OK);
        }
        List<TmSysRole> sysRoles = sysRoleRepository.findByUserNameAndCustomer(userCode, "taimeng");
        if(!sysRoles.isEmpty()){
            sysRoleRepository.delete(sysRoles);
        }
//        String[] strings = {"yuqilv","ocrtest"};
        TmSysResource sysResource;
        for(String src : resources){
            TmSysRole tmSysRole = new TmSysRole();
            sysResource = sysResourceRepository.findByName(src);
            List<TmSysResource> tmSysResources = new ArrayList<>();
            tmSysResources.add(sysResource);
            tmSysRole.setCustomer("taimeng");
            tmSysRole.setUserName(userCode);
            tmSysRole.setTmSysResources(tmSysResources);
            sysRoleRepository.save(tmSysRole);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    public ResponseEntity<Message> getRole(String userCode) {
        List<TmSysRole> sysRoles = sysRoleRepository.findByUserNameAndCustomer(userCode, "taimeng");
        List<TmSysResource> sysResources = sysResourceRepository.findDistinctByTmSysRolesIn(sysRoles);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysResources), HttpStatus.OK);
    }
}
