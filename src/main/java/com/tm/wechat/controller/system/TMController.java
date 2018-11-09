package com.tm.wechat.controller.system;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.RedisProperties;
import com.tm.wechat.dao.*;
import com.tm.wechat.domain.*;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sysUser.LoginRecordDto;
import com.tm.wechat.dto.sysUser.RoleDto;
import com.tm.wechat.dto.sysUser.UserLoginInfoDto;
import com.tm.wechat.security.PermissionService;
import com.tm.wechat.service.TMInterface;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.service.sale.SaleService;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.PushMsgToSingleDeviceUtil;
import com.tm.wechat.utils.commons.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LEO on 16/8/25.
 */
@RestController
@RequestMapping()
public class TMController {

    @Autowired
    private TMInterface tmInterface;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LoginRecordRepository loginRecordRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysResourceRepository sysResourceRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BaseService baseService;


//    @Autowired
//    private PushMsgToSingleDeviceUtil pushMsgToSingleDeviceUtil;

    /**
     * 拉取用户信息
     * （登录在 CustomUserDetailsService ，现在这个接口只作验证用户及拉取用户信息） -- 2018/10/10 14:07 By ChengQiChuan
     * @param headerParam
     * @param loginAddress  登录地址
     * @param deviceType    设备类型（友盟推送）
     * @param login
     * @param user          Principal
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<Message> getUser(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                           @RequestParam(required = false) String loginAddress, String deviceType, String login, Principal user){
        String customer = JsonPath.from(headerParam).get("systemflag");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取消息头中的用户标识
        String deviceToken = request.getHeader("deviceToken");
        String userName = user.getName();
        return ycLogin(headerParam, loginAddress, user, customer, userName, deviceToken, deviceType);
        // 问过雨生，现在亚驰的系统已经完全没有使用了，所以现在下面这段代码已经没有什么作用了
        // 下面这段代码是 2017/5/31 胡宗诚 注释的，所以应该在2017年5月31日亚驰应该就停用了 -- 2018/10/10 14:08 By ChengQiChuan
//        if ("taimeng".equals(customer)) {
//            return tmLogin(headerParam, loginAddress, deviceType, login, customer, userName);
//        } else {
//            return ycLogin(headerParam, loginAddress, user, customer, userName);
//        }
    }

    /**
     * 亚驰登录   现在太盟宝登录也使用此方法，
     * （登录好像有个单独的单点登录服务，现在这个接口只作验证用户及拉取用户信息） -- 2018/10/10 14:37 By ChengQiChuan
     * @param headerParam
     * @param loginAddress
     * @param user
     * @param customer
     * @param userName
     * @param deviceToken
     * @param deviceType
     * @return
     */
    private ResponseEntity<Message> ycLogin(@RequestHeader(value = "Header-Param", defaultValue = "{\"systemflag\":\"taimeng\"}") String headerParam, @RequestParam(required = false) String loginAddress, Principal user, String customer, String userName, String deviceToken, String deviceType) {
        // 如果deviceToken 不为空那么将用户存入redis中
        if(!CommonUtils.isNull(deviceToken)){
            String uuid = UUID.randomUUID().toString().replace("-", "");
            uuid = deviceToken +  ":" +  uuid;
            String key = CommonUtils.loginkey + userName;
            UserLoginInfoDto userDto = new UserLoginInfoDto(deviceToken, uuid, userName);
            redisRepository.save(key, userDto, 7 * 24 * 3600);
        }
        //判断是否是总部人员
        Boolean isHplUser = permissionService.isHplUser(userName, headerParam);
        //获取用户角色
        RoleDto roleDto = permissionService.getRole(userName, headerParam);
        Map map = Maps.newHashMap();
        map.put("isHplUser", isHplUser);
        map.put("userRole", roleDto);
        //获取用户上次登录信息
        LoginRecord loginRecordb = loginRecordRepository.findTop1ByUserNameAndCustomerOrderByTimeDesc(userName, customer);
        LoginRecordDto loginRecordDto = new LoginRecordDto();
        //如果用户有上次登录记录
        if(loginRecordb != null){
            BeanUtils.copyProperties(loginRecordb, loginRecordDto);
        }


        map.put("loginRecord", loginRecordDto);
        map.put("userName", user.getName());

        SysUser sysUser = baseService.getSysUser(userName, customer);

        map.put("levelId", sysUser.getXTJGID());
        String userType = "3";
        if("22".equals(sysUser.getXTJSID())){
            SysUserRole sysUserRole = sysUserRoleRepository.getUserType(sysUser.getXtczdm());
            if(sysUserRole != null){
//                if ("QYJL".equals(sysUserRole.getXtbmdm())) {
//                    userType = "1";
//                } else if ("XSJL".equals(sysUserRole.getXtbmdm())){
//                    userType = "2";
//                }else {
                userType = "0";
//                }
            } else {
                SysUser saller = sysUserRepository.beSaller(userName, customer);
                if(saller != null){
                    userType = "0";
                }
            }
        }
        map.put("userType", userType);
        String code = sysUserService.getCode(sysUser.getXtczdm(),customer);
        map.put("code", code);
        try {
            ResponseEntity<Message> responseEntity = saleService.getChildLevel(headerParam, sysUser.getXTJGID().toString(), user.getName());
            Map temp = (Map)responseEntity.getBody().getData();
            map.put("areaName", temp.get("areaName"));
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取区域异常，稍后再试"), HttpStatus.OK);
        }
        List<TmSysRole> sysRoles = sysRoleRepository.findByUserNameAndCustomer(userName, customer);
        List<TmSysResource> sysResources = sysResourceRepository.findDistinctByTmSysRolesIn(sysRoles);
        map.put("permissions", sysResources);
        if(null != loginAddress && !"".equals(loginAddress)){
            LoginRecord loginRecord = new LoginRecord(loginAddress, userName, deviceType, deviceToken);
            loginRecordRepository.save(loginRecord);
        }
        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, map);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    /**
     * 退出登录         测试安卓app及网页登录，此方法目前没有被调用 （貌似登录和退出登录使用了专门的单点登录） -- 2018/10/10 14:56 By ChengQiChuan
     * @param headerParam
     * @param user
     */
    @RequestMapping(value = "/tmLogout", method = RequestMethod.GET)
    public void logout(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam, Principal user){
        // 太盟宝和亚驰 之前都是调用的本系统接口，所以很多接口都做了区分，
        // systemflag 正常情况下，app请求的时候会传回来，如果没有传，那么在这个地方会给一个默认的值 taimeng -- 2018/10/10 12:31 By ChengQiChuan
        String customer = JsonPath.from(headerParam).get("systemflag");
        // 查询到登录记录，并增加退出登录的信息 -- 2018/10/10 12:35 By ChengQiChuan
        LoginRecord newestLoginRecord =
                loginRecordRepository.findTop1ByUserNameAndCustomerOrderByTimeDesc(user.getName(), customer);
        if (newestLoginRecord != null) {
            newestLoginRecord.setEffectiveFlg("0");
            newestLoginRecord.setLogoutTime(new Date());
            loginRecordRepository.save(newestLoginRecord);
        }
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String queryForGet(Principal user,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "baczlb", required = false) String baczlb,
            @RequestParam(value = "bacjdm", required = false) String bacjdm,
            @RequestParam(value = "baqsrq", required = false) String baqsrq,
            @RequestParam(value = "bajsrq", required = false) String bajsrq,
            @RequestParam(value = "basqbh", required = false) String basqbh,

            /**
             * 3.5.	查询合同还款计划明细表
             */
            @RequestParam(value = "basqxm", required = false) String basqxm,
            @RequestParam(value = "bazjhm", required = false) String bazjhm
    ){
        return tmInterface.queryForGet(url, baczlb, bacjdm, baqsrq, bajsrq, user.getName(), "1429604397531", "12940581fbbf2df3b9739fe34a3344b8", basqbh,
                basqxm, bazjhm);
    }


    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String queryForPost(Principal user,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "basqbh", required = false) String basqbh,

            /**
             * 3.1.	查询合同还款计划列表
             * url:localhost:8080/TM/query?url=queryContractRepayplanList&operator=admin&basqbh=&bakhmc=&bakssj=&bajssj=&bahkzt=&bapage=1
             */
            @RequestParam(value = "bakhmc", required = false) String bakhmc,
            @RequestParam(value = "bakssj", required = false) String bakssj,
            @RequestParam(value = "bajssj", required = false) String bajssj,
            @RequestParam(value = "bahkzt", required = false) String bahkzt,
            @RequestParam(value = "bapage", required = false) String bapage,

            /**
             * 3.1.	相询合同申请状态列表
             * url:localhost:8080/TM/query?url=queryContractStateList&operator=admin&basqbh=&bakhmc=&bakssj=&bajssj=&basqzt=&bapage=1
             */
            @RequestParam(value = "basqzt", required = false) String basqzt
    ){
        return tmInterface.queryForPost(url, user.getName(), "1429604397531", "12940581fbbf2df3b9739fe34a3344b8", basqbh,
                bakhmc, bakssj, bajssj, bahkzt, bapage, basqzt);
    }

    @RequestMapping(value = "/tj/query", method = RequestMethod.GET)
    public String queryForTmtj(Principal user,
           @RequestParam(value = "url", required = false) String url,

           /**
            * 1.1	统计量按年/月查询
            * url:http://happyleasing.cn/TMZL/app/tmtjapi.htm!?.url=qryStatByYearOrMonth&userId=admin&type=2&userLevel=81&year=2016&month=0
            */
           @RequestParam(value = "type", required = false) String type,
           @RequestParam(value = "userLevel", required = false) String userLevel,
           @RequestParam(value = "year", required = false) String year,
           @RequestParam(value = "month", required = false) String month,
           /**
                * 统计量按时间段查询
                * localhost:8081/TM/tj/query?url=qryStatByTimeSlot&type=2&userLevel=81&startDate=20160101&endDate=20170101
            */
           @RequestParam(value = "startDate", required = false) String startDate,
           @RequestParam(value = "endDate", required = false) String endDate
    ){
       return tmInterface.queryForTmtj(url, user.getName(), type, userLevel, year, month, startDate, endDate);
    }


    /**
     * 获取超级验证码
     * @return
     */
    @RequestMapping(value = "/getAdminMsgCode", method = RequestMethod.GET)
    public ResponseEntity<Message> getAdminMsgCode(Principal user){
        return sysUserService.getAdminMagCode();
    }


//  问过雨生，下面这段代码已经没有使用了。 -- 2018/10/10 14:17 By ChengQiChuan
//
//    /**
//     * 太盟宝登录
//     * @param headerParam
//     * @param loginAddress
//     * @param deviceType
//     * @param login
//     * @param customer
//     * @param userName
//     * @return
//     */
//    private ResponseEntity<Message> tmLogin(@RequestHeader(value = "Header-Param", defaultValue = "{\"systemflag\":\"taimeng\"}")
//                                             String headerParam, @RequestParam(required = false) String loginAddress, String deviceType,
//                                            String login, String customer, String userName) {
//        String deviceId = JsonPath.from(headerParam).get("channelId");
//        if(deviceId == null && !"web".equals(deviceType)){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//        }
//        Boolean isHplUser = permissionService.isHplUser(userName, headerParam);
//        RoleDto roleDto = permissionService.getRole(userName, headerParam);
//        Map map = Maps.newHashMap();
//        map.put("isHplUser", isHplUser);
//        map.put("userRole", roleDto);
//        map.put("userName", userName);
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName, customer);
//        map.put("levelId", sysUser.getXTJGID());
//        String userType = "3";
//        if("22".equals(sysUser.getXTJSID())){
//            SysUserRole sysUserRole = sysUserRoleRepository.getUserType(sysUser.getXtczdm());
//            if(sysUserRole != null){
//                    userType = "1";
//            } else {
//                SysUser saller = sysUserRepository.beSaller(userName, customer);
//                if(saller != null){
//                    userType = "0";
//                }
//            }
//        }
//        map.put("userType", userType);
//        try {
//            ResponseEntity<Message> responseEntity = saleService.getChildLevel(headerParam, sysUser.getXTJGID().toString(), userName);
//            Map temp = (Map) responseEntity.getBody().getData();
//            map.put("areaName", temp.get("areaName"));
//        } catch (Exception e){
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系区域异常，稍后再试"), HttpStatus.OK);
//        }
//        List<TmSysRole> sysRoles = sysRoleRepository.findByUserNameAndCustomer(userName, customer);
//        List<TmSysResource> sysResources = sysResourceRepository.findDistinctByTmSysRolesIn(sysRoles);
//        map.put("permissions", sysResources);
//        List<LoginRecord> loginRecords = loginRecordRepository.findByUserNameAndCustomerOrderByTimeDesc(userName, customer);
//        LoginRecordDto loginRecordDto = new LoginRecordDto();
//        if (0 != loginRecords.size()) {
//            BeanUtils.copyProperties(loginRecords.get(0), loginRecordDto);
//        }
//        map.put("loginRecord", loginRecordDto);
//
//        //网页正常登录
//        if("web".equals(deviceType)){
//            Message message = new Message(MessageType.MSG_TYPE_SUCCESS, map);
//            return new ResponseEntity<Message>(message, HttpStatus.OK);
//        }
//
//        // 准备此次登录insert的loginRecord数据
//        LoginRecord loginRecord = new LoginRecord(loginAddress, userName, deviceId, deviceType);
//        // 查找此用户名和客户标识下最新的登录历史记录的数据。
//        LoginRecord newestLoginRecord =
//                loginRecordRepository.findTop1ByUserNameAndCustomerOrderByTimeDesc(userName, customer);
//        // 若此账号已在其他设备登录
//        if (newestLoginRecord != null && "1".equals(newestLoginRecord.getEffectiveFlg()) && !deviceId.equals(newestLoginRecord.getDeviceId()) && "1".equals(login)) {
//            // 更新上一次登录的数据
//            newestLoginRecord.setEffectiveFlg("0");
//            newestLoginRecord.setLogoutTime(new Date());
//            loginRecordRepository.save(newestLoginRecord);
//            // 插入此次登录的数据
//            loginRecordRepository.save(loginRecord);
//
//            //在第一次调用登录接口时才可发送推送
//            try {
//                // 调用百度云SDK插件给上次登录的手机推送消息
//                PushMsgToSingleDeviceUtil pushMsgToSingleDeviceUtil= new PushMsgToSingleDeviceUtil();
//                pushMsgToSingleDeviceUtil.pushMsgToSingleDevice(newestLoginRecord.getDeviceId(), newestLoginRecord.getDeviceType());
//            } catch (PushClientException e) {
//                e.printStackTrace();
//            } catch (PushServerException e) {
//                e.printStackTrace();
//            }
//
//
//        } else if (newestLoginRecord == null || !"1".equals(newestLoginRecord.getEffectiveFlg())){
//            // 此账号没有在其他手机上登录。
//            loginRecordRepository.save(loginRecord);
//        }
//        Message message = new Message(MessageType.MSG_TYPE_SUCCESS, map);
//        return new ResponseEntity<Message>(message, HttpStatus.OK);
//    }

}