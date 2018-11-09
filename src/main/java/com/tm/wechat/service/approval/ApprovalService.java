package com.tm.wechat.service.approval;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tm.wechat.config.RedisProperties;
import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.config.WzProperties;
import com.tm.wechat.consts.*;
import com.tm.wechat.dao.*;
import com.tm.wechat.dao.applyOnline.OriginTypeRepository;
import com.tm.wechat.dao.applyOnline.OriginTypeUserWhiteRepository;
import com.tm.wechat.domain.*;
import com.tm.wechat.domain.originType.OriginTypeUserWhite;
import com.tm.wechat.dto.SubmitStateDto;
import com.tm.wechat.dto.apply.ApplyLocalInfoDto;
import com.tm.wechat.dto.approval.*;
import com.tm.wechat.dto.approval.icbc.ApprovalFileDto;
import com.tm.wechat.dto.approval.icbc.ApprovalSubmitDto;
import com.tm.wechat.dto.approval.xw_Bank.*;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.resu.Resul;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.dto.webank.AppUserBasicInfoDto;
import com.tm.wechat.dto.webank.*;
import com.tm.wechat.service.JsonDecoder;
import com.tm.wechat.service.aatestInterface.TestCoreSystemInterface;
import com.tm.wechat.service.applyOnline.ApplyService;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.order.OrderService;
import com.tm.wechat.service.sale.SaleInterface;
import com.tm.wechat.service.sysUser.LeaseWeChatInterface;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.IPAddressUtils;
import com.tm.wechat.utils.RegexUtils;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.DateUtils;
import com.tm.wechat.utils.commons.ItemDataTypeEnum;
import com.tm.wechat.utils.commons.ItemSerialize;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.tm.wechat.utils.commons.ItemDeserialize.deserialize;

/**
 * Created by LEO on 16/9/13.
 */
@Service
public class ApprovalService {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VersionProperties versionProperties;

    @Autowired
    private ApplyInfoNewRepository applyInfoNewRepository;

    @Autowired
    private ApplyRecordRepository applyRecordRepository;

    @Autowired
    private ApprovalInterface approvalInterface;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    private Gson gson;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private LeaseWeChatInterface leaseWeChatInterface;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private WzProperties wzProperties;

    @Autowired
    private WzApplyInterface wzApplyInterface;

    @Autowired
    private WzApplyInfoRepository wzApplyInfoRepository;

    @Autowired
    private CoreSystemInterface coreSystemInterface;

    @Autowired
    private RedisRepository redisRepository;

    //提交预审批测试接口
    @Autowired
    private TestCoreSystemInterface testCoreSystemInterface;

    @Autowired
    private OriginTypeRepository originTypeRepository;

    @Autowired
    private OriginTypeUserWhiteRepository originTypeUserWhiteRepository;

    @Autowired
    private WechatProperties wechatProperties;

    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private SaleInterface saleInterface;

    @Autowired
    private BankNameOriginRepository bankNameOriginRepository;

    @Autowired
    private ApplyInfoStatusRepository applyInfoStatusRepository;

    /**
     * 通过 uniqueMark 检查预审批是否存在
     * @param uniqueMark
     * @return
     */
    private ApplyInfoNew checkApplyByuniqueMark(String uniqueMark){
        return applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark,versionProperties.getVersion());
    }

    /**
     * 验证身份证信息是否填写正确
     * @param idCardInfoDto     身份证信息
     * @return
     */
    private String checkIdCardInfo(IdCardInfoDto idCardInfoDto) {
        if(idCardInfoDto == null){
            return "idCardInfoDto为空，请补全身份证信息";
        }else if(CommonUtils.isNull(idCardInfoDto.getName())){
            return "请填写姓名";
        }else if(CommonUtils.isNull(idCardInfoDto.getProductSource())){
            return "产品来源为空";
        }else if(CommonUtils.isNull(idCardInfoDto.getIdCardNum())) {
            return "请填写身份证号";
        }else if(!RegexUtils.checkIdCard(idCardInfoDto.getIdCardNum())) {
            // 检验身份证号是否通过正则 -- By ChengQiChuan 2018/10/16 15:26
            return "请输入正确的身份证号";
        }else if(CommonUtils.isNull(idCardInfoDto.getIssuingAuthority())){
            return "请输入身份证签发机关";
        }else if(CommonUtils.isNull(idCardInfoDto.getFrontImg())){
            return "请上传身份证正面照片";
        }else if(CommonUtils.isNull(idCardInfoDto.getBehindImg())){
            return "请上传身份证背面照片";
        }
        return null;
    }

    /**
     * 验证紧急联系人信息
     * @param idCardInfoDto
     * @param otherInfoDto
     * @param contactInfoDto
     * @return
     */
    private ResponseEntity<Message> checkContactInfo(IdCardInfoDto idCardInfoDto, OtherInfoDto otherInfoDto, ContactInfoDto contactInfoDto) {
        if(CommonUtils.isNull(contactInfoDto.getContact1Name()) || CommonUtils.isNull(contactInfoDto.getContact2Name())
                || CommonUtils.isNull(contactInfoDto.getContact1Mobile()) || CommonUtils.isNull(contactInfoDto.getContact2Mobile())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全紧急联系人信息"), HttpStatus.OK);
        }
        if(idCardInfoDto.getName().equals(contactInfoDto.getContact1Name()) || idCardInfoDto.getName().equals(contactInfoDto.getContact2Name())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"紧急联系人不能是申请人本人"), HttpStatus.OK);
        }
        if(otherInfoDto.getPhoneNumber().equals(contactInfoDto.getContact1Mobile()) || otherInfoDto.getPhoneNumber().equals(contactInfoDto.getContact2Mobile())
                || contactInfoDto.getContact1Mobile().equals(otherInfoDto.getVicePhoneNumber()) || contactInfoDto.getContact2Mobile().equals(otherInfoDto.getVicePhoneNumber())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"紧急联系人手机号不能是申请人手机号"), HttpStatus.OK);
        }
        return null;
    }

    /**
     * 查询是否存在旧数据，如果存在则需要进行操作（在线助力融姓名身份证号改变需要重新验证）
     * @param idCardInfoDto
     * @param uniqueMark
     * @param version
     * @return
     */
    private void checkOldApproval(IdCardInfoDto idCardInfoDto, String uniqueMark, String version) throws Exception {
        List<Approval> approvalListOld;
        ApplyFromDto resultDto;
        approvalListOld = approvalRepository.findIdCardInfo(uniqueMark, version);
        if (!approvalListOld.isEmpty()) {

            resultDto = deserialize(ApplyFromDto.class, approvalListOld);

            //获取身份证信息姓名和产品来源
            String name =  resultDto.getIdCardInfoDto().getName();
            String idCardNum = resultDto.getIdCardInfoDto().getIdCardNum();
            String product = resultDto.getIdCardInfoDto().getProductSource();

            //微盟贷（在线助力融）姓名身份证号改变需要重新验证
            if(OriginType.ONLINE_HELP.value().equals(product)){
                //基本信息改变重置银行卡信息
                if(!idCardInfoDto.getName().equals(name) || !idCardInfoDto.getIdCardNum().equals(idCardNum)){
                    List<Approval> approvalBankListOld = new ArrayList();
                    approvalBankListOld = approvalRepository.findBankInfo(uniqueMark, version);
                    if (!approvalBankListOld.isEmpty()) {
                        approvalRepository.delete(approvalBankListOld);
                    }
                }
            }
            approvalRepository.delete(approvalListOld);
        }
    }


    /**
     * 获取到用户可新建的产品类型
     * @return
     */
    public ResponseEntity<Message> getOriginType(String name) {
        List<com.tm.wechat.domain.originType.OriginType> originTypeList = originTypeRepository.findByActive("1");
        List<OriginTypeUserWhite> originTypeUserWhiteList = new ArrayList<>();
        //如果页面传过来的name不为空则查白名单
        if(CommonUtils.isNotNull(name)){
            originTypeUserWhiteList = originTypeUserWhiteRepository.findByFpCode(name);
        }
        //使用java8特性流处理取出 code 集合
        List<String> originTypeCodeList = originTypeList.stream().map(com.tm.wechat.domain.originType.OriginType::getCode).collect(Collectors.toList());
        List<String> originTypeUserWhiteCodeList = originTypeUserWhiteList.stream().map(OriginTypeUserWhite::getCode).collect(Collectors.toList());
        //去重处理
        Set<String> set = new HashSet<>();
        set.addAll(originTypeCodeList);
        set.addAll(originTypeUserWhiteCodeList);
        //返回结果给页面
        List<String> resultList = new ArrayList<>();
        resultList.addAll(set);

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, resultList), HttpStatus.OK);
    }




    /**
     * 生成UUID（唯一标识） 获取 UniqueMark
     *
     * @return
     */
    public ResponseEntity<Message> getUniqueMark() {
        String uniqueMark = UUID.randomUUID().toString().replace("-", "");
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, uniqueMark), HttpStatus.OK);
    }

//    /**
//     * 新增待提交审批信息（产品来源信息）
//     *
//     * @param productSourceDto
//     * @param userName
//     * @return
//     */
//    @Transactional
//    public ResponseEntity<Message> addProductSourceInfo(ProductSourceDto productSourceDto, String uniqueMark, String userName) {
//        if(productSourceDto == null || CommonUtils.isNull(productSourceDto.getProductSource())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全产品信息"), HttpStatus.OK);
//        }
//        // 版本号
//        String version = versionProperties.getVersion();
//        ApplyFromDto totalDto = new ApplyFromDto();
//        totalDto.setProductSourceDto(productSourceDto);
//        List<Approval> approvalListNew = new ArrayList();
//        List<Approval> approvalListOld = new ArrayList();
//        approvalListOld = approvalRepository.findProductSourceInfo(uniqueMark, version);
//        ApplyFromDto resultDto = new ApplyFromDto();
//        try {
//            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//        }
//        if (!approvalListOld.isEmpty()) {
//            try {
//                resultDto = deserialize(ApplyFromDto.class, approvalListOld);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//            }
//            approvalRepository.delete(approvalListOld);
//        }
//        if (!approvalListNew.isEmpty()) {
//            approvalRepository.save(approvalListNew);
//        }
//        ApplyInfo applyInfo = applyInfoRepository.findByApprovalUuidAndVersion(uniqueMark, version);
//        ApplyInfo newApplyInfo = new ApplyInfo();
//        if (applyInfo == null) {
//            newApplyInfo.setApprovalUuid(uniqueMark);
//            newApplyInfo.setStatus(ApprovalType.NEW.code());
//            newApplyInfo.setCreateUser(userName);
//            newApplyInfo.setVersion(version);
//            //产品来源
//            newApplyInfo.setOrigin(OriginType.getCodeByValue(productSourceDto.getProductSource()));
//            applyInfoRepository.save(newApplyInfo);
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//        }
//        //产品来源更改更新来源
//        //有其他来源（如微信：1）则不能更新
//        if(!OriginType.WE_BANK.code().equals(applyInfo.getOrigin())){
//            applyInfo.setOrigin(OriginType.getCodeByValue(productSourceDto.getProductSource()));
//        }
//        applyInfoRepository.save(applyInfo);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//    }

    /**
     * 新增待提交审批信息（身份证信息）
     *
     * @param idCardInfoDto
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addIdentityInfo(IdCardInfoDto idCardInfoDto, String uniqueMark, String userName) {
        /**
         * 防止重复提交
         */
        Object objRedis = redisRepository.get("addIdentityInfo_uniqueMark"+uniqueMark);
        if(objRedis != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请勿重复提交"), HttpStatus.OK);
        }
        redisRepository.save(RedisProperties.addIdentityInfo_uniqueMark+uniqueMark,uniqueMark,RedisProperties.addIdentityInfo_uniqueMarkTime);

        //打印用户输入的信息
        logger.info("idCardInfoDto={}", JSONObject.fromObject(idCardInfoDto).toString(2));

        //验证身份证信息是否完整
        String checkString = checkIdCardInfo(idCardInfoDto);
        if (checkString != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, checkString), HttpStatus.OK);
        }

        // 获取当前系统版本号 （这个版本号现在应该没什么用了）
        String version = versionProperties.getVersion();

        //预审批信息
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setIdCardInfoDto(idCardInfoDto);


        //查询之前的信息，如果更改则执行操作 （在线助力融姓名身份证号改变需要重新验证）
        try {
            checkOldApproval(idCardInfoDto, uniqueMark, version);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "旧数据处理失败"), HttpStatus.OK);
        }


        //序列化 Approval 列表参数
        List<Approval> approvalListNew = new ArrayList();
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //保存预审批横向存储数据信息
        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }

        //查询预审批是否有数据
        ApplyInfoNew applyInfoNew = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        //如果预审批表数据为空，那么创建新的记录
        if (applyInfoNew == null) {
            applyInfoNew = new ApplyInfoNew();
            applyInfoNew.setApprovalUuid(uniqueMark);
            applyInfoNew.setHplStatus(ApprovalType.NEW.code());
            applyInfoNew.setCreateUser(userName);
            applyInfoNew.setVersion(version);
        }
        //增加审批记录表
        ApplyRecord applyRecord = new ApplyRecord();
        applyRecord.setApprovalUuid(uniqueMark);
        applyRecord.setStatus(ApprovalType.NEW.code());
        applyRecord.setReason("创建订单");
        applyRecord.setCreateUser(userName);


        // 更新产品类型,注：若有其他来源（如微信：1）则不能更新
        if(!OriginType.WE_BANK.code().equals(applyInfoNew.getOrigin())){
//            //如果是微盟贷（在线助力融）这里不知道为什么名字没有统一，现在也不敢乱改，则需要设置类型为 1
//            if(OriginType.ONLINE_HELP.value().equals(idCardInfoDto.getProductSource())){
//                applyRecord.setOrigin(OriginType.ONLINE_HELP.code());
//                applyInfoNew.setOrigin(OriginType.ONLINE_HELP.code());
//            }else {
                applyRecord.setOrigin(OriginType.getCodeByValue(idCardInfoDto.getProductSource()));
                applyInfoNew.setOrigin(OriginType.getCodeByValue(idCardInfoDto.getProductSource()));
//            }
        }

        //非hpl自营产品和微众(包括 微盟贷（在线助力融） 及 微信端)时；其他的产品需要给一个产品商状态 例如：工行，新网
        if(CommonUtils.isNull(applyInfoNew.getProductStatus()) &&
                (!OriginType.HPL.value().equals(idCardInfoDto.getProductSource()) &&
                 !OriginType.WE_BANK.value().equals(idCardInfoDto.getProductSource()) &&
                 !OriginType.ONLINE_HELP.value().equals(idCardInfoDto.getProductSource())
                )
          ){
            applyInfoNew.setProductStatus(ApprovalType.NEW.code());
        }

        //保存预审批及审批记录
        applyRecordRepository.save(applyRecord);
        applyInfoNewRepository.save(applyInfoNew);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }



// 修改数据库 apply_info 表结构，下面备份 -- By ChengQiChuan 2018/10/15 19:44
//    /**
//     * 新增待提交审批信息（身份证信息）
//     *
//     * @param idCardInfoDto
//     * @param userName
//     * @return
//     */
//    @Transactional
//    public ResponseEntity<Message> addIdentityInfo(IdCardInfoDto idCardInfoDto, String uniqueMark, String userName) {
//        logger.info("idCardInfoDto={}", JSONObject.fromObject(idCardInfoDto).toString(2));
//        String origin = "1";
//        if(redisRepository.get("tmwechat_icbcOrignFlag") != null){
//            origin = (String) redisRepository.get("tmwechat_icbcOrignFlag");
//        }
//        if(idCardInfoDto != null && "工盟贷".equals(idCardInfoDto.getProductSource()) && "0".equals(origin)){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "工盟贷产品系统维护中，暂时无法使用！"), HttpStatus.OK);
//        }
//        if(idCardInfoDto == null || CommonUtils.isNull(idCardInfoDto.getName()) ||
//                CommonUtils.isNull(idCardInfoDto.getProductSource()) ||
//                CommonUtils.isNull(idCardInfoDto.getIdCardNum()) ||
//                CommonUtils.isNull(idCardInfoDto.getFrontImg()) ||
//                CommonUtils.isNull(idCardInfoDto.getBehindImg())){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全身份证信息"), HttpStatus.OK);
//        }
////        if(idCardInfoDto != null && "工盟贷".equals(idCardInfoDto.getProductSource()) && CommonUtils.isNull(idCardInfoDto.getIssuingAuthority())){
////            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全身份证签发机关"), HttpStatus.OK);
////        }
//        // 版本号
//        String version = versionProperties.getVersion();
//        ApplyFromDto totalDto = new ApplyFromDto();
//        totalDto.setIdCardInfoDto(idCardInfoDto);
//        List<Approval> approvalListNew = new ArrayList();
//        List<Approval> approvalListOld = new ArrayList();
//        approvalListOld = approvalRepository.findIdCardInfo(uniqueMark, version);
//        ApplyFromDto resultDto = new ApplyFromDto();
//        try {
//            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//        }
//        //查询之前的信息，判断身份证，姓名和产品来源是否更改
//        if (!approvalListOld.isEmpty()) {
//            try {
//                resultDto = deserialize(ApplyFromDto.class, approvalListOld);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//            }
//            //获取身份证信息姓名和产品来源
//            String name =  resultDto.getIdCardInfoDto().getName();
//            String idCardNum = resultDto.getIdCardInfoDto().getIdCardNum();
//            String product = resultDto.getIdCardInfoDto().getProductSource();
//            //在线助力融姓名身份证号改变需要重新验四
//            if(OriginType.ONLINE_HELP.value().equals(product) || "微盟贷".equals(product)){
//                //基本信息改变重置银行卡信息
//                if(!idCardInfoDto.getName().equals(name) || !idCardInfoDto.getIdCardNum().equals(idCardNum)){
//                    List<Approval> approvalBankListOld = new ArrayList();
//                    approvalBankListOld = approvalRepository.findBankInfo(uniqueMark, version);
//                    if (!approvalBankListOld.isEmpty()) {
//                        approvalRepository.delete(approvalBankListOld);
//                    }
//                }
//            }
//            approvalRepository.delete(approvalListOld);
//        }
//        if (!approvalListNew.isEmpty()) {
//            approvalRepository.save(approvalListNew);
//        }
//        //保存至审批记录表
//        ApplyInfo applyInfo = applyInfoRepository.findByApprovalUuidAndVersion(uniqueMark, version);
//        ApplyInfo newApplyInfo = new ApplyInfo();
//        ApplyRecord applyRecord = new ApplyRecord();
//        applyRecord.setApprovalUuid(uniqueMark);
//        applyRecord.setStatus(ApprovalType.NEW.code());
//        applyRecord.setReason("创建订单");
//        applyRecord.setCreateUser(userName);
//        if (applyInfo == null) {
//            newApplyInfo.setApprovalUuid(uniqueMark);
//            newApplyInfo.setStatus(ApprovalType.NEW.code());
//            newApplyInfo.setCreateUser(userName);
//            newApplyInfo.setVersion(version);
//            //微盟贷 及在线助力融
//            if("微盟贷".equals(idCardInfoDto.getProductSource())){
//                applyRecord.setOrigin(OriginType.ONLINE_HELP.code());
//                newApplyInfo.setOrigin(OriginType.ONLINE_HELP.code());
//            }else {
//                newApplyInfo.setOrigin(OriginType.getCodeByValue(idCardInfoDto.getProductSource()));
//                applyRecord.setOrigin(OriginType.getCodeByValue(idCardInfoDto.getProductSource()));
//            }
//            //工盟贷（工行产品）需要给个工行申请状态
//            if("工盟贷".equals(idCardInfoDto.getProductSource())){
//                newApplyInfo.setIcbcState(ApprovalType.NEW.code());
//            }
//            applyInfoRepository.save(newApplyInfo);
//            applyRecordRepository.save(applyRecord);
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//        }
//        //产品来源更改更新来源
//        //若有其他来源（如微信：1）则不能更新
//        if(!OriginType.WE_BANK.code().equals(applyInfo.getOrigin())){
//            if("微盟贷".equals(idCardInfoDto.getProductSource())){
//                applyRecord.setOrigin(OriginType.ONLINE_HELP.code());
//                applyInfo.setOrigin(OriginType.ONLINE_HELP.code());
//            }else {
//                applyRecord.setOrigin(OriginType.getCodeByValue(idCardInfoDto.getProductSource()));
//                applyInfo.setOrigin(OriginType.getCodeByValue(idCardInfoDto.getProductSource()));
//            }
//        }
//        //工盟贷（工行产品）需要给个工行申请状态
//        if("工盟贷".equals(idCardInfoDto.getProductSource()) && CommonUtils.isNull(applyInfo.getIcbcState())){
//            applyInfo.setIcbcState(ApprovalType.NEW.code());
//        }
//        applyRecordRepository.save(applyRecord);
//        applyInfoRepository.save(applyInfo);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//    }


// 问了君杰，驾驶证信息在很久以前有，现在已经不需要驾驶证信息了 -- By ChengQiChuan 2018/10/19 12:59
//    /**
//     * 新增驾驶证信息
//     *
//     * @param driveLicenceInfoDto
//     * @param uniqueMark
//     * @param userName
//     * @return
//     */
//    @Transactional
//    public ResponseEntity<Message> addDriveInfo(DriveLicenceInfoDto driveLicenceInfoDto, String uniqueMark, String userName) {
//        logger.info("driveLicenceInfoDto={}", JSONObject.fromObject(driveLicenceInfoDto).toString(2));
//
//        if(checkApplyByuniqueMark(uniqueMark) == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
//        }
//
//        // 版本号
//        String version = versionProperties.getVersion();
//        List<Approval> approvalListOld = new ArrayList();
//        List<Approval> approvalListNew = new ArrayList();
//        approvalListOld = approvalRepository.findDriveInfo(uniqueMark, version);
//        ApplyFromDto totalDto = new ApplyFromDto();
//        totalDto.setDriveLicenceInfoDto(driveLicenceInfoDto);
//        try {
//            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//        }
//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
//        if (!approvalListNew.isEmpty()) {
//            approvalRepository.save(approvalListNew);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//    }
//

    /**
     * 获取产品来源
     * @param uniqueMark
     * @return
     */
    public ResponseEntity<Message> getProductSource(String uniqueMark) {
        String version = versionProperties.getVersion();
        //获取身份证信息
        List<Approval> approvalList = approvalRepository.findIdCardInfo(uniqueMark, version);
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            if(approvalList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先完善身份证信息"), HttpStatus.OK);
            }
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"获取产品来源 时 getProductSource 执行 deserialize 异常"), HttpStatus.OK);
        }
        //获取身份证信息姓名和产品来源
        String name =  resultDto.getIdCardInfoDto().getName();
        String idCardNum = resultDto.getIdCardInfoDto().getIdCardNum();
        String product = resultDto.getIdCardInfoDto().getProductSource();
        if(CommonUtils.isNull(name) || CommonUtils.isNull(product) || CommonUtils.isNull(idCardNum)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请完善身份证信息"), HttpStatus.OK);
        }
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        String productValue = OriginType.getCode(applyInfo.getOrigin());
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, productValue), HttpStatus.OK);
    }

    /**
     * 查询预审批申请中状态
     * @param uniqueMark
     * @return
     */
    public ResponseEntity<Message> getApprovalSubmitState(String uniqueMark) {
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"未查询到订单信息"), HttpStatus.OK);
        }
        //获取身份证信息
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        List<ApplyRecord> applyRecordList = applyRecordRepository.findByApprovalUuidOrderByCreateTimeDesc(uniqueMark);
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            if(approvalList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先完善身份证信息"), HttpStatus.OK);
            }
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //获取身份证信息姓名和配偶姓名
        String name =  resultDto.getIdCardInfoDto().getName();
        String mateName = resultDto.getMateInfoDto().getMateName();
        //hpl 天启审批状态，及合作产品商审批状态
        String hplState = applyInfo.getHplStatus();
        String productState = applyInfo.getProductStatus();

        String operation = "";
        SubmitStateDto submitStateDto = new SubmitStateDto();
        List<SubmitStateDetailDto> applicantDtoList = new ArrayList<>();
        List<SubmitStateDetailDto> mateList = new ArrayList<>();
        List<ApplyRecordDto> applyRecordDtoList = new ArrayList<>();
        for(ApplyRecord applyRecord : applyRecordList){
            ApplyRecordDto applyRecordDto = new ApplyRecordDto(applyRecord);
            applyRecordDtoList.add(applyRecordDto);
        }
        SubmitStateDetailDto applicantDto = new SubmitStateDetailDto();
        applicantDto.setProductSource("过滤器");
        applicantDto.setName(name);
        applicantDto.setOperation(operation);

        //判断天启状态
        if("1000".equals(hplState)){
            applicantDto.setState("通过");
            applicantDto.setCode(1);
            //拒绝状态下返回操作和原因，目前只返回天启的原因
        }else if("1100".equals(hplState)){
            applicantDto.setState("拒绝");
            applicantDto.setCode(0);
            applicantDto.setOperation("查看原因");
            applicantDto.setReason(applyInfo.getHplReason());
        }else if("100".equals(hplState)){
            applicantDto.setState("等待审核");
            applicantDto.setCode(2);
        }else if("0".equals(hplState)){
            applicantDto.setState("未提交");
            applicantDto.setCode(0);
        }

        // hpl 自营产品 -- By ChengQiChuan 2018/10/16 12:37
        if(applyInfo.getOrigin().equals(OriginType.HPL.code())){
            applicantDtoList.add(applicantDto);
        //微众产品
        }else if(applyInfo.getOrigin().equals(OriginType.ONLINE_HELP.code())){
            //查询最新微众状态
            ResponseEntity<Message> responseEntity = queryApplyState(uniqueMark);
            if("ERROR".equals(responseEntity.getBody().getStatus())){
                return responseEntity;
            }
            applicantDtoList.add(applicantDto);
            SubmitStateDetailDto applicantWeBankDto = new SubmitStateDetailDto();
            applicantWeBankDto.setName(name);
            applicantWeBankDto.setProductSource("微众");
            WzApplyInfo wzApplyInfo = wzApplyInfoRepository.findTop1ByUniqueMarkOrderByCreateTimeDesc(uniqueMark);
            if(wzApplyInfo == null){
                operation = "重新发送";
                applicantWeBankDto.setState("未获取到订单");
                applicantWeBankDto.setCode(2);
            }else{
                String weBankState = wzApplyInfo.getStatus();
                if("0".equals(weBankState)){
                    applicantWeBankDto.setState("等待审核");
                    applicantWeBankDto.setCode(2);
                }else if("1".equals(weBankState)){
                    String reason = wzApplyInfo.getReason();
                    if(reason.contains("四要素不一致")){
                        operation = "重新发送";
                        applicantWeBankDto.setState("四要素不一致");
                        applicantWeBankDto.setCode(2);
                    }else if(reason.contains("重复提交")){
                        operation = "请联系大区经理";
                        applicantWeBankDto.setState("重复提交");
                        applicantWeBankDto.setCode(0);
                    }else {
                        applicantWeBankDto.setState("拒绝");
                        applicantWeBankDto.setCode(0);
                    }
                }else if("2".equals(weBankState)){
                    applicantWeBankDto.setState("一审通过");
                    applicantWeBankDto.setCode(1);
                }
            }
            applicantWeBankDto.setOperation(operation);
            applicantDtoList.add(applicantWeBankDto);
//        }else if(applyInfo.getOrigin().equals(OriginType.ICBC.code())) {
        // 除了hpl自营产品 及 微众产品，  例：工行，新网
        }else{
            applicantDtoList.add(applicantDto);
            SubmitStateDetailDto applicantProductDto = new SubmitStateDetailDto();
            applicantProductDto.setProductSource(OriginType.getValueByCode(applyInfo.getOrigin()));
            applicantProductDto.setName(name);
            if("1000".equals(productState)){
                applicantProductDto.setState("通过");
                applicantProductDto.setCode(1);
            }else if("1100".equals(productState)){
                applicantProductDto.setState("拒绝");
                applicantProductDto.setCode(0);
            }else if("100".equals(productState)){
                applicantProductDto.setState("等待审核");
                applicantProductDto.setCode(2);
            }else if("300".equals(productState)){
                operation = "补充材料";
                applicantProductDto.setState("退回补充材料");
                applicantProductDto.setCode(0);
            }
            applicantProductDto.setOperation(operation);
            applicantDtoList.add(applicantProductDto);
        }
        submitStateDto.setApplicantList(applicantDtoList);
        submitStateDto.setMateList(mateList);
        submitStateDto.setApplyRecordDtoList(applyRecordDtoList);
        submitStateDto.setApplyInfo(applyInfo);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, submitStateDto), HttpStatus.OK);
    }

    //微众一审提交 (微众预审批提交  提交微众预审批)
    @Transactional
    public ResponseEntity<Message> weBankSubmit(BasicInfoDto basicInfoDto){
        WzApplyInfo oldWzApplyInfo = wzApplyInfoRepository.findTop1ByUniqueMarkOrderByCreateTimeDesc(basicInfoDto.getUniqueMark());
        if(oldWzApplyInfo != null){
            Long s = (System.currentTimeMillis() - oldWzApplyInfo.getCreateTime().getTime()) / (1000 * 60);
            if(s < 1){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "一分钟内请勿频繁提交"), HttpStatus.OK);
            }
        }
        basicInfoDto.setIdCard(basicInfoDto.getIdCard().toUpperCase());
        String wxId = UUID.randomUUID().toString().replace("-", "");
        //提交到主系统
        if (CommonUtils.isNull(basicInfoDto.getIp())){
            String ip = IPAddressUtils.getIpAddress(httpServletRequest);
            if(ip == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取ip地址失败"), HttpStatus.OK);
            }
            basicInfoDto.setIp(ip);
        }
        FirstApplyDto firstApplyDto = buildFirstApplyDto(basicInfoDto);//提交参数构建
        firstApplyDto.setWXID(wxId);//wxId无构造方法
        WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
        logger.info("firstApplyDto={}", JSONObject.fromObject(firstApplyDto).toString(2));
        try {
            //提交一审信息到主系统
            String result = wzApplyInterface.applySubmit(firstApplyDto);
            logger.info("applyResult={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
            //        wzResultCommonDto.setCode("0000");
            //更新申请状态
            if("0000".equals(wzResultCommonDto.getCode())){
                List<Approval> approvalList = addOnlineHelpInfo(basicInfoDto);
                if(approvalList == null || approvalList.isEmpty()){
                    logger.info("保存用户四要素失败,必要参数缺失");
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败,必要参数缺失"), HttpStatus.OK);
                }
                approvalRepository.save(approvalList);
                // 申请信息保存到本地DB中
                WzApplyInfo wzApplyInfo = new WzApplyInfo();
                BeanUtils.copyProperties(basicInfoDto, wzApplyInfo);
                wzApplyInfo.setIdType(CommonUtils.idType);
                wzApplyInfo.setSignStatus(ContractType.NO_SIGN.code());
                wzApplyInfo.setStatus(ApplyType.NEW.code());
                wzApplyInfo.setWxId(wxId);
                wzApplyInfoRepository.save(wzApplyInfo);

                //保存历史记录表
                ApplyRecord newApplyRecord = new ApplyRecord();
                newApplyRecord.setApplyNum(wzApplyInfo.getApplyNum());
                newApplyRecord.setReason(wzApplyInfo.getReason());
                newApplyRecord.setStatus(wzApplyInfo.getStatus());
                newApplyRecord.setApprovalUuid(wzApplyInfo.getUniqueMark());
                newApplyRecord.setOrigin(OriginType.ONLINE_HELP.code());
                newApplyRecord.setCreateUser(basicInfoDto.getName());
                applyRecordRepository.save(newApplyRecord);

                //保存预审批申请状态表,逻辑删除之前的数据
                newApplyInfoStatus(wzApplyInfo.getUniqueMark(), ApplyInfoStatusType.WZ_BANK, basicInfoDto.getName(), "微盟贷 提交预审批", wzApplyInfo.getStatus());

                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交申请失败,参数异常"), HttpStatus.OK);
        }
        //如果主系统中返回消息包含一审已通过，则增加详细提示信息
        if(wzResultCommonDto.getMessage().contains("【一审】已经通过")){

            // 在太盟宝中查找通过一审的记录，如果没有找到则在微信端去查找 -- 2018/9/13 18:30 By ChengQiChuan
            // 一审通过在微信端是 3，太盟宝APP 是 2
            WzApplyInfo wzApplyInfo = wzApplyInfoRepository.findTop1ByIdCardAndStatusAndCreateTimeAfterOrderByCreateTimeDesc(basicInfoDto.getIdCard(),
                    2+"", DateUtils.getBeforeDateByDay(new Date(),-30));
            if (wzApplyInfo != null){
                wzResultCommonDto.setMessage("您当前【一审】已经在 (太盟宝APP:"+wzApplyInfo.getFpName()+") 处通过，不能重复提交。感谢您的申请");
            }else{
                //查找微信端中通过微众预审批的记录
                //调用接口查找微信端中通过微众预审批的记录
                ResponseEntity<Message> responseEntity = leaseWeChatInterface.getwzApplyInfoByIdCard(basicInfoDto.getIdCard());
                //强制转换类型
                HashMap hashMap = (HashMap) responseEntity.getBody().getData();
                if(hashMap != null && !hashMap.isEmpty()){
                    wzResultCommonDto.setMessage("您当前【一审】已经在 (微信:"+hashMap.get("fpName").toString()+") 处通过，不能重复提交。感谢您的申请");
                }else{
                    wzResultCommonDto.setMessage("您当前【一审】已经通过，不能重复提交，但未查到通过相关信息，请联系主系统管理员并提供身份证号、手机号等信息！");
                }
            }

        }else{
            //增加主系统提示
            String mainResult = wzResultCommonDto.getMessage();
            wzResultCommonDto.setMessage("主系统："+mainResult);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, wzResultCommonDto.getMessage()), HttpStatus.OK);
    }



    //微众审批结果查询
    public ResponseEntity<Message> queryApplyState(String uniqueMark){
        // 申请信息保存到本地DB中
        WzApplyInfo wzApplyInfo = wzApplyInfoRepository.findTop1ByUniqueMarkOrderByCreateTimeDesc(uniqueMark);
        if(wzApplyInfo == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到订单信息"), HttpStatus.OK);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "客户手机没有提交成功"), HttpStatus.OK);
        }
        ApplyStateQueryDto applyStateQueryDto = new ApplyStateQueryDto();
        applyStateQueryDto.setWXID(wzApplyInfo.getWxId());
        applyStateQueryDto.setTXN_ID(WzSubmitType.STATEQUERY.code());
        WzResultCommonDto wzResultCommonDto = new WzResultCommonDto();
        logger.info("applyStateQueryDto={}", JSONObject.fromObject(applyStateQueryDto).toString(2));
        try {
            //提交一审信息到主系统
            String result = wzApplyInterface.queryApplyState(applyStateQueryDto);
//            String result = "{\"CODE\":\"0000\",\"DATA\":{\n" +
//                    "\t\"resultReason\":\"人太丑，不予通过\",\n" +
//                    "\t\"applyNum\":\"\",\n" +
//                    "\t\"type\":\"1\",\n" +
//                    "\t\"uniqueMark\":\"db456036688d4433b373f6aa57fda03a\"\n" +
//                    "},\"MESSAGE\":\"人太丑，不予通过\"}";
//            logger.info("applyStateQueryDto={}", result);
            logger.info("applyStateQueryResultDto={}", result);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            wzResultCommonDto = objectMapper.readValue(result, WzResultCommonDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询审批结果失败"), HttpStatus.OK);
        }

        JSONObject resultDto = JSONObject.fromObject(wzResultCommonDto.getData());
        //更新申请状态
        if("0000".equals(wzResultCommonDto.getCode())){
            // 申请信息保存到本地DB中
            wzApplyInfo.setStatus(resultDto.get("type").toString());
            ApplyRecord applyRecord = new ApplyRecord();
            if("2".equals(resultDto.get("type").toString())){
                wzApplyInfo.setReason("一审通过");
                applyRecord.setReason("一审通过");
            }else {
                wzApplyInfo.setReason(resultDto.get("resultReason").toString());
                applyRecord.setReason(resultDto.get("resultReason").toString());
            }
            wzApplyInfo.setApplyNum(resultDto.get("applyNum").toString());
            wzApplyInfoRepository.save(wzApplyInfo);

            //保存预审批申请历史记录
            applyRecord.setStatus(resultDto.get("type").toString());
            applyRecord.setApplyNum(resultDto.get("applyNum").toString());
            applyRecord.setApprovalUuid(uniqueMark);
            applyRecord.setOrigin(OriginType.ONLINE_HELP.code());
            applyRecordRepository.save(applyRecord);


            //更新预审批状态表
            updateApplyInfoStatus(wzApplyInfo.getUniqueMark(), ApplyInfoStatusType.WZ_BANK, wzApplyInfo.getName(), wzApplyInfo.getReason(), wzApplyInfo.getStatus());

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null,wzApplyInfo.getReason()), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, wzResultCommonDto.getMessage()), HttpStatus.OK);
    }



    /**
     * 新增银行卡信息（HPL）
     *
     * @param bankCardInfoDto
     * @param uniqueMark  唯一标识
     * @param userName 用户名
     * @param msgCode 短信验证码
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addBankInfo(BankCardInfoDto bankCardInfoDto, String msgCode, String uniqueMark, String userName) {
        if(checkApplyByuniqueMark(uniqueMark) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
        }

        if(CommonUtils.isNull(bankCardInfoDto.getBankPhoneNum()) || CommonUtils.isNull(bankCardInfoDto.getName())
                || CommonUtils.isNull(bankCardInfoDto.getAccountNum()) || CommonUtils.isNull(bankCardInfoDto.getBankImg())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全信息"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(bankCardInfoDto.getMonthlyIncome())){
            bankCardInfoDto.setMonthlyIncome(null);
        }
        if(CommonUtils.isNull(bankCardInfoDto.getAppId())){
            bankCardInfoDto.setAppId(null);
        }
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
        }
        //获取身份证信息
        List<Approval> approvalList = approvalRepository.findIdCardInfo(uniqueMark, version);
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            if(approvalList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
            }
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }
        //获取身份证信息姓名和产品来源
        String name =  resultDto.getIdCardInfoDto().getName();
        String product = OriginType.getCode(applyInfo.getOrigin());
        if(CommonUtils.isNull(name) || CommonUtils.isNull(product)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
        }
        //产品来源在线助力融，还款卡姓名必须和身份证一致
        if(OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin()) || OriginType.WE_BANK.code().equals(applyInfo.getOrigin())){
            if(!name.equals(bankCardInfoDto.getName())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "在线助力融开户行户名与身份证姓名必须一致"), HttpStatus.OK);
            }
        }
        //校验短信验证码
        if(CommonUtils.isNull(msgCode)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信验证码不可为空"), HttpStatus.OK);
        }
        String message = sysUserService.checkMsgCode(bankCardInfoDto.getBankPhoneNum(), msgCode);
        if(message != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, message), HttpStatus.OK);
        }
        List<Approval> approvalListOld = new ArrayList();
        List<Approval> approvalListNew = new ArrayList();
        approvalListOld = approvalRepository.findBankInfo(uniqueMark, version);
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setBankCardInfoDto(bankCardInfoDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }

        //同步 approvalListOld 到 approvalListNew 的 主键和创建时间
        sysncApprovalList(approvalListOld, approvalListNew);

//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 从 approvalListOld 同步 主键和创建时间  到 approvalListNew
     * @param approvalListOld
     * @param approvalListNew
     */
    private void sysncApprovalList(List<Approval> approvalListOld, List<Approval> approvalListNew) {
        if (!approvalListOld.isEmpty() && !approvalListNew.isEmpty()) {
            for (Approval approvaln : approvalListNew) {
                for (Approval approvalo : approvalListOld) {
                    if( approvaln.getUniqueMark().equals(approvalo.getUniqueMark()) &&
                            approvaln.getItemKey().equals(approvalo.getItemKey())){
                        //设置主键及创建时间
                        approvaln.setId(approvalo.getId());
                        approvaln.setCreateTime(approvalo.getCreateTime());
                    }
                }
            }
        }
    }

    /**
     * app四要素信息查询
     * @return
     */
    public ResponseEntity<Message> searchAppUserBasicInfo(String uniqueMark){
        String version = versionProperties.getVersion();
        //获取身份证信息
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"未查询到该订单信息"), HttpStatus.OK);
        }
        if(!OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"您的产品非在线助力融，请联系经销商"), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            if(approvalList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
            }
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //获取身份证信息姓名和产品来源
        AppUserBasicInfoDto appUserBasicInfoDto = new AppUserBasicInfoDto(resultDto);
        appUserBasicInfoDto.setFpName(applyInfo.getCreateUser());
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, appUserBasicInfoDto), HttpStatus.OK);
    }

    /**
     * 获取银行列表
     * @return
     */
    public ResponseEntity<Message> getBankList(){
        List<String> result = bankRepository.getBankList();
        if(result == null || result.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "银行列表获取失败"), HttpStatus.OK);
        }
        List<BankListDto> resultList = new ArrayList();
        Object[] objs;
        BankListDto recoveryListDto;
        for (String object : result) {
            recoveryListDto = new BankListDto(object);
            resultList.add(recoveryListDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 获取还款借记卡开户行 ( hpl , 工行，新网 )
     * @param username
     * @return
     */
    public ResponseEntity<Message> getBanks(String username,String originType) throws JSONException {
        if (OriginType.HPL.value().equals(originType) || OriginType.ICBC.value().equals(originType)){
            String result = saleInterface.getBanks("queryRepaymentBankList", username, wechatProperties.getSign(), wechatProperties.getTimestamp());
            return jsonDecoder.singlePropertyDecoder(result, "repaymentBankList");
        }else{
            //根据产品名称获取到产品编号
            String originCode = OriginType.getCodeByValue(originType);
            List<BankNameOrigin> bankNameOriginList = bankNameOriginRepository.findByOriginType(originCode);

            List<Map> mapList = new ArrayList<>();
            if(!bankNameOriginList.isEmpty()){
                for (BankNameOrigin bankNameOrigin : bankNameOriginList) {
                    Map map = new HashMap();
                    map.put("BAHKYH",bankNameOrigin.getName());
                    mapList.add(map);
                }
            }

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,mapList), HttpStatus.OK);
        }
    }


    /**
     * 新增银行卡信息（在线助力融）
     *
     * @param bankCardInfoDto
     * @param uniqueMark  唯一标识
     * @param userName 用户名
     * @param msgCode 短信验证码
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addWeBankInfo(BankCardInfoDto bankCardInfoDto, String msgCode, String uniqueMark, String userName) {
        if(checkApplyByuniqueMark(uniqueMark) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
        }

        if(CommonUtils.isNull(bankCardInfoDto.getBankPhoneNum())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请填写银行卡预留手机号"), HttpStatus.OK);
        }else if(CommonUtils.isNull(bankCardInfoDto.getName())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请填写借记卡户名"), HttpStatus.OK);
        }else if(CommonUtils.isNull(bankCardInfoDto.getAccountNum())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请填写借记卡卡号"), HttpStatus.OK);
        }else if(CommonUtils.isNull(bankCardInfoDto.getBankImg())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请上传银行卡图片"), HttpStatus.OK);
        }else if(CommonUtils.isNull(bankCardInfoDto.getMonthlyIncome())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全月收入水平"), HttpStatus.OK);
        }

        //部分android无法获取appId
        if(CommonUtils.isNull(bankCardInfoDto.getAppId())){
            bankCardInfoDto.setAppId(uniqueMark);
        }
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
        }
        //获取身份证信息
        List<Approval> approvalList = approvalRepository.findIdCardInfo(uniqueMark, version);
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            if(approvalList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
            }
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //获取身份证信息姓名和产品来源
        String name =  resultDto.getIdCardInfoDto().getName();
        String idCardNum = resultDto.getIdCardInfoDto().getIdCardNum();
        String product = OriginType.getCode(applyInfo.getOrigin());
        if(CommonUtils.isNull(name) || CommonUtils.isNull(product) || CommonUtils.isNull(idCardNum)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
        }

        //产品来源在线助力融，还款卡姓名必须和身份证一致
        if(OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin()) || OriginType.WE_BANK.code().equals(applyInfo.getOrigin())){
            if(!name.equals(bankCardInfoDto.getName())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "在线助力融开户行户名与身份证姓名必须一致"), HttpStatus.OK);
            }
            String bankCardNum = bankCardInfoDto.getAccountNum(); //获取银行卡号
            Bank bank = checkCardNum(bankCardNum);
            if(bank == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "银行卡号输入有误，请仔细核验"), HttpStatus.OK);
            } else if(!bank.getName().equals(bankCardInfoDto.getBank())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "您输入的卡号和银行名称不一致，请仔细核验"), HttpStatus.OK);
            }
            if (CommonUtils.isNull(bankCardInfoDto.getIp())){
                String ip = IPAddressUtils.getIpAddress(httpServletRequest);
                if(ip == null){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取ip地址失败"), HttpStatus.OK);
                }
                bankCardInfoDto.setIp(ip);
            }
            //银行卡页面提交微众预审时判断，若一审通过不可在提交 （判断的条件是 姓名 身份证 银行卡)
            WzApplyInfo wzApplyInfo = wzApplyInfoRepository.findTop1ByUniqueMarkOrderByCreateTimeDesc(uniqueMark);
            if(wzApplyInfo != null && ApplyType.FIRST_PASS.code().equals(wzApplyInfo.getStatus()) &&
                    bankCardNum.equals(wzApplyInfo.getBankCardNum())
                    && idCardNum.equals(wzApplyInfo.getIdCard()) && name.equals(wzApplyInfo.getName())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"您的预审批已经通过不可重复提交"), HttpStatus.OK);
            }

            //发送短信让客户确认信息并提交
            ResponseEntity<Message> responseEntity = sysUserService.sendRandomCode(bankCardInfoDto.getBankPhoneNum(), SendMsgType.CONFIRM_MSG.code(), name, uniqueMark);
            if(responseEntity.getBody().getStatus().equals(MessageType.MSG_TYPE_ERROR)){
                return responseEntity;
            }
        }
        List<Approval> approvalListOld = new ArrayList();
        List<Approval> approvalListNew = new ArrayList();
        approvalListOld = approvalRepository.findBankInfo(uniqueMark, version);
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setBankCardInfoDto(bankCardInfoDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "系统异常"), HttpStatus.OK);
        }

        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);

//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> sendWeBankApply(String phoneNum, String uniqueMark, String name) {
        return sysUserService.sendRandomCode(phoneNum, SendMsgType.CONFIRM_MSG.code(), name, uniqueMark);
    }

    /**
     * 新增婚姻状况
     *
     * @param maritalStatusDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addMaritalStatus(MaritalStatusDto maritalStatusDto, String uniqueMark, String userName) {
        if(checkApplyByuniqueMark(uniqueMark) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
        }
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalListOld = new ArrayList();
        List<Approval> approvalListNew = new ArrayList();
        approvalListOld = approvalRepository.findMaritalStatusInfo(uniqueMark, version);
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setMaritalStatusDto(maritalStatusDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }

        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);

//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }




    /**
     * 新增其他信息
     *
     * @param otherInfoDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addOtherInfo(OtherInfoDto otherInfoDto, String uniqueMark, String userName) {
        if(checkApplyByuniqueMark(uniqueMark) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
        }

        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalListOld = new ArrayList();
        List<Approval> approvalListNew = new ArrayList();
        approvalListOld = approvalRepository.findOtherInfo(uniqueMark, version);
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setOtherInfoDto(otherInfoDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }

        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);

//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 新增配偶信息
     *
     * @param mateInfoDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addMateInfo(MateInfoDto mateInfoDto, String uniqueMark, String userName) {
        if(checkApplyByuniqueMark(uniqueMark) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
        }

        //配偶手机号、姓名、身份证号必填check
//        if("".equals(mateInfoDto.getMateMobile()) || mateInfoDto.getMateMobile() == null || "".equals(mateInfoDto.getMateIdty()) || mateInfoDto.getMateIdty() == null ||  "".equals(mateInfoDto.getMateName()) || mateInfoDto.getMateName() == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查配偶信息是否填写完整"), HttpStatus.OK);
//        }
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalListOld = new ArrayList();
        List<Approval> approvalListNew = new ArrayList();
        approvalListOld = approvalRepository.findMateInfo(uniqueMark, version);
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setMateInfoDto(mateInfoDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);

        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 新增客户紧急联系人
     *
     * @param contactInfoDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addContactInfo(ContactInfoDto contactInfoDto, String uniqueMark, String userName) {
        if(checkApplyByuniqueMark(uniqueMark) == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先上传身份证信息！"), HttpStatus.OK);
        }

        if(CommonUtils.isNull(contactInfoDto.getContact1Mobile()) || CommonUtils.isNull(contactInfoDto.getContact1Name())
                || CommonUtils.isNull(contactInfoDto.getContact2Mobile()) || CommonUtils.isNull(contactInfoDto.getContact2Name())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全信息"), HttpStatus.OK);
        }
        String version = versionProperties.getVersion();
        //获取身份证信息
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            if(approvalList.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
            }
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //获取身份证信息姓名和手机号
        String name =  resultDto.getIdCardInfoDto().getName();
        String phoneNum = resultDto.getOtherInfoDto().getPhoneNumber();
        String vicePhoneNumber = resultDto.getOtherInfoDto().getVicePhoneNumber();
        if(CommonUtils.isNull(name)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全身份证信息"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(phoneNum)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先补全联系方式"), HttpStatus.OK);
        }
        if(name.equals(contactInfoDto.getContact1Name()) || name.equals(contactInfoDto.getContact2Name())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"紧急联系人不能是申请人本人"), HttpStatus.OK);
        }
        if(contactInfoDto.getContact1Name().equals(contactInfoDto.getContact2Name())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"紧急联系人不能是同一人"), HttpStatus.OK);
        }
        if(phoneNum.equals(contactInfoDto.getContact1Mobile()) || phoneNum.equals(contactInfoDto.getContact2Mobile())
                || contactInfoDto.getContact1Mobile().equals(vicePhoneNumber) || contactInfoDto.getContact2Mobile().equals(vicePhoneNumber)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"紧急联系人手机号不能是申请人手机号"), HttpStatus.OK);
        }
        if(contactInfoDto.getContact1Mobile().equals(contactInfoDto.getContact2Mobile())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"联系人手机号不可为同一手机号"), HttpStatus.OK);
        }
        List<Approval> approvalListOld = new ArrayList();
        List<Approval> approvalListNew = new ArrayList();
        approvalListOld = approvalRepository.findContactInfo(uniqueMark, version);
        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setContactInfoDto(contactInfoDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);
        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }
    /**
     * 新增预审批附件信息
     *
     * @param ApprovalAttachmentAndIdCardImgDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addApprovalAttachment(ApprovalAttachmentAndIdCardImgDto approvalAttachmentAndIdCardImgDto, String uniqueMark, String userName) {
        String version = versionProperties.getVersion();
        //预审批申请
        ApplyInfoNew applyInfo = checkApplyByuniqueMark(uniqueMark);
        //判断预审批申请是否为空，或产品类型是否为空
        if(applyInfo == null || CommonUtils.isNull(applyInfo.getOrigin())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先补全身份证信息"), HttpStatus.OK);
        }

        ApprovalAttachmentDto approvalAttachmentDto = new ApprovalAttachmentDto();
        BeanUtils.copyProperties(approvalAttachmentAndIdCardImgDto,approvalAttachmentDto);

        IdCardInfoDto idCardInfoDto = new IdCardInfoDto();
        BeanUtils.copyProperties(approvalAttachmentAndIdCardImgDto,idCardInfoDto);



        /**
         * 目前只有 工盟贷3 ，新盟贷4 需要传附件
         */
        //工盟贷
        if(OriginType.ICBC.code().equals(applyInfo.getOrigin())){
            if(
                //按指纹照片
                CommonUtils.isNull(approvalAttachmentDto.getFingerprintImage()) ||
                //手持征信授权书
                CommonUtils.isNull(approvalAttachmentDto.getHandHoldLetterOfCredit()) ||
                //签字照片
                CommonUtils.isNull(approvalAttachmentDto.getSignImage())
            ){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全必传附件信息"), HttpStatus.OK);
            }
        }
        //新盟贷
        if(OriginType.XW_BANK.code().equals(applyInfo.getOrigin())){
            if(
                //用户大头照
                CommonUtils.isNull(approvalAttachmentDto.getUserPhoto())
            ){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全必传附件信息"), HttpStatus.OK);
            }
        }


        List<Approval> approvalListOld ;
        List<Approval> approvalListNew ;

        //如果是退回的情况，需要在附件列表中查询出身份证信息
        if (ApprovalType.BACK.code().equals(applyInfo.getProductStatus())){
            approvalListOld = approvalRepository.findApprovalAttachmentAndIdCardInfoImg(uniqueMark, version);
        }else{
            approvalListOld = approvalRepository.findApprovalAttachment(uniqueMark, version);
        }

        ApplyFromDto totalDto = new ApplyFromDto();
        totalDto.setApprovalAttachmentDto(approvalAttachmentDto);
        totalDto.setIdCardInfoDto(idCardInfoDto);
        try {
            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);

        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询预审批附件
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getApprovalAttachment(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        //产品来源是在身份证页面提交的，必须先完善身份证信息
        if(applyInfo == null || CommonUtils.isNull(applyInfo.getOrigin())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先补全身份证信息"), HttpStatus.OK);
        }
        List<Approval> approvalList;
        //如果是退回的情况，需要在附件列表中查询出身份证信息
        if (ApprovalType.BACK.code().equals(applyInfo.getProductStatus())){
            approvalList = approvalRepository.findApprovalAttachmentAndIdCardInfoImg(uniqueMark, version);
        }else{
            approvalList = approvalRepository.findApprovalAttachment(uniqueMark, version);
        }

        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, new ApprovalAttachmentDto()), HttpStatus.OK);
        }

        //整理数据给前端
        ApprovalAttachmentAndIdCardImgDto approvalAttachmentAndIdCardImgDto = new ApprovalAttachmentAndIdCardImgDto();
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
            ApprovalAttachmentDto attachmentDto = resultDto.getApprovalAttachmentDto();
            IdCardInfoDto idCardInfoDto = resultDto.getIdCardInfoDto();
            approvalAttachmentAndIdCardImgDto.setFrontImg(idCardInfoDto.getFrontImg());
            approvalAttachmentAndIdCardImgDto.setBehindImg(idCardInfoDto.getBehindImg());
            BeanUtils.copyProperties(attachmentDto,approvalAttachmentAndIdCardImgDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, approvalAttachmentAndIdCardImgDto ), HttpStatus.OK);
    }



    /**
     * 查询待提交列表
     *
     * @param userName
     * @param originType 1：app，2：微信，0或者是空：全部
     * @return
     */
    public ResponseEntity<Message> getLocalInfo(String userName, String condition, String originType, Integer size, Integer page) {
        // 版本号
        String version = versionProperties.getVersion();
        //产品类型
        List<String> originTypeList = OriginType.getCodeListBySourceType(originType);

        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> result = approvalRepository.findLocalInfo(userNameList, version, CommonUtils.likePartten(condition),originTypeList,size,(page -1) * size);
        if (result == null || result.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<LocalInfoDto> resultList = new ArrayList();
        LocalInfoDto recoveryListDto;
        Object[] objs;
        for (Object object : result) {
                objs = (Object[]) object;
                recoveryListDto = new LocalInfoDto(objs);
                resultList.add(recoveryListDto);
        }
//        LocalInfoDto recoveryListDto;
//        Object[] objs;
//        if("0".equals(originType) || originType == null){
//            for (Object object : result) {
//                objs = (Object[]) object;
//                recoveryListDto = new LocalInfoDto(objs);
//                resultList.add(recoveryListDto);
//            }
//        } else if("1".equals(originType)){
//            for (Object object : result) {
//                objs = (Object[]) object;
//                //来源为2，在线助力融，也属于APP originType=1
//                if(objs[4] == null || CommonUtils.isNull(objs[4].toString()) ||
//                        OriginType.HPL.code().equals(objs[4].toString()) ||
//                        OriginType.ONLINE_HELP.code().equals(objs[4].toString()) ||
//                        OriginType.ICBC.code().equals(objs[4].toString())){
//                    recoveryListDto = new LocalInfoDto(objs);
//                    resultList.add(recoveryListDto);
//                }
//            }
//        } else if("2".equals(originType)){
//            for (Object object : result) {
//                objs = (Object[]) object;
//                if(objs[4] != null && "1".equals(objs[4].toString())){
//                    recoveryListDto = new LocalInfoDto(objs);
//                    resultList.add(recoveryListDto);
//                }
//            }
//        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 查询返回待修改列表
     *
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getBackInfo(String userName) {
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> dataList = approvalRepository.findBackInfo(userNameList, version);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<BackInfoDto> resultList = new ArrayList();
        BackInfoDto result;
        Object[] objs;
        for (Object object : dataList) {
            objs = (Object[]) object;
            result = new BackInfoDto(objs);
            resultList.add(result);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 获取附件信息退回列表
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getAttachmentBackInfo(String userName, String condition, Integer size, Integer page) {
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }

        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> dataList = approvalRepository.findAttachmentBackInfo(userNameList, version, condition,OriginType.getCodeListByHaveReturn(), size, (page -1) * size);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<BackInfoDto> resultList = new ArrayList();
        BackInfoDto result;
        Object[] objs;
        for (Object object : dataList) {
            objs = (Object[]) object;
            result = new BackInfoDto(objs);
            resultList.add(result);
        }
        PageDto pageDto = new PageDto();
        pageDto.setPage(page);
        pageDto.setSize(size);
        pageDto.setContent(resultList);
        List<Object> resultCount = approvalRepository.findAttachmentBackCount(userNameList, version, condition,OriginType.getCodeListByHaveReturn());
        pageDto.setTotalElements(resultCount.size());
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, pageDto), HttpStatus.OK);
    }

    /**
     * 查询申请中列表
     *
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getSubmitInfo(String userName, Integer size, Integer page) {
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> dataList = approvalRepository.findSubmitInfo(userNameList, version, size, (page -1) * size);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<SubmitInfoDto> resultList = new ArrayList();
        SubmitInfoDto result;
        Object[] objs;
        for (Object object : dataList) {
            objs = (Object[]) object;
            result = new SubmitInfoDto(objs);
            resultList.add(result);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }

    /**
     * 查询审批完成列表
     *
     * @param userName
     * @param searchType:{1:查询审批通过；2:查询拒绝；0:查询全部审批完成}
     * @param condition 检索条件(姓名or申请编号)
     * @param originType 1：app，2：微信，0或者是空：全部
     * @return
     */
    public ResponseEntity<Message> getAchieveInfo(String searchType, String userName, String condition, String originType, Integer size, Integer page) {
        String param = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();

        //产品类型
        List<String> originTypeList = OriginType.getCodeListBySourceType(originType);


        List<Object> dataList = new ArrayList<>();
        if ("1".equals(searchType)) {
            dataList = approvalRepository.findAchieveInfo(userNameList, version,originTypeList ,  ApprovalType.PASS.code(), "", param, size, (page -1) * size);
        } else if ("2".equals(searchType)) {
            dataList = approvalRepository.findAchieveInfo(userNameList, version,originTypeList , "", ApprovalType.REFUSE_NOREASON.code(), param, size, (page -1) * size);
        } else if ("0".equals(searchType)) {
            dataList = approvalRepository.findAchieveInfo(userNameList, version,originTypeList , ApprovalType.PASS.code(),ApprovalType.REFUSE_NOREASON.code(), param, size, (page -1) * size);
        }
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }

        List<AchieveInfoDto> resultList = new ArrayList();
        AchieveInfoDto recoveryListDto;
        Object[] objs;
        for (Object object : dataList) {
            objs = (Object[]) object;
            recoveryListDto = new AchieveInfoDto(objs);
            resultList.add(recoveryListDto);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 根据唯一标识查询其他信息
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getOtherInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(CommonUtils.isNull(resultDto.getOtherInfoDto().getPhoneNumber())){
            resultDto.getOtherInfoDto().setPhoneNumber(resultDto.getBankCardInfoDto().getBankPhoneNum());
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getOtherInfoDto()), HttpStatus.OK);
    }


    /**
     * 根据唯一标识查询婚姻状况
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getMaritalStatus(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findMaritalStatusInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, new MaritalStatusDto()), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getMaritalStatusDto()), HttpStatus.OK);
    }

    /**
     * 查询配偶信息
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getMateInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findMateInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getMateInfoDto()), HttpStatus.OK);
    }


    /**
     * 查询紧急联系人信息
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getContactInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findContactInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getContactInfoDto()), HttpStatus.OK);
    }

    /**
     * 根据唯一标识查询身份证信息
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getIdCardInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findIdCardInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getIdCardInfoDto()), HttpStatus.OK);
    }

    /**
     * 根据唯一标识查询驾驶证信息
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getDriveInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findDriveInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getDriveLicenceInfoDto()), HttpStatus.OK);
    }

    /**
     * 根据唯一标识查询银行卡信息
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getBankInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(CommonUtils.isNull(resultDto.getBankCardInfoDto().getName())){
            resultDto.getBankCardInfoDto().setName(resultDto.getIdCardInfoDto().getName());
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getBankCardInfoDto()), HttpStatus.OK);
    }

    /**
     * 查询预审批附件信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getApprovalImages(String uniqueMark) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new ApprovalImagesDto()), HttpStatus.OK);
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        ApprovalImagesDto approvalImagesDto = new ApprovalImagesDto();
        if(resultDto != null){
            approvalImagesDto = new ApprovalImagesDto(resultDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,approvalImagesDto), HttpStatus.OK);
    }


    /**
     * 预审批附件信息修改
     * @param approvalImagesDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addApprovalImages(ApprovalImagesDto approvalImagesDto, String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Approval> approvalListOld = new ArrayList();
        approvalListOld = approvalRepository.findFullInfo(uniqueMark, version);
        if (approvalListOld.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        ApplyFromDto oldResultDto = new ApplyFromDto();
        try {
            oldResultDto = deserialize(ApplyFromDto.class, approvalListOld);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //更新附件信息
        if(oldResultDto != null){
            oldResultDto.getIdCardInfoDto().setFrontImg(approvalImagesDto.getFrontImg());
            oldResultDto.getIdCardInfoDto().setBehindImg(approvalImagesDto.getBehindImg());
            oldResultDto.getDriveLicenceInfoDto().setLicenceImg(approvalImagesDto.getDriveLicenceImg());
            oldResultDto.getBankCardInfoDto().setBankImg(approvalImagesDto.getBankImg());
        }
        List<Approval> approvalListNew = new ArrayList();
        try {
            approvalListNew = ItemSerialize.serialize(oldResultDto, uniqueMark, version, userName);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }

        //从 approvalListOld 同步 主键和创建时间  到 approvalListNew
        sysncApprovalList(approvalListOld, approvalListNew);

        if (!approvalListNew.isEmpty()) {
            approvalRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询自动预审批各个信息完善状态
     * @param uniqueMark
     * @param
     * @return
     */
    public ResponseEntity<Message> autoApplyState(String uniqueMark) {
        AutoApplyStateDto autoApplyStateDto = new AutoApplyStateDto();
        if(CommonUtils.isNull(uniqueMark)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "数据异常，请联系网站管理人员"), HttpStatus.OK);
        }
        String version = versionProperties.getVersion();
        List<Approval> resultList = approvalRepository.findFullInfo(uniqueMark, version);
        if (resultList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, autoApplyStateDto), HttpStatus.OK);
        }
        //提交校验信息是否填写完整,查询所有未提交项目
        List<String> noSubmits = submitCheck(resultList);

        if(!noSubmits.contains("身份证信息")){
            autoApplyStateDto.setIdCardInfoState(ContractSignStatus.SUBMIT.code());
        }

        if(!noSubmits.contains("驾驶证信息")){
            autoApplyStateDto.setDriveLicenceInfoState(ContractSignStatus.SUBMIT.code());
        }
        if(!noSubmits.contains("银行卡信息")){
            autoApplyStateDto.setBankCardInfoState(ContractSignStatus.SUBMIT.code());
        }
        if(!noSubmits.contains("联系方式")){
            autoApplyStateDto.setOtherInfoState(ContractSignStatus.SUBMIT.code());
        }

        if(!noSubmits.contains("紧急联系人")){
            autoApplyStateDto.setContactInfoState(ContractSignStatus.SUBMIT.code());
        }
        if(!noSubmits.contains("配偶信息")){
            autoApplyStateDto.setMateInfoState(ContractSignStatus.SUBMIT.code());
        }
        if(!noSubmits.contains("婚姻状况")){
            autoApplyStateDto.setMaritalStatusState(ContractSignStatus.SUBMIT.code());
        }
        if(!noSubmits.contains("附件信息")){
            autoApplyStateDto.setApprovalAttachmentState(ContractSignStatus.SUBMIT.code());
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, autoApplyStateDto), HttpStatus.OK);
    }

    /**
     * 自动审批提交  自动预审批提交
     *
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> autoFinancingPreApplySubmit(String uniqueMark, String userName, Double lon, Double lat) {

        //检验 uniqueMark 是否为空
        if(CommonUtils.isNull(uniqueMark)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "uniqueMark 为空"), HttpStatus.OK);
        }
        String version = versionProperties.getVersion();
        //获取到预审批记录
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        //获取到预审批纵向存储表
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        if (approvalList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请补全信息"), HttpStatus.OK);
        }

        //序列化 approvalList 结果到 resultDto
        ApplyFromDto resultDto;
        try {
            resultDto = deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "approvalList 序列化失败"), HttpStatus.OK);
        }

        /**
         *   验证提交信息是否全部提交
         */
        ResponseEntity<Message> returnResult = checkAutoFinancingPreApplySubmit(applyInfo, approvalList, resultDto);
        if (returnResult != null) return returnResult;

        /**
         *    在提交给主系统天启审批的 autoFinancingPreApplyInfoDto 中设值
         */
        AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto = new AutoFinancingPreApplyInfoDto();
        //设置提交参数
        commonAutoFinancingPreApplyInfoDto(uniqueMark, lon, lat, applyInfo, resultDto, autoFinancingPreApplyInfoDto);

        /**
         * 来源为 1 ，微信过来的 微众银行
         * 微信的单子为特殊情况，因为微信过来的单子是已经成功了的，所以微信的单子不用再走预审批了,直接保存微信信息到 applyInfo 中就可以了
         */
        //微信的单子不需要再走预审批
        if(OriginType.WE_BANK.code().equals(applyInfo.getOrigin())){
            //保存信息到applyInfo中
            return wxApplyInfoSubmit(applyInfo);
        }

        /**
         * 来源为 2 ，app自己的  微众银行 - 微盟贷（在线助力融）
         * 在线助力融情况也比较特殊，在线助力融需要获取到 “微众银行” 的审批结果之后才可以提交预审批
         * （这个审批的提交和获取结果在微盟贷的银行卡信息界面）
         * 而且这里跟 hpl 自营产品一样， 只需要走hpl的天启审批就可以了。
         */
        if(OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin())){
            // 传 autoFinancingPreApplyInfoDto 参数是为了需要设置 提交微众唯一标识
            ResponseEntity<Message> wzApplyInfoMessage = onlineHelpApplyInfoSubmit(uniqueMark, autoFinancingPreApplyInfoDto);
            //如果有异常信息，则返回异常信息
            if (wzApplyInfoMessage != null) return wzApplyInfoMessage;
        }


        /**
         * 来源为 3  工商银行 - 工盟贷
         * 工盟贷有自己的信息，及附件
         */
        if(OriginType.ICBC.code().equals(applyInfo.getOrigin())){
            return icbcApplyInfoSubmit(uniqueMark, userName, lon, lat, applyInfo, resultDto, autoFinancingPreApplyInfoDto);
        }

        /**
         * 来源为 4  新网银行 - 新盟贷
         * 新盟贷有自己的信息，及附件
         */
        if(OriginType.XW_BANK.code().equals(applyInfo.getOrigin())){
            return xw_BankApplyInfoSubmit(uniqueMark, userName, lon, lat, applyInfo, resultDto, autoFinancingPreApplyInfoDto);
        }


        /**
         * 其他产品走hpl预审批 （目前只有 hpl 自己产品及 上面的 微盟贷（在线助力融））
         * OriginType = 0,2
         */
        if( OriginType.HPL.code().equals(applyInfo.getOrigin()) ||
            OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin())
        ){
            ResponseEntity<Message> hplResponse = hplApproval(applyInfo, userName, autoFinancingPreApplyInfoDto);
            if(MessageType.MSG_TYPE_SUCCESS.equals(hplResponse.getBody().getStatus())){
                applyInfo.setApprovalLon(lon);
                applyInfo.setApprovalLat(lat);
                applyInfoNewRepository.save(applyInfo);
            }
            return hplResponse;
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "OriginType 状态值不对"), HttpStatus.OK);
    }



    /**
     * 自动预审批提交参数验证
     * @param applyInfo       预审批申请表
     * @param approvalList    预审批纵向存储表
     * @param resultDto       结果值
     * @return
     */
    private ResponseEntity<Message> checkAutoFinancingPreApplySubmit(ApplyInfoNew applyInfo, List<Approval> approvalList, ApplyFromDto resultDto) {

        /**
         * 四个必填项验证：
         *      身份证信息，联系方式，紧急联系人，婚姻状况
         *
         * 这四项在所有产品中都为必填项
         */

        //验证身份证信息是否完整
        String checkString = checkIdCardInfo(resultDto.getIdCardInfoDto());
        if (checkString != null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, checkString), HttpStatus.OK);
        }
        //验证联系方式信息是否完整
        if (CommonUtils.isNull(resultDto.getOtherInfoDto().getPhoneNumber())) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查联系方式是否填写完整"), HttpStatus.OK);
        }
        //验证紧急联系人是否完整填写,及是否正确填写
        ResponseEntity<Message> x = checkContactInfo(resultDto.getIdCardInfoDto(), resultDto.getOtherInfoDto(), resultDto.getContactInfoDto());
        if (x != null) return x;
        //验证婚姻状况是否完整
        String maritalStatus = resultDto.getMaritalStatusDto().getMaritalStatus();
        if(CommonUtils.isNull(maritalStatus)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查婚姻状况是否填写完整"), HttpStatus.OK);
        }


        //验证其他提交信息是否提交完整 （这个地方的验证和上面的 “ 四个必填项 ” 验证需要综合优化一下，这里验证重复了）
        List<String> noSubmits = submitCheck(approvalList);
        String checkResult = "";
        if (!noSubmits.isEmpty()) {
            for (int i = 0; i < noSubmits.size(); i++) {
                //配偶信息非必填项（未婚or离异）
                if("配偶信息".equals(noSubmits.get(i)) && ("未婚".equals(maritalStatus) || "离异".equals(maritalStatus))){
                    continue;
                    //HPL、工行 产品银行卡信息非必填项
                } else if("银行卡信息".equals(noSubmits.get(i)) &&
                        (OriginType.HPL.code().equals(applyInfo.getOrigin()) ||
                                OriginType.ICBC.code().equals(applyInfo.getOrigin()))){
                    continue;
                    //目前只有工行和新网需要附件
                } else if("附件信息".equals(noSubmits.get(i)) &&
                        !OriginType.ICBC.code().equals(applyInfo.getOrigin()) &&
                        !OriginType.XW_BANK.code().equals(applyInfo.getOrigin()) ){
                    continue;
                } else if (i == 0) {
                    checkResult = noSubmits.get(0);
                } else {
                    checkResult = checkResult + "," + noSubmits.get(i);
                }
            }
            if(!"".equals(checkResult)){
                String returnResult = "提交失败！请检查" + checkResult + "是否填写完整";
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, returnResult), HttpStatus.OK);
            }
        }



        /**
         * 其他特殊验证
         */

        //产品来源 微盟贷（在线助力融） 独特验证
        if(OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin())){
            //获取身份证信息中的姓名
            String name =  resultDto.getIdCardInfoDto().getName();
            //获取银行卡姓名
            String bankUserName = resultDto.getBankCardInfoDto().getName();

            //检验开户行户名是否为空
            if(CommonUtils.isNull(bankUserName)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请补全银行卡户名"), HttpStatus.OK);
            }
            //验证开户行户名与身份证姓名是否一致
            if(!name.equals(bankUserName)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "在线助力融开户行户名与身份证姓名必须一致"), HttpStatus.OK);
            }
        }

        return null;
    }

    /**
     * 提交校验 ，这个验证和  checkAutoFinancingPreApplySubmit(后来新加的有验证重复的地方，后期需要改良一下)
     *
     * @param resultList
     * @return
     */
    private List<String> submitCheck(List<Approval> resultList) {
        //数据库有保存
        List<String> submits = new LinkedList<>();
        //数据库有保存无数据
        List<String> noSubmits = new LinkedList<>();
        String key = null;
        String otherInfoDto = "otherInfoDto";
        String phoneNumber = "#otherInfoDto#phoneNumber";
        for (Approval item : resultList) {
            key = item.getItemKey().trim().split("#")[1];
            if (otherInfoDto.equals(key)) {
                if (phoneNumber.equals(item.getItemKey()) && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
                    noSubmits.add(key);
                    continue;
                }

            }else if("approvalAttachmentDto".equals(key)){
                //附件信息：主贷人的三张为必传，配偶附件非必传
                if ("#approvalAttachmentDto#signImage".equals(item.getItemKey()) && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
                    if (!noSubmits.contains(key)) {
                        noSubmits.add(key);
                        continue;
                    }
                }
                if ("#approvalAttachmentDto#handHoldLetterOfCredit".equals(item.getItemKey()) && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
                    if (!noSubmits.contains(key)) {
                        noSubmits.add(key);
                        continue;
                    }
                }
                if ("#approvalAttachmentDto#fingerprintImage".equals(item.getItemKey()) && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
                    if (!noSubmits.contains(key)) {
                        noSubmits.add(key);
                        continue;
                    }
                }

                //用户大头照
                if ("#approvalAttachmentDto#userPhoto".equals(item.getItemKey()) && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
                    if (!noSubmits.contains(key)) {
                        noSubmits.add(key);
                        continue;
                    }
                }
            } else if (ItemDataTypeEnum.StringType.getType() == item.getItemType() && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
                if (!noSubmits.contains(key)) {
                    noSubmits.add(key);
                    continue;
                }
            }
            if (ItemDataTypeEnum.IntegerType.getType() == item.getItemType() && (("").equals(item.getItemInt32Value().toString()) || item.getItemInt32Value() == null)) {
                if (!noSubmits.contains(key)) {
                    noSubmits.add(key);
                    continue;
                }
            }
            if (ItemDataTypeEnum.LongType.getType() == item.getItemType() && (("").equals(item.getItemInt64Value().toString()) || item.getItemInt64Value() == null)) {
                if (!noSubmits.contains(key)) {
                    noSubmits.add(key);
                    continue;
                }
            }
            if (ItemDataTypeEnum.DateType.getType() == item.getItemType() && (("").equals(item.getItemDataTimeValue().toString()) || item.getItemDataTimeValue() == null)) {
                if (!noSubmits.contains(key)) {
                    noSubmits.add(key);
                    continue;
                }
            }
            if (!submits.contains(key)) {
                submits.add(key);
            }
        }
        ApplyFromDto resultDto = new ApplyFromDto();
        Field[] fields = resultDto.getClass().getDeclaredFields();
        List<String> fieldNames = new LinkedList();
        String fieldName;
        for (Field field : fields) {
            fieldName = field.getName();
            fieldNames.add(fieldName);
        }
        for (String field : fieldNames) {
            //数据库未保存
            if (!submits.contains(field) && !noSubmits.contains(field)) {
                noSubmits.add(field);
            }
        }
        //check信息是否填写完整
//        if(noSubmits.size() == 0 || noSubmits.isEmpty()){
//            for (Approval item : resultList) {
//                   key = item.getItemKey().trim().split("#")[1];
//                if (ItemDataTypeEnum.StringType.getType() == item.getItemType() && (("").equals(item.getItemStringValue().trim()) || item.getItemStringValue() == null)) {
//                    if(!noSubmits.contains(key)){
//                        noSubmits.add(key);
//                    }
//                }
//                if(ItemDataTypeEnum.IntegerType.getType() ==item.getItemType() &&(("").equals(item.getItemInt32Value()) || item.getItemInt32Value() == null)){
//                    if(!noSubmits.contains(key)){
//                        noSubmits.add(key);
//                    }
//                }
//                if(ItemDataTypeEnum.LongType.getType() == item.getItemType() &&(("").equals(item.getItemInt64Value()) || item.getItemInt64Value() == null)){
//                    if(!noSubmits.contains(key)){
//                        noSubmits.add(key);
//                    }
//                }
//                if(ItemDataTypeEnum.DateType.getType() == item.getItemType() && (("").equals(item.getItemDataTimeValue()) || item.getItemDataTimeValue() == null)){
//                    if(!noSubmits.contains(key)){
//                        noSubmits.add(key);
//                    }
//                }
//            }
//        }
        List<String> noSubmitInfos = new ArrayList<>();
        for (String noSubmit : noSubmits) {
            if ("idCardInfoDto".equals(noSubmit)) {
                noSubmitInfos.add("身份证信息");
//            } else if ("driveLicenceInfoDto".equals(noSubmit)) {
//                noSubmitInfos.add("驾驶证信息");
            } else if ("bankCardInfoDto".equals(noSubmit)) {
                noSubmitInfos.add("银行卡信息");
            } else if ("otherInfoDto".equals(noSubmit)) {
                noSubmitInfos.add("联系方式");
            } else if("contactInfoDto".equals(noSubmit)){
                noSubmitInfos.add("紧急联系人");
            } else if("mateInfoDto".equals(noSubmit)){
                noSubmitInfos.add("配偶信息");
            } else if("maritalStatusDto".equals(noSubmit)){
                noSubmitInfos.add("婚姻状况");
            } else if("approvalAttachmentDto".equals(noSubmit)){
                noSubmitInfos.add("附件信息");
            }
        }
        return noSubmitInfos;
    }

    /**
     * 修改微信过来的 OriginType = 1  微众银行  的单子的审批状态，由于微信过来的单子是已经通过的，只需要太盟宝这边补全信息。
     * 所以这个地方，只要前面太盟宝验证了所有必填信息已经填写完毕之后就可以通过预审批了
     * @param applyInfo
     * @return
     */
    private ResponseEntity<Message> wxApplyInfoSubmit(ApplyInfoNew applyInfo) {
        //修改数据库中预审批数据微信状态为已补充完毕
        applyInfo.setWxState("1");
        applyInfoNewRepository.save(applyInfo);

        //在applyRecord中保存数据
        ApplyRecord wxapplyRecord = new ApplyRecord();
        wxapplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
        wxapplyRecord.setCreateUser(applyInfo.getCreateUser());
        wxapplyRecord.setOrigin(applyInfo.getOrigin());
        wxapplyRecord.setApplyNum(applyInfo.getApplyNum());
        wxapplyRecord.setStatus("1");
        applyRecordRepository.save(wxapplyRecord);

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询在线助力融的 OriginType = 2  微众银行审批状态， 如果审批通过，则给 autoFinancingPreApplyInfoDto 中设置提交微众的唯一标识
     * @param uniqueMark
     * @param autoFinancingPreApplyInfoDto
     * @return
     */
    private ResponseEntity<Message> onlineHelpApplyInfoSubmit(String uniqueMark, AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto) {
        WzApplyInfo wzApplyInfo = wzApplyInfoRepository.findTop1ByUniqueMarkOrderByCreateTimeDesc(uniqueMark);
        if(wzApplyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先在【银行卡信息】页面提交预审批，确认客户已授权"), HttpStatus.OK);
        }
        if(wzApplyInfo.getStatus().equals(ApplyType.NEW.code())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先在【银行卡信息】页面获取微众预审结果再进行提交"), HttpStatus.OK);
        }
        if(!wzApplyInfo.getStatus().equals(ApplyType.FIRST_PASS.code())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"提交失败，微众" + wzApplyInfo.getReason()), HttpStatus.OK);
        }
        //提交微众唯一标识
        autoFinancingPreApplyInfoDto.setWxId(wzApplyInfo.getWxId());
        return null;
    }


    /**
     * 预审批基本参数。 所有类型的单子。 给提交给主系统天启审批的 autoFinancingPreApplyInfoDto 参数设值
     * @param uniqueMark
     * @param lon
     * @param lat
     * @param applyInfo
     * @param resultDto
     * @param autoFinancingPreApplyInfoDto
     */
    private void commonAutoFinancingPreApplyInfoDto(String uniqueMark, Double lon, Double lat, ApplyInfoNew applyInfo, ApplyFromDto resultDto, AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto) {
        autoFinancingPreApplyInfoDto.setUserid(applyInfo.getCreateUser());
        autoFinancingPreApplyInfoDto.setApplyId(uniqueMark);
        autoFinancingPreApplyInfoDto.setBahyzk(resultDto.getMaritalStatusDto().getMaritalStatus());
        autoFinancingPreApplyInfoDto.setFirstCommitTime(DateUtils.getStrDate(new Date(),new SimpleDateFormat("yyyy-MM-dd")));
        autoFinancingPreApplyInfoDto.setName(resultDto.getIdCardInfoDto().getName());
        autoFinancingPreApplyInfoDto.setIdty(resultDto.getIdCardInfoDto().getIdCardNum());
        autoFinancingPreApplyInfoDto.setMobile(resultDto.getOtherInfoDto().getPhoneNumber());
        autoFinancingPreApplyInfoDto.setLat(lat == null ? "" : lat.toString());
        autoFinancingPreApplyInfoDto.setLon(lon == null ? "" : lon.toString());
        //紧急联系人
        if (resultDto.getContactInfoDto() != null) {
            autoFinancingPreApplyInfoDto.setContact1Name(resultDto.getContactInfoDto().getContact1Name());
            autoFinancingPreApplyInfoDto.setContact1Mobile(resultDto.getContactInfoDto().getContact1Mobile());
            // 联系人关系为空，默认为家人
            String relationShip1 = resultDto.getContactInfoDto().getContact1Relationship();
            autoFinancingPreApplyInfoDto.setContact1Relationship(relationShip1 == null || "".equals(relationShip1) ? "家人" : relationShip1);

            autoFinancingPreApplyInfoDto.setContact2Name(resultDto.getContactInfoDto().getContact2Name());
            autoFinancingPreApplyInfoDto.setContact2Mobile(resultDto.getContactInfoDto().getContact2Mobile());
            // 联系人关系为空，默认为家人
            String relationShip2 = resultDto.getContactInfoDto().getContact2Relationship();
            autoFinancingPreApplyInfoDto.setContact2Relationship(relationShip2 == null || "".equals(relationShip2) ? "家人" : relationShip2);
        }
        //配偶信息
        if (resultDto.getMateInfoDto() != null) {
            autoFinancingPreApplyInfoDto.setMateName(resultDto.getMateInfoDto().getMateName());
            // 配偶手机号
            String mateMobile = resultDto.getMateInfoDto().getMateMobile();
            // 配偶身份证号
            String mateIdty = resultDto.getMateInfoDto().getMateIdty();
            // 如果手机号和身份证号值颠倒，则调换两者数值。
            if (mateMobile != null && mateIdty != null
                    && ((mateMobile.length() == 15 || mateMobile.length() == 18) && mateIdty.length() == 11)) {
                autoFinancingPreApplyInfoDto.setMateMobile(mateIdty);
                autoFinancingPreApplyInfoDto.setMateIdty(mateMobile);
            } else {
                autoFinancingPreApplyInfoDto.setMateMobile(mateMobile);
                autoFinancingPreApplyInfoDto.setMateIdty(mateIdty);
            }
            autoFinancingPreApplyInfoDto.setCompany("");
            autoFinancingPreApplyInfoDto.setAddress("");

        }
    }

    /**
     * 工盟贷提交预审批 工行单子  OriginType = 3   工行审批，hpl审批
     * @param uniqueMark
     * @param userName
     * @param lon
     * @param lat
     * @param applyInfo
     * @param resultDto
     * @param autoFinancingPreApplyInfoDto
     * @return
     */
    private ResponseEntity<Message> icbcApplyInfoSubmit(String uniqueMark, String userName, Double lon, Double lat, ApplyInfoNew applyInfo, ApplyFromDto resultDto, AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto) {
        ResponseEntity<Message> hplResponse;
        ApprovalSubmitDto approvalSubmitDto = new ApprovalSubmitDto();
        approvalSubmitDto.setApplyId(uniqueMark);
        approvalSubmitDto.setUserid(applyInfo.getCreateUser());
        approvalSubmitDto.setProductType("工行");
        approvalSubmitDto.setBahyzk(resultDto.getMaritalStatusDto().getMaritalStatus());
        approvalSubmitDto.setBasqxm(resultDto.getIdCardInfoDto().getName());
        approvalSubmitDto.setBazjhm(resultDto.getIdCardInfoDto().getIdCardNum());
        approvalSubmitDto.setBasjhm(resultDto.getOtherInfoDto().getPhoneNumber());
        if (resultDto.getMateInfoDto() != null) {
            approvalSubmitDto.setMateName(resultDto.getMateInfoDto().getMateName());
            approvalSubmitDto.setMateMobile(resultDto.getMateInfoDto().getMateMobile());
            approvalSubmitDto.setMateIdty(resultDto.getMateInfoDto().getMateIdty());
        }
        ApprovalFileDto zdr_file = new ApprovalFileDto();
        zdr_file.setIdCardFrontImg(resultDto.getIdCardInfoDto().getFrontImg());
        zdr_file.setIdCardBehindImg(resultDto.getIdCardInfoDto().getBehindImg());
        zdr_file.setFingerprintImage(resultDto.getApprovalAttachmentDto().getFingerprintImage());
        zdr_file.setSignImage(resultDto.getApprovalAttachmentDto().getSignImage());
        zdr_file.setHandHoldLetterOfCredit(resultDto.getApprovalAttachmentDto().getHandHoldLetterOfCredit());
        ApprovalFileDto po_file = new ApprovalFileDto();
        po_file.setIdCardFrontImg(resultDto.getApprovalAttachmentDto().getMateFrontImg());
        po_file.setIdCardBehindImg(resultDto.getApprovalAttachmentDto().getMateBehindImg());
        po_file.setFingerprintImage(resultDto.getApprovalAttachmentDto().getMateFingerprintImage());
        po_file.setSignImage(resultDto.getApprovalAttachmentDto().getMateSignImage());
        po_file.setHandHoldLetterOfCredit(resultDto.getApprovalAttachmentDto().getMateHandHoldLetterOfCredit());
        if(CommonUtils.isNull(resultDto.getIdCardInfoDto().getAddress())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请先在【身份证信息】页面完善户籍地址"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(resultDto.getIdCardInfoDto().getIssuingAuthority())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请在最新版本太盟宝【身份证信息】页面完善签发机关"), HttpStatus.OK);
        }
        if(CommonUtils.isNull(resultDto.getIdCardInfoDto().getEffectiveTerm().split("-")[0])){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请在最新版本太盟宝【身份证信息】页面完善身份证有效期"), HttpStatus.OK);
        }
        approvalSubmitDto.setZdr_file(zdr_file);
        approvalSubmitDto.setPo_file(po_file);
        approvalSubmitDto.setLon(lon == null ? "" : lon.toString());
        approvalSubmitDto.setLat(lat == null ? "" : lat.toString());
        approvalSubmitDto.setBahjdz(resultDto.getIdCardInfoDto().getAddress());
        approvalSubmitDto.setBaqfjg(resultDto.getIdCardInfoDto().getIssuingAuthority());
        approvalSubmitDto.setBaksrq(resultDto.getIdCardInfoDto().getEffectiveTerm().split("-")[0]);
        String bajsrq = resultDto.getIdCardInfoDto().getEffectiveTerm().split("-")[1];
        if(CommonUtils.isNull(bajsrq)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"请在最新版本太盟宝【身份证信息】页面完善身份证有效期"), HttpStatus.OK);
        }
        if("长期".equals(bajsrq.trim())){
            bajsrq = "99991230";
        }
        approvalSubmitDto.setBajsrq(bajsrq);
        //首次提交走hpl自动预审批
        if(CommonUtils.isNull(applyInfo.getProductStatus()) || ApprovalType.NEW.code().equals(applyInfo.getProductStatus())){
            hplResponse = hplApproval(applyInfo, userName, autoFinancingPreApplyInfoDto);
            //提交成功后工行产品需要走工行征信
            if(MessageType.MSG_TYPE_SUCCESS.equals(hplResponse.getBody().getStatus())){
                ResponseEntity<Message> icbcResponse = icbcApproval(applyInfo, userName, approvalSubmitDto);
                if(MessageType.MSG_TYPE_SUCCESS.equals(icbcResponse.getBody().getStatus())){
                    applyInfo.setApprovalLon(lon);
                    applyInfo.setApprovalLat(lat);
                    applyInfoNewRepository.save(applyInfo);
                }
                return icbcResponse;
            }
            return hplResponse;
        }
        //退回再提交，不需要走hpl预审批
        ResponseEntity<Message> icbcResponse = icbcApproval(applyInfo, userName, approvalSubmitDto);
        if(MessageType.MSG_TYPE_SUCCESS.equals(icbcResponse.getBody().getStatus())){
            applyInfo.setApprovalLon(lon);
            applyInfo.setApprovalLat(lat);
            applyInfoNewRepository.save(applyInfo);
        }
        return icbcResponse;
    }


    /**
     * 新盟贷提交预审批 新网银行 OriginType = 4   新网审批，hpl审批
     * @param uniqueMark
     * @param userName
     * @param lon
     * @param lat
     * @param applyInfo
     * @param resultDto
     * @param autoFinancingPreApplyInfoDto
     * @return
     */
    private ResponseEntity<Message> xw_BankApplyInfoSubmit(String uniqueMark, String userName, Double lon, Double lat, ApplyInfoNew applyInfo, ApplyFromDto resultDto, AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto) {
        ResponseEntity<Message> hplResponse,xw_BankResponse;
        XW_ApprovalSubmitDto xwApprovalSubmitDto = new XW_ApprovalSubmitDto();
        xwApprovalSubmitDto.setApplyid(uniqueMark);
        xwApprovalSubmitDto.setBasqxm(resultDto.getIdCardInfoDto().getName());
        xwApprovalSubmitDto.setBazjhm(resultDto.getIdCardInfoDto().getIdCardNum());
        xwApprovalSubmitDto.setBasjhm(resultDto.getOtherInfoDto().getPhoneNumber());
        xwApprovalSubmitDto.setBayhkh(resultDto.getBankCardInfoDto().getAccountNum());
        //获取用户真实外网ip
        String ip = IPAddressUtils.getIpAddress(httpServletRequest);
        if(ip == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，获取ip地址失败"), HttpStatus.OK);
        }
        xwApprovalSubmitDto.setXwsqip(ip);
        //附件
        XW_ApprovalFileDto xwApprovalFileDto = new XW_ApprovalFileDto();
        xwApprovalFileDto.setIdCardFront(resultDto.getIdCardInfoDto().getFrontImg());
        xwApprovalFileDto.setIdCardBack(resultDto.getIdCardInfoDto().getBehindImg());
        xwApprovalFileDto.setUserPhoto(resultDto.getApprovalAttachmentDto().getUserPhoto());
        xwApprovalSubmitDto.setFiles(xwApprovalFileDto);

        //首次提交走hpl自动预审批
        if(CommonUtils.isNull(applyInfo.getProductStatus()) || ApprovalType.NEW.code().equals(applyInfo.getProductStatus())){
            hplResponse = hplApproval(applyInfo, userName, autoFinancingPreApplyInfoDto);
            //提交成功后走新网预审批
            if(MessageType.MSG_TYPE_SUCCESS.equals(hplResponse.getBody().getStatus())){
                xw_BankResponse = xw_BankApproval(applyInfo, userName, xwApprovalSubmitDto);
                if(MessageType.MSG_TYPE_SUCCESS.equals(xw_BankResponse.getBody().getStatus())){
                    applyInfo.setApprovalLon(lon);
                    applyInfo.setApprovalLat(lat);
                    applyInfoNewRepository.save(applyInfo);
                }
                return xw_BankResponse;
            }
            return hplResponse;
        }
        //退回再提交，不需要走hpl预审批
        xw_BankResponse = xw_BankApproval(applyInfo, userName, xwApprovalSubmitDto);
        if(MessageType.MSG_TYPE_SUCCESS.equals(xw_BankResponse.getBody().getStatus())){
            applyInfo.setApprovalLon(lon);
            applyInfo.setApprovalLat(lat);
            applyInfoNewRepository.save(applyInfo);
        }
        return xw_BankResponse;
    }


    //HPL预审批  hpl天启
    public ResponseEntity<Message> hplApproval(ApplyInfoNew applyInfo, String userName,  AutoFinancingPreApplyInfoDto autoFinancingPreApplyInfoDto){
        FinancePreApplyResultDto financePreApplyResultDto = new FinancePreApplyResultDto();
        try {
            JSONObject jsonObject = JSONObject.fromObject(autoFinancingPreApplyInfoDto);
            String param = jsonObject.toString();
            logger.info("autoFinancingPreApply={}", param);
            String result = approvalInterface.coreServer("autoFinancingPreApply", param);
            financePreApplyResultDto = objectMapper.readValue(result, FinancePreApplyResultDto.class);
            logger.info("autoFinancingPreApplyResult={}", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }

        // 更新申请状态为申请中
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        ApplyRecord newApplyRecord = new ApplyRecord();
        String status =  approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult());
        if(status == null || "".equals(status)){
            applyInfo.setHplStatus(ApprovalType.SUBMIT.code());
            newApplyRecord.setStatus(ApprovalType.SUBMIT.code());
            newApplyRecord.setReason("申请已提交，等待审核");
        }else {
            applyInfo.setHplStatus(status);
            newApplyRecord.setStatus(status);
            newApplyRecord.setReason(CommonUtils.isNull(financePreApplyResultDto.getApplyResultReason()) ? "申请已提交，等待审核" : financePreApplyResultDto.getApplyResultReason());
        }
        applyInfo.setUpdateUser(userName);
        applyInfo.setApprovalSubmitTime(new Date());
        applyInfo.setUpdateTime(new Date());
        applyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
        applyInfo.setHplReason(financePreApplyResultDto.getApplyResultReason());
        //保存至审批记录表
        newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
        newApplyRecord.setOrigin(OriginType.HPL.code());
        newApplyRecord.setCreateUser(userName);
        applyRecordRepository.save(newApplyRecord);

        //保存预审批申请状态表,逻辑删除之前的数据
        newApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.HPL, userName, "HPL 提交预审批", status);

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    //工行征信  工行预审批
    public ResponseEntity<Message> icbcApproval(ApplyInfoNew applyInfo, String userName, ApprovalSubmitDto approvalSubmitDto){
        Resul res = new Resul();
        try {
            logger.info("icbcApprovalSubmitDto={}", JSONObject.fromObject(approvalSubmitDto).toString(2));
            String result = coreSystemInterface.approvalSubmit("ICBCcreditInformation", approvalSubmitDto);
            res = objectMapper.readValue(result, Resul.class);
            logger.info("ICBCcreditInformation={}", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(res.getResult().getIsSuccess().toString())){
            ApplyRecord newApplyRecord = new ApplyRecord();
            FinancePreApplyResultDto icbcFinancePreApplyResultDto = res.getFinancePreApplyResultDto();
            // 更新申请状态为申请中
            if(icbcFinancePreApplyResultDto == null || CommonUtils.isNull(icbcFinancePreApplyResultDto.getApplyResult())){
                applyInfo.setProductStatus(ApprovalType.SUBMIT.code());
                newApplyRecord.setReason("申请已提交，等待审核");
            }else {
                newApplyRecord.setReason(CommonUtils.isNull(icbcFinancePreApplyResultDto.getApplyResultReason()) ? "申请已提交，等待审核" : icbcFinancePreApplyResultDto.getApplyResultReason());
                newApplyRecord.setApplyNum(icbcFinancePreApplyResultDto.getApplyIdOfMaster());
                applyInfo.setProductStatus(ApprovalType.SUBMIT.code());
                applyInfo.setApplyNum(icbcFinancePreApplyResultDto.getApplyIdOfMaster());
                applyInfo.setProductReason(icbcFinancePreApplyResultDto.getApplyResultReason());
            }
            applyInfo.setUpdateUser(userName);
            applyInfo.setApprovalSubmitTime(new Date());
            applyInfo.setUpdateTime(new Date());
            //保存至审批记录表
            newApplyRecord.setStatus(ApprovalType.SUBMIT.code());
            newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
            newApplyRecord.setOrigin(OriginType.ICBC.code());
            newApplyRecord.setCreateUser(userName);
            applyRecordRepository.save(newApplyRecord);

            //保存预审批申请状态表,逻辑删除之前的数据
            newApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.ICBC, userName, "工盟贷 提交预审批", ApprovalType.SUBMIT.code());

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, res.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    //新网提交预审批 新网银行 新网预审批
    public ResponseEntity<Message> xw_BankApproval(ApplyInfoNew applyInfo, String userName, XW_ApprovalSubmitDto xwApprovalSubmitDto){
        Resul res = new Resul();
        try {
            logger.info("xw_BankApprovalSubmitDto={}", JSONObject.fromObject(xwApprovalSubmitDto).toString(2));
            String result = coreSystemInterface.xw_approvalSubmit("CustomerInformationSubmission", xwApprovalSubmitDto);
            res = objectMapper.readValue(result, Resul.class);
            logger.info("xw_BankcreditInformation={}", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(res.getResult().getIsSuccess().toString())){
            ApplyRecord newApplyRecord = new ApplyRecord();
            FinancePreApplyResultDto xw_FinancePreApplyResultDto = res.getFinancePreApplyResultDto();
            // 更新申请状态为申请中
            if(xw_FinancePreApplyResultDto == null || CommonUtils.isNull(xw_FinancePreApplyResultDto.getApplyResult())){
                applyInfo.setProductStatus(ApprovalType.SUBMIT.code());
                newApplyRecord.setReason("申请已提交，等待审核");
            }else {
                newApplyRecord.setReason(CommonUtils.isNull(xw_FinancePreApplyResultDto.getApplyResultReason()) ? "申请已提交，等待审核" :  xw_FinancePreApplyResultDto.getApplyResultReason());
                newApplyRecord.setApplyNum(xw_FinancePreApplyResultDto.getApplyIdOfMaster());
                applyInfo.setProductStatus(ApprovalType.SUBMIT.code());
                applyInfo.setApplyNum(xw_FinancePreApplyResultDto.getApplyIdOfMaster());
                applyInfo.setProductReason(xw_FinancePreApplyResultDto.getApplyResultReason());
            }
            applyInfo.setUpdateUser(userName);
            applyInfo.setApprovalSubmitTime(new Date());
            applyInfo.setUpdateTime(new Date());
            //保存至审批记录表
            newApplyRecord.setStatus(ApprovalType.SUBMIT.code());
            newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
            newApplyRecord.setOrigin(OriginType.XW_BANK.code());
            newApplyRecord.setCreateUser(userName);
            applyRecordRepository.save(newApplyRecord);


            //新网风控提交状态、大头照提交状态
            newApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.XW_BANK, applyInfo.getCreateUser(), "新盟贷-风控 提交预审批", applyInfo.getProductStatus());
            newApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.XW_BANK_PHOTO_MSG, applyInfo.getCreateUser(), "新盟贷-大头照 提交预审批", applyInfo.getProductStatus());

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"主系统提示：" + res.getResult().getResultMsg()), HttpStatus.OK);
        }
    }



//    现在没有人工提交 -- By ChengQiChuan 2018/10/16 13:14
//    /**
//     * 人工审批提交
//     *
//     * @param uniqueMark
//     * @param userName
//     * @return
//     */
//    @Transactional
//    public ResponseEntity<Message> submit(String uniqueMark, String userName, SubmitProveDto submitProveDto) {
//        if (submitProveDto.getCaseProveUrl().length == 0 || submitProveDto.getCourtVerdictUrl().length == 0) {
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请完善法院证明信息"), HttpStatus.OK);
//        }
//        String version = versionProperties.getVersion();
//        List<AttachmentInfoDto> attachmentInfoDtoList = new ArrayList<>();
//        String[] caseProveUrls = submitProveDto.getCaseProveUrl();
//        String[] courtVerdictUrls = submitProveDto.getCourtVerdictUrl();
//        for (String url : caseProveUrls) {
//            AttachmentInfoDto attachmentInfoDto = new AttachmentInfoDto();
//            attachmentInfoDto.setUrl(url);
//            attachmentInfoDtoList.add(attachmentInfoDto);
//        }
//        for (String url : courtVerdictUrls) {
//            AttachmentInfoDto attachmentInfoDto = new AttachmentInfoDto();
//            attachmentInfoDto.setUrl(url);
//            attachmentInfoDtoList.add(attachmentInfoDto);
//        }
//
//        //保存附件信息
//        List<Approval> approvalListNew = new ArrayList();
//        ApplyFromDto totalDto = new ApplyFromDto();
//        totalDto.setAttachmentInfoDto(attachmentInfoDtoList);
//        List<Approval> approvalListOld = new ArrayList();
//        approvalListOld = approvalRepository.findattachmentInfo(uniqueMark, version);
//        try {
//            approvalListNew = ItemSerialize.serialize(totalDto, uniqueMark, version, userName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "保存附件信息失败"), HttpStatus.OK);
//        }
//        if (!approvalListOld.isEmpty()) {
//            approvalRepository.delete(approvalListOld);
//        }
//        if (!approvalListNew.isEmpty()) {
//            approvalRepository.save(approvalListNew);
//        }
//
//        List<Approval> resultList = approvalRepository.findFullInfo(uniqueMark, version);
//        if (resultList.isEmpty()) {
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到申请信息"), HttpStatus.OK);
//        }
//        ApplyFromDto resultDto = new ApplyFromDto();
//        FinancePreApplyResultDto financePreApplyResultDto;
//        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
//        if (applyInfo == null) {
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败,预审批信息为空"), HttpStatus.OK);
//        }
//        try {
//            resultDto = deserialize(ApplyFromDto.class, resultList);
////
////            ApprovalInterface approvalInterface = mock(ApprovalInterface.class);
////            when(approvalInterface.submit(any(),any(),any(),any(),any(),any(),any(),any(),any())).thenReturn(setSubmitResult(userName,uniqueMark,null,null));
////            String result = "{\"result\":{\"resultMsg\":\"成功\",\"resultCode\":\"\",\"isSuccess\":\"true\"},\"userid\":\"userNameParam\",\"applyId\":\"applyIdParam\",\"applyResult\":\"100\",\"applyResultReason\":\"\"}";
//            if (resultDto.getMateInfoDto() != null) {
//                // 配偶手机号
//                String mateMobile = resultDto.getMateInfoDto().getMateMobile();
//                // 配偶身份证号
//                String mateIdty = resultDto.getMateInfoDto().getMateIdty();
//                // 如果手机号和身份证号值颠倒，则调换两者数值。
//                if (mateMobile != null && mateIdty != null
//                        && ((mateMobile.length() == 15 || mateMobile.length() == 18) && mateIdty.length() == 11)) {
//                    resultDto.getMateInfoDto().setMateMobile(mateIdty);
//                    resultDto.getMateInfoDto().setMateIdty(mateMobile);
//                }
//            }
//            if (resultDto.getContactInfoDto() != null) {
//                // 联系人关系为空，默认为家人
//                ContactInfoDto contactInfoDto = resultDto.getContactInfoDto();
//                String relationShip1 = contactInfoDto.getContact1Relationship();
//                contactInfoDto.setContact1Relationship(relationShip1 == null || "".equals(relationShip1) ? "家人" : relationShip1);
//                String relationShip2 = contactInfoDto.getContact2Relationship();
//                contactInfoDto.setContact2Relationship(relationShip2 == null || "".equals(relationShip2) ? "家人" : relationShip2);
//                resultDto.setContactInfoDto(contactInfoDto);
//            }
//            FinancingPreApplyDto financingPreApplyDto = new FinancingPreApplyDto();
//            financingPreApplyDto.setIdCardInfoDto(resultDto.getIdCardInfoDto());
//            financingPreApplyDto.setBankCardInfoDto(resultDto.getBankCardInfoDto());
//            financingPreApplyDto.setDriveLicenceInfoDto(resultDto.getDriveLicenceInfoDto());
//            financingPreApplyDto.setOtherInfoDto(resultDto.getOtherInfoDto());
//            financingPreApplyDto.setAttachmentInfoDto(resultDto.getAttachmentInfoDto());
//
//            ManualFinancingPreApplyInfoDto manualFinancingPreApplyInfoDto = new ManualFinancingPreApplyInfoDto();
//            manualFinancingPreApplyInfoDto.setUserid(userName);
//            manualFinancingPreApplyInfoDto.setApplyId(uniqueMark);
//            manualFinancingPreApplyInfoDto.setFinancingPreApplyDto(financingPreApplyDto);
//            JSONObject jsonObject = JSONObject.fromObject(manualFinancingPreApplyInfoDto);
//            String param = jsonObject.toString();
//            String result = approvalInterface.coreServer("manualFinancingPreApply", param);
//            financePreApplyResultDto = objectMapper.readValue(result, FinancePreApplyResultDto.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
//        }
////             更新申请状态为申请中
//        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
//        applyInfo.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
//        applyInfo.setUpdateUser(userName);
//        applyInfo.setUpdateTime(new Date());
//        applyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
//        applyInfo.setReason(financePreApplyResultDto.getApplyResultReason());
//        applyInfo.setIsAutoApproval("1");
//        applyInfoNewRepository.save(applyInfo);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//    }



//  修改数据库表结构，修改此数据 -- By ChengQiChuan 2018/10/15 18:24
//    /**
//     * 数据库中查询各状态申请数量
//     * @param userName
//     * @return
//     */
//    public ResponseEntity<Message> getApprovalCount(String userName) {
//        // 版本号
//        String version = versionProperties.getVersion();
//
////        List<ApplyInfo> toSubmitList = new ArrayList<>();
////        List<ApplyInfo> backList = new ArrayList<>();
////        List<ApplyInfo> approvalList = new ArrayList<>();
////        List<ApplyInfo> passList = new ArrayList<>();
//
//        //获取用户所关联的下级用户列表组织架构
//        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
//        if("ERROR".equals(responseEntity.getBody().getStatus())){ return responseEntity; }
//        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
//        // 在数据库中查询申请列表，然后再在下面for循环判断取出每种类型的数量
//        // 这不知道是谁写的，在数据量小的时候还好，数据量特别大的时候，这里的循环时间将会很长，这个地方有时间优化一下
//        //  -- By ChengQiChuan 2018/10/11 16:00
//
////  已优化 -- By ChengQiChuan 2018/10/11 18:12
////        List<ApplyInfo> applyList = applyInfoRepository.findApprovalCount(userNameList, version);
////        for (ApplyInfo applyInfo : applyList) {
////            if (ApprovalType.NEW.code().equals(applyInfo.getStatus()) || (ApprovalType.PASS.code().equals(applyInfo.getStatus()) && "0".equals(applyInfo.getWxState()))) {
////                toSubmitList.add(applyInfo);
////                //工行产品有单独状态
////            } else if (ApprovalType.BACK.code().equals(applyInfo.getStatus()) || (ApprovalType.PASS.code().equals(applyInfo.getStatus()) && OriginType.ICBC.code().equals(applyInfo.getOrigin()) && ApprovalType.BACK.code().equals(applyInfo.getIcbcState()))) {
////                backList.add(applyInfo);
////                //工行产品有单独状态
////            } else if (ApprovalType.SUBMIT.code().equals(applyInfo.getStatus()) || (ApprovalType.PASS.code().equals(applyInfo.getStatus()) && OriginType.ICBC.code().equals(applyInfo.getOrigin()) && ApprovalType.SUBMIT.code().equals(applyInfo.getIcbcState()))) {
////                approvalList.add(applyInfo);
////            } else if (((ApprovalType.PASS.code().equals(applyInfo.getStatus()) && applyInfo.getApplyNum().length() == 8)  || ApprovalType.REFUSE_NOREASON.code().equals(applyInfo.getStatus()))
////                    && !"0".equals(applyInfo.getWxState())) {
////                    passList.add(applyInfo);
////            }
////        }
////        ApprovalCountDto approvalCountDto = new ApprovalCountDto();
////        approvalCountDto.setToSubmitListCount(toSubmitList.size());
////        approvalCountDto.setBackListCount(backList.size());
////        approvalCountDto.setApprovalListCount(approvalList.size());
////        approvalCountDto.setPassListCount(passList.size());
//        // 数据库中查询各状态申请数量 -- By ChengQiChuan 2018/10/11 18:13
//        Object[] approvalCountDtoObj = applyInfoRepository.findApprovalCount(userNameList, version);
//        ApprovalCountDto approvalCountDto = new ApprovalCountDto();
//        try {
//            /**
//             * 将数据转换为实体类
//             */
//            approvalCountDto = com.tm.wechat.utils.sql.EntityUtils.castEntity(approvalCountDtoObj,ApprovalCountDto.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("数据库中查询各状态申请数量 - 数据库结果转实体类异常：" + e);
//        }
//
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, approvalCountDto), HttpStatus.OK);
//    }


    /**
     * 数据库中查询各状态申请数量
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getApprovalCount(String userName) {
        // 版本号
        String version = versionProperties.getVersion();

        //获取用户所关联的下级用户列表组织架构
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){ return responseEntity; }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        Object[] approvalCountDtoObj = applyInfoNewRepository.findApprovalCount(userNameList, version);
        ApprovalCountDto approvalCountDto = new ApprovalCountDto();
        try {
            /**
             * 将数据转换为实体类
             */
            approvalCountDto = com.tm.wechat.utils.sql.EntityUtils.castEntity(approvalCountDtoObj,ApprovalCountDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据库中查询各状态申请数量 - 数据库结果转实体类异常：" + e);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, approvalCountDto), HttpStatus.OK);
    }

    /**
     * 接收主系统推送过来的审批结果 (天启)
     *
     * @param financePreApplyResultDto
     * @return
     */
    public ResponseEntity<Message> receiveFinancePreApplyResult(FinancePreApplyResultDto financePreApplyResultDto) {
        logger.info("financePreApplyResultDto={}", financePreApplyResultDto);
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.getApplyId(), version);
        if(applyInfo==null){
            applyInfo = applyInfoNewRepository.findByApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
        }
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if (applyInfo != null && financePreApplyResultDto.getApplyResult() != null && !"".equals(financePreApplyResultDto.getApplyResult().trim())) {
            applyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            applyInfo.setHplReason(financePreApplyResultDto.getApplyResultReason());
            applyInfo.setHplStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
            applyInfo.setUpdateUser(financePreApplyResultDto.getUserid());
            applyInfo.setUpdateTime(new Date());
            applyInfoNewRepository.save(applyInfo);

            //保存至审批记录表
            ApplyRecord newApplyRecord = new ApplyRecord();
            newApplyRecord.setReason(financePreApplyResultDto.getApplyResultReason());
            newApplyRecord.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.getApplyResult()));
            newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
            newApplyRecord.setOrigin(OriginType.HPL.code());
            newApplyRecord.setCreateUser(financePreApplyResultDto.getUserid());
            newApplyRecord.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            applyRecordRepository.save(newApplyRecord);

            //更新预审批状态表
            updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.HPL, applyInfo.getCreateUser(), applyInfo.getHplReason(), applyInfo.getHplStatus());

            //预审批通过调用订单绑定接口，自己绑定给自己
            if("000".equals(financePreApplyResultDto.getApplyResult())){
                ApplyFromDto resultDto = new ApplyFromDto();
                List<Approval> approvalList = approvalRepository.findApprovalInfo(financePreApplyResultDto.getApplyIdOfMaster(), version);
                if (approvalList != null && !approvalList.isEmpty()) {
                    try {
                        resultDto = deserialize(ApplyFromDto.class, approvalList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String name = resultDto.getIdCardInfoDto().getName();
                ResponseEntity<Message> responseEntity = orderService.orderBind(
                        financePreApplyResultDto.getUserid(), financePreApplyResultDto.getUserid(),
                        financePreApplyResultDto.getApplyIdOfMaster(),name);
                return responseEntity;
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        //推送失败，主动查询预审批、完善申请状态
        approvalService.queryFinancePreApplyResult();
        approvalService.queryApplyStates();
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "审批结果推送失败"), HttpStatus.OK);
    }


    /**
     * 接收主系统推送过来的审批结果（工行）
     *
     * @param financePreApplyResultDto
     * @return
     */
    public ResponseEntity<Message> receiveICBCFinancePreApplyResult(FinancePreApplyResultDto financePreApplyResultDto) {
        logger.info("icbcFinancePreApplyResultDto={}", financePreApplyResultDto);
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.getApplyId(), version);
        if(applyInfo==null){
            applyInfo = applyInfoNewRepository.findByApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
        }
        if (applyInfo != null && financePreApplyResultDto.getApplyResult() != null && !"".equals(financePreApplyResultDto.getApplyResult().trim())) {
            applyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            applyInfo.setProductReason(financePreApplyResultDto.getApplyResultReason());
            applyInfo.setProductStatus(financePreApplyResultDto.getApplyResult());
            applyInfo.setUpdateUser(financePreApplyResultDto.getUserid());
            applyInfo.setUpdateTime(new Date());
            applyInfoNewRepository.save(applyInfo);
            //保存至审批记录表
            ApplyRecord newApplyRecord = new ApplyRecord();
            newApplyRecord.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
            newApplyRecord.setReason(financePreApplyResultDto.getApplyResultReason());
            newApplyRecord.setStatus(financePreApplyResultDto.getApplyResult());
            newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
            newApplyRecord.setOrigin(OriginType.ICBC.code());
            newApplyRecord.setCreateUser(financePreApplyResultDto.getUserid());
            applyRecordRepository.save(newApplyRecord);

            //更新预审批状态表
            updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.ICBC, applyInfo.getCreateUser(), applyInfo.getProductReason(), applyInfo.getProductStatus());

            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        //推送失败，主动查询预审批、完善申请状态
        approvalService.queryICBCFinancePreApplyResult();
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "工行审批结果推送失败"), HttpStatus.OK);
    }



    /**
     * 接收主系统推送过来的审批结果（新网）
     *
     * @param financePreApplyResultDto
     * @return
     */
    public ResponseEntity<Message> receiveXWBankFinancePreApplyResult(XW_FinancePreApplyResultDto financePreApplyResultDto) {
        logger.info("xwBankFinancePreApplyResultDto={}", financePreApplyResultDto);
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.getApplyId(), version);
        //这里不知道为为什么要判断一下，难道主系统有时候传过来的额 applyId 有问题？
        if(applyInfo == null){
            applyInfo = applyInfoNewRepository.findByApplyNum(financePreApplyResultDto.getBasqbh());
        }
        if (applyInfo != null && financePreApplyResultDto.getStatus() != null && !"".equals(financePreApplyResultDto.getStatus().trim())) {
            applyInfo.setApplyNum(financePreApplyResultDto.getBasqbh());
            applyInfo.setProductReason(XW_FinancePreApplyResultDto.getProductReason(financePreApplyResultDto));
            applyInfo.setProductStatus(XW_FinancePreApplyResultDto.changeCode(financePreApplyResultDto));
            applyInfo.setUpdateUser(financePreApplyResultDto.getUserid());
            applyInfo.setUpdateTime(new Date());
            applyInfoNewRepository.save(applyInfo);
            //如果返回的结果不为申请中，保存至审批记录表
            if(!ApprovalType.SUBMIT.code().equals(XW_FinancePreApplyResultDto.changeCode(financePreApplyResultDto))){
                ApplyRecord newApplyRecord = new ApplyRecord();
                newApplyRecord.setApplyNum(financePreApplyResultDto.getBasqbh());
                newApplyRecord.setReason(XW_FinancePreApplyResultDto.getProductReason(financePreApplyResultDto));
                newApplyRecord.setStatus(XW_FinancePreApplyResultDto.changeCode(financePreApplyResultDto));
                newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
                newApplyRecord.setOrigin(OriginType.XW_BANK.code());
                newApplyRecord.setCreateUser(financePreApplyResultDto.getUserid());
                applyRecordRepository.save(newApplyRecord);
            }

            //更新预审批状态表  新网风控
            updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.XW_BANK, applyInfo.getCreateUser(), financePreApplyResultDto.getReason(), financePreApplyResultDto.getStatus());
            //更改审批状态表  新网大头照
            updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.XW_BANK_PHOTO_MSG, applyInfo.getCreateUser(), financePreApplyResultDto.getPhotoMsg(), financePreApplyResultDto.getUserPhotores());


            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        //推送失败，主动查询预审批、完善申请状态
        approvalService.queryXWBankFinancePreApplyResult();
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "新网审批结果推送失败"), HttpStatus.OK);
    }


    /**
     * 查询自动预审批审批结果 (hpl 天启)
     */
    public ResponseEntity<Message> queryFinancePreApplyResult() {
        String version = versionProperties.getVersion();
        //查询预审批中状态为待审核的订单
        List<ApplyInfoNew> toSubmitList = applyInfoNewRepository.findByVersionAndHplStatus(version, ApprovalType.SUBMIT.code());
        List<String> applyIdList = new ArrayList<>();
        //查询待审批订单uuid
        for (ApplyInfoNew applyInfo : toSubmitList) {
            applyIdList.add(applyInfo.getApprovalUuid());
        }
        List<Map> financePreApplyResultDtos = new ArrayList<>();//主系统返回状态不在申请中的预审批
        FinancePreApplyResultListDto financePreApplyResultListDto = null;//主系统返回结果
        try {
            QueryFinancePreApplyResultDto queryFinancePreApplyResultDto = new QueryFinancePreApplyResultDto();
            queryFinancePreApplyResultDto.setParam(applyIdList);
            JSONObject jsonObject = JSONObject.fromObject(queryFinancePreApplyResultDto);
            String param = jsonObject.toString();
            String result = approvalInterface.coreServer("qureyFinancePreApplyResult", param);
            if (result == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到主系统结果"), HttpStatus.OK);
            }
            financePreApplyResultListDto = objectMapper.readValue(result, FinancePreApplyResultListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
        //取得返回结果
        List<Map> financePreApplyResultList = (List<Map>) financePreApplyResultListDto.getLr();
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if (financePreApplyResultList == null || financePreApplyResultList.size() == 0) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        //取得所有状态不在审批中的订单
        for (Map financePreApplyResult : financePreApplyResultList) {
            if (financePreApplyResult != null && financePreApplyResult.get("applyResult") != null && !"".equals(financePreApplyResult.get("applyResult").toString().trim()) && !ApprovalType.SUBMIT.code().equals(approvalTypeConvert.typeConvert(financePreApplyResult.get("applyResult").toString()))) {
                financePreApplyResultDtos.add(financePreApplyResult);
            }
        }
        ApplyInfoNew applyInfo;
        List<ApplyInfoNew> applyInfoList = new ArrayList<>();
        //更新状态
        for (Map financePreApplyResultDto : financePreApplyResultDtos) {
            applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.get("applyId").toString(), version);
            if (applyInfo != null) {
                applyInfo.setApplyNum(financePreApplyResultDto.get("applyIdOfMaster").toString());
                applyInfo.setHplReason(financePreApplyResultDto.get("applyResultReason").toString());
                applyInfo.setUpdateUser(financePreApplyResultDto.get("userid").toString());
                applyInfo.setHplStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult").toString()));
                applyInfo.setUpdateTime(new Date());
                applyInfoNewRepository.save(applyInfo);
                //保存至审批记录表
                ApplyRecord newApplyRecord = new ApplyRecord();
                newApplyRecord.setReason(financePreApplyResultDto.get("applyResultReason").toString());
                newApplyRecord.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult").toString()));
                newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
                newApplyRecord.setOrigin(OriginType.HPL.code());
                newApplyRecord.setCreateUser(financePreApplyResultDto.get("userid").toString());
                newApplyRecord.setApplyNum(financePreApplyResultDto.get("applyIdOfMaster").toString());
                applyRecordRepository.save(newApplyRecord);

                //更新预审批状态表
                updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.HPL, applyInfo.getCreateUser(), applyInfo.getHplReason(), applyInfo.getHplStatus());


//                applyInfoList.add(applyInfo);
                //预审批通过调用订单绑定接口，自己绑定给自己
                if("000".equals(financePreApplyResultDto.get("applyResult").toString())){
                    ApplyFromDto resultDto = new ApplyFromDto();
                    List<Approval> approvalList = approvalRepository.findApprovalInfo(financePreApplyResultDto.get("applyIdOfMaster").toString(), version);
                    if (approvalList != null && !approvalList.isEmpty()) {
                        try {
                            resultDto = deserialize(ApplyFromDto.class, approvalList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 之前这个地方总报错，这里解决一下 -- By ChengQiChuan 2018/10/11 12:59
                    if(resultDto != null && resultDto.getIdCardInfoDto() != null && resultDto.getIdCardInfoDto().getName()!=null){
                        String name = resultDto.getIdCardInfoDto().getName();
                        ResponseEntity<Message> responseEntity = orderService.orderBind(
                                financePreApplyResultDto.get("userid").toString(), financePreApplyResultDto.get("userid").toString(),
                                financePreApplyResultDto.get("applyIdOfMaster").toString(),name);
                        return responseEntity;
                    }
                }
            }
        }
//        applyInfoRepository.save(applyInfoList);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }



    /**
     * 查询工行预审批审批结果（工行）
     */
    public ResponseEntity<Message> queryICBCFinancePreApplyResult() {
        String version = versionProperties.getVersion();
        //查询工行预审批中状态为审核中的订单
        List<ApplyInfoNew> toSubmitList = applyInfoNewRepository.findByVersionAndOriginAndProductStatus(version, OriginType.ICBC.code(),ApprovalType.SUBMIT.code());
        List<String> applyIdList = new ArrayList<>();
        //查询待审批订单uuid
        for (ApplyInfoNew applyInfo : toSubmitList) {
            applyIdList.add(applyInfo.getApprovalUuid());
        }
        QueryICBCFinancePreApplyResultDto queryFinancePreApplyResultDto = new QueryICBCFinancePreApplyResultDto();
        queryFinancePreApplyResultDto.setQueryList(applyIdList);

        List<FinancePreApplyResultDto> financePreApplyResultDtos = new ArrayList<>();//主系统返回状态不在申请中的预审批
        FinancePreApplyResultListDto financePreApplyResultListDto = null;//主系统返回结果
        try {
            String result = coreSystemInterface.approvalStateQuery("BatchQqueryICBCcreditResults", queryFinancePreApplyResultDto);
            if (result == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到主系统结果"), HttpStatus.OK);
            }
            financePreApplyResultListDto = objectMapper.readValue(result, FinancePreApplyResultListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
        //取得返回结果
        List<FinancePreApplyResultDto> financePreApplyResultList =  financePreApplyResultListDto.getResultList();
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if (financePreApplyResultList == null || financePreApplyResultList.size() == 0) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        //取得所有状态不在审批中的订单
        for (FinancePreApplyResultDto financePreApplyResult : financePreApplyResultList) {
            if (financePreApplyResult != null && financePreApplyResult.getApplyResult() != null && !"".equals(financePreApplyResult.getApplyResult()) && !ApprovalType.SUBMIT.code().equals(financePreApplyResult.getApplyResult())) {
                financePreApplyResultDtos.add(financePreApplyResult);
            }
        }
        ApplyInfoNew applyInfo;
        List<ApplyInfoNew> applyInfoList = new ArrayList<>();
        //更新状态
        for (FinancePreApplyResultDto financePreApplyResultDto : financePreApplyResultDtos) {
            applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.getApplyId(), version);
            if (applyInfo != null) {
                applyInfo.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
                applyInfo.setProductReason(financePreApplyResultDto.getApplyResultReason());
                applyInfo.setUpdateUser(financePreApplyResultDto.getUserid());
                applyInfo.setProductStatus(financePreApplyResultDto.getApplyResult());
                applyInfo.setUpdateTime(new Date());
                applyInfoNewRepository.save(applyInfo);
                //保存至审批记录表
                ApplyRecord newApplyRecord = new ApplyRecord();
                newApplyRecord.setReason(financePreApplyResultDto.getApplyResultReason());
                newApplyRecord.setStatus(financePreApplyResultDto.getApplyResult());
                newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
                newApplyRecord.setOrigin(OriginType.ICBC.code());
                newApplyRecord.setCreateUser(financePreApplyResultDto.getUserid());
                newApplyRecord.setApplyNum(financePreApplyResultDto.getApplyIdOfMaster());
                applyRecordRepository.save(newApplyRecord);

                //更新预审批状态表
                updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.ICBC, applyInfo.getCreateUser(), applyInfo.getProductReason(), applyInfo.getProductStatus());
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询在线助力融拒绝审批结果，成功的单子由主系统推送
     */
    public ResponseEntity<Message> queryWxApplyResult() {
        String version = versionProperties.getVersion();
        //查询在线助力融预审批中状态为待审核的订单
        String origin = OriginType.ONLINE_HELP.code();
        List<ApplyInfoNew> toSubmitList = applyInfoNewRepository.findByVersionAndOriginAndHplStatus(version, ApprovalType.SUBMIT.code(), origin);
        List<String> applyIdList = new ArrayList<>();
        for (ApplyInfoNew applyInfo : toSubmitList) {
            applyIdList.add(applyInfo.getApprovalUuid());
        }
        List<Map> financePreApplyResultDtos = new ArrayList<>();
        ResponseEntity<Message> responseEntity = null;
        try {
             responseEntity = leaseWeChatInterface.getApplyInfoList(applyIdList);
            if (responseEntity == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到微信结果"), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询微信系统异常"), HttpStatus.OK);
        }
        //取得返回结果
        List<Map> financePreApplyResultList = (List<Map>) responseEntity.getBody().getData();
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if (financePreApplyResultList == null || applyIdList.size() == 0) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        //取得所有状态不在审批中的订单
        for (Map financePreApplyResult : financePreApplyResultList) {
            if (financePreApplyResult != null && financePreApplyResult.get("applyResult") != null && !"".equals(financePreApplyResult.get("applyResult").toString().trim()) && !ApprovalType.SUBMIT.code().equals(approvalTypeConvert.typeConvert(financePreApplyResult.get("applyResult").toString()))) {
                financePreApplyResultDtos.add(financePreApplyResult);
            }
        }
        ApplyInfoNew applyInfo;
        List<ApplyInfoNew> applyInfoList = new ArrayList<>();
        List<ApplyRecord> applyRecordList = new ArrayList<>();
        //更新状态
        for (Map financePreApplyResultDto : financePreApplyResultDtos) {
            applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.get("applyId").toString(), version);
            if (applyInfo != null) {
                applyInfo.setApplyNum(financePreApplyResultDto.get("applyIdOfMaster") == null ? "" : financePreApplyResultDto.get("applyIdOfMaster").toString());
                applyInfo.setProductReason(financePreApplyResultDto.get("applyResultReason") == null ? "" : financePreApplyResultDto.get("applyResultReason").toString());
                applyInfo.setUpdateUser(financePreApplyResultDto.get("userid") == null ? "" : financePreApplyResultDto.get("userid").toString());
                applyInfo.setHplStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult") == null ? "" : financePreApplyResultDto.get("applyResult").toString()));
                applyInfo.setUpdateTime(new Date());
                applyInfoList.add(applyInfo);

                ApplyRecord applyRecord = new ApplyRecord();
                applyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
                applyRecord.setApplyNum(financePreApplyResultDto.get("applyIdOfMaster") == null ? "" : financePreApplyResultDto.get("applyIdOfMaster").toString());
                applyRecord.setOrigin(applyInfo.getOrigin());
                applyRecord.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult") == null ? "" : financePreApplyResultDto.get("applyResult").toString()));
                applyRecord.setCreateUser(financePreApplyResultDto.get("userid") == null ? "" : financePreApplyResultDto.get("userid").toString());
                applyRecord.setReason(financePreApplyResultDto.get("applyResultReason") == null ? "" : financePreApplyResultDto.get("applyResultReason").toString());
                applyRecordList.add(applyRecord);

                //更新预审批状态表
                updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.WZ_BANK, applyInfo.getCreateUser(), applyInfo.getProductReason(), applyInfo.getHplStatus());
            }
        }
        applyInfoNewRepository.save(applyInfoList);
        applyRecordRepository.save(applyRecordList);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, financePreApplyResultDtos), HttpStatus.OK);
    }


    /**
     * 查询新网预审批审批结果（新网）
     */
    public ResponseEntity<Message> queryXWBankFinancePreApplyResult() {
        String version = versionProperties.getVersion();
        //查询新网预审批中状态为审核中的订单
        List<ApplyInfoNew> toSubmitList = applyInfoNewRepository.findByVersionAndOriginAndProductStatus(version, OriginType.XW_BANK.code(),ApprovalType.SUBMIT.code());
        List<String> applyIdList = new ArrayList<>();
        //查询待审批订单uuid
        for (ApplyInfoNew applyInfo : toSubmitList) {
            applyIdList.add(applyInfo.getApprovalUuid());
        }
        XW_QueryFinancePreApplyResultDto queryFinancePreApplyResultDto = new XW_QueryFinancePreApplyResultDto();
        queryFinancePreApplyResultDto.setQueryList(applyIdList);

        List<XW_FinancePreApplyResultDto> financePreApplyResultDtos = new ArrayList<>();//主系统返回状态不在申请中的预审批
        XW_FinancePreApplyResultListDto financePreApplyResultListDto = null;//主系统返回结果
        try {
            String result = coreSystemInterface.xw_approvalStateQuery("AuditResultQuery", queryFinancePreApplyResultDto);
            if (result == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到主系统结果"), HttpStatus.OK);
            }
            financePreApplyResultListDto = objectMapper.readValue(result, XW_FinancePreApplyResultListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
        //取得返回结果
        List<XW_FinancePreApplyResultDto> financePreApplyResultList =  financePreApplyResultListDto.getResultList();
        if (financePreApplyResultList == null || financePreApplyResultList.size() == 0) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        //取得所有状态不在审批中的订单
        for (XW_FinancePreApplyResultDto financePreApplyResult : financePreApplyResultList) {
            if (financePreApplyResult != null && financePreApplyResult.getStatus() != null && !"".equals(financePreApplyResult.getStatus()) && !ApprovalType.SUBMIT.code().equals(financePreApplyResult.getStatus())) {
                financePreApplyResultDtos.add(financePreApplyResult);
            }
        }
        ApplyInfoNew applyInfo;
        List<ApplyInfoNew> applyInfoList = new ArrayList<>();
        //更新状态
        for (XW_FinancePreApplyResultDto financePreApplyResultDto : financePreApplyResultDtos) {
            applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(financePreApplyResultDto.getApplyId(), version);
            if (applyInfo != null) {
                applyInfo.setApplyNum(financePreApplyResultDto.getBasqbh());
                applyInfo.setProductReason(XW_FinancePreApplyResultDto.getProductReason(financePreApplyResultDto));
                applyInfo.setProductStatus(XW_FinancePreApplyResultDto.changeCode(financePreApplyResultDto));
                applyInfo.setUpdateUser(financePreApplyResultDto.getUserid());
                applyInfo.setUpdateTime(new Date());
                applyInfoNewRepository.save(applyInfo);
                //如果返回的结果不为申请中，保存至审批记录表
                if(!ApprovalType.SUBMIT.code().equals(XW_FinancePreApplyResultDto.changeCode(financePreApplyResultDto))){
                    ApplyRecord newApplyRecord = new ApplyRecord();
                    newApplyRecord.setReason(XW_FinancePreApplyResultDto.getProductReason(financePreApplyResultDto));
                    newApplyRecord.setStatus(XW_FinancePreApplyResultDto.changeCode(financePreApplyResultDto));
                    newApplyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
                    newApplyRecord.setOrigin(OriginType.XW_BANK.code());
                    newApplyRecord.setCreateUser(financePreApplyResultDto.getUserid());
                    newApplyRecord.setApplyNum(financePreApplyResultDto.getBasqbh());
                    applyRecordRepository.save(newApplyRecord);
                }

                //更新预审批状态表  新网风控
                updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.XW_BANK, applyInfo.getCreateUser(), financePreApplyResultDto.getReason(), financePreApplyResultDto.getStatus());
                //更新预审批状态表  新网大头照
                updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.XW_BANK, applyInfo.getCreateUser(), financePreApplyResultDto.getPhotoMsg(), financePreApplyResultDto.getUserPhotores());


            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询在线申请审批结果
     */
    public ResponseEntity<Message> queryApplyStates() {
        String version = versionProperties.getVersion();
        //查询预审批中状态为待审核的订单
        List<ApplyInfoNew> toSubmitList = applyInfoNewRepository.findByVersionAndHplStatus(version, ApprovalType.WAIT_APPROVAL.code());
        List<String> applyIdList = new ArrayList<>();
        //查询待审批订单主系统审批编号
        for (ApplyInfoNew applyInfo : toSubmitList) {
            applyIdList.add(applyInfo.getApplyNum());
        }
        List<Map> financePreApplyResultDtos = new ArrayList<>();//主系统返回状态不在申请中的预审批
        CoreResult financePreApplyResultListDto = null;//主系统返回结果
        try {
//            QueryApplyResultDto queryApplyResultDto = new QueryApplyResultDto();
            ApplyIdOfMasterDto applyIdOfMasterDto;
            List<ApplyIdOfMasterDto> applyIdOfMasterList = new ArrayList<>();
            for(String applyId : applyIdList){
                applyIdOfMasterDto = new ApplyIdOfMasterDto();
                applyIdOfMasterDto.setApplyIdOfMaster(applyId);
                applyIdOfMasterList.add(applyIdOfMasterDto);
            }
//            queryApplyResultDto.setApplyIdOfMasterList(applyIdOfMasterList);
//            JSONObject jsonObject = JSONObject.fromObject(queryApplyResultDto);
//            String param = jsonObject.get("applyIdOfMasterList").toString();
            String param = gson.toJson(applyIdOfMasterList);
            String result = approvalInterface.queryApplyStates("qureyApplyStates",param);
            if (result == null) {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到主系统结果"), HttpStatus.OK);
            }
            financePreApplyResultListDto = objectMapper.readValue(result, CoreResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统异常"), HttpStatus.OK);
        }
        if(!"true".equals(financePreApplyResultListDto.getResult().getIsSuccess())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询状态失败"), HttpStatus.OK);
        }
        //取得返回结果
        List<Map> financePreApplyResultList = (List<Map>) financePreApplyResultListDto.getApplyResults();
        ApprovalTypeConvert approvalTypeConvert = new ApprovalTypeConvert();
        if (financePreApplyResultList == null || applyIdList.size() == 0) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
        }
        //取得所有状态不在审批中的订单
        for (Map financePreApplyResult : financePreApplyResultList) {
            if (financePreApplyResult != null && financePreApplyResult.get("applyResult") != null && !"".equals(financePreApplyResult.get("applyResult").toString().trim()) && !ApprovalType.WAIT_APPROVAL.code().equals(approvalTypeConvert.typeConvert(financePreApplyResult.get("applyResult").toString()))) {
                financePreApplyResultDtos.add(financePreApplyResult);
            }
        }
        ApplyInfoNew applyInfo;
        List<ApplyInfoNew> applyInfoList = new ArrayList<>();
        List<ApplyRecord> applyRecordList = new ArrayList<>();
        //更新状态
        for (Map financePreApplyResultDto : financePreApplyResultDtos) {
            applyInfo = applyInfoNewRepository.findByApplyNum(financePreApplyResultDto.get("applyIdOfMaster").toString());
            if (applyInfo != null) {
                applyInfo.setHplReason(financePreApplyResultDto.get("applyResultReason").toString());
                applyInfo.setHplStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult").toString()));
                applyInfo.setUpdateTime(new Date());
                applyInfoList.add(applyInfo);

                ApplyRecord applyRecord = new ApplyRecord();
                applyRecord.setApprovalUuid(applyInfo.getApprovalUuid());
                applyRecord.setApplyNum(applyInfo.getApplyNum());
                applyRecord.setOrigin(applyInfo.getOrigin());
                applyRecord.setStatus(approvalTypeConvert.typeConvert(financePreApplyResultDto.get("applyResult").toString()));
                applyRecord.setCreateUser(financePreApplyResultDto.get("userid") == null ? "" : financePreApplyResultDto.get("userid").toString());
                applyRecord.setReason(financePreApplyResultDto.get("applyResultReason").toString());
                applyRecordList.add(applyRecord);

                //更新预审批状态记录
                updateApplyInfoStatus(applyInfo.getApprovalUuid(), ApplyInfoStatusType.HPL, applyInfo.getCreateUser(), applyInfo.getHplReason(), applyInfo.getHplStatus());


            }
        }
        applyInfoNewRepository.save(applyInfoList);
        applyRecordRepository.save(applyRecordList);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 增加预审批状态表数据
     * @param uniqueMark            预审批唯一编号
     * @param applyInfoStatusType   ApplyInfoStatusType类型
     * @param name                  名字
     * @param reason                备注
     * @param status                状态
     */
    private void newApplyInfoStatus(String uniqueMark, ApplyInfoStatusType applyInfoStatusType, String name, String reason, String status) {
        //查询到已经存在的记录，进行逻辑删除
        List<ApplyInfoStatus> applyInfoStatusList = applyInfoStatusRepository.findByUniqueMarkAndItemKeyAndIsDelete(uniqueMark, applyInfoStatusType.getValue(),"0");
        for (ApplyInfoStatus applyInfoStatus : applyInfoStatusList) {
                applyInfoStatus.setIsDelete("1");
        }
        applyInfoStatusRepository.save(applyInfoStatusList);

        ApplyInfoStatus applyInfoStatus = new ApplyInfoStatus();
        applyInfoStatus.setCreateUser(name);
        applyInfoStatus.setUniqueMark(uniqueMark);
        applyInfoStatus.setItemKey(applyInfoStatusType.getValue());
        applyInfoStatus.setItemReason(reason);
        applyInfoStatus.setStatus(status);
        applyInfoStatus.setIsDelete("0");
        applyInfoStatus.setSubmitTime(new Date());
        applyInfoStatusRepository.save(applyInfoStatus);
    }

    /**
     * 更新预审批状态表数据
     * @param approvalUuid              预审批唯一编号
     * @param applyInfoStatusType       ApplyInfoStatusType类型
     * @param createUser                创建用户
     * @param Reason                    审批备注
     * @param Status                    审批状态
     */
    private void updateApplyInfoStatus(String approvalUuid, ApplyInfoStatusType applyInfoStatusType, String createUser, String hplReason, String hplStatus) {
        ApplyInfoStatus applyInfoStatus = applyInfoStatusRepository.findTopByUniqueMarkAndItemKeyAndIsDelete(approvalUuid, applyInfoStatusType.getValue(), "0");
        if (applyInfoStatus == null) {
            applyInfoStatus = new ApplyInfoStatus();
            applyInfoStatus.setItemKey(applyInfoStatusType.getValue());
            applyInfoStatus.setIsDelete("0");
            applyInfoStatus.setUniqueMark(approvalUuid);
            applyInfoStatus.setCreateUser(createUser);
            applyInfoStatus.setSubmitTime(new Date());
        }
        applyInfoStatus.setItemReason(hplReason);
        applyInfoStatus.setStatus(hplStatus);
        applyInfoStatus.setResultTime(new Date());
        //得到提交和返回结果的毫秒差
        long interval = applyInfoStatus.getResultTime().getTime() - applyInfoStatus.getSubmitTime().getTime();
        applyInfoStatus.setInterval(interval + "");
        applyInfoStatusRepository.save(applyInfoStatus);
    }

//    private String setSubmitResult(String userName, String applyId, String applyIdOfMaster, String contractId) {
//        if (applyIdOfMaster == null) {
//            applyIdOfMaster = getRandomStr();
//        }
//        String lineTxt = "";
//        try {
//            String encoding = "UTF-8";
//            File file = new File("D:/text/SubmitResult.txt");
//            if (file.isFile() && file.exists()) { //判断文件是否存在
//                InputStreamReader read = new InputStreamReader(
//                        new FileInputStream(file), encoding);//考虑到编码格式
//                BufferedReader bufferedReader = new BufferedReader(read);
//                lineTxt = bufferedReader.readLine();
//                System.out.println(lineTxt);
//                read.close();
//            } else {
//                System.out.println("找不到指定的文件");
//            }
//        } catch (Exception e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//        lineTxt = lineTxt.replace("userNameParam", userName).replace("applyIdParam", applyId).
//                replace("applyIdOfMasterParam", applyIdOfMaster).replace("contractIdParam", contractId != null ? contractId : "");
//        return lineTxt;
//    }
//
//    private String getRandomStr() {
//        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//        char[] c = s.toCharArray();
//        Random random = new Random();
//        StringBuffer result = new StringBuffer("");
//        for (int i = 0; i < 2; i++) {
//            result.append(c[random.nextInt(c.length)]);
//        }
//        int max = 9999;
//        int min = 1000;
//
//        int num = random.nextInt(max) % (max - min + 1) + min;
//        return result.toString() + String.valueOf(num);
//    }
//
//    /**
//     * 修改证人状态
//     *
//     * @param authItem   认证途径
//     * @param uniqueMark:申请编号
//     * @return
//     */
//    public ResponseEntity<Message> alterCertificationStatus(String uniqueMark, String authItem) {
//        // 版本号
//        String version = versionProperties.getVersion();
//        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
//        if (applyInfo == null) {
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
//        }
//        String certificationStatus = applyInfo.getCertificationStatus();
//        if(certificationStatus == null || certificationStatus.isEmpty()){
//            certificationStatus = CertificationStatus.MOREN.code();
//        }
//        if(CertificationStatus.WEIXIN.value().equals(authItem)){
//            certificationStatus = "1".concat(certificationStatus.substring(1));
//        }else if(CertificationStatus.ZHIFUBAO.value().equals(authItem)){
//            certificationStatus = certificationStatus.substring(0,1).concat("1").concat(certificationStatus.substring(2));
//        }else if(CertificationStatus.TAOBAO.value().equals(authItem)){
//            certificationStatus = certificationStatus.substring(0,2).concat("1");
//        }else{
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"不存在的认证途径"), HttpStatus.OK);
//        }
//        applyInfo.setCertificationStatus(certificationStatus);
//        applyInfoRepository.save(applyInfo);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//    }

    /**
     * 在线申请二期客户信息页面初期化
     *
     * @param applyNum:申请编号
     * @return
     */
//    public ResponseEntity<Message> initCustomerInfo(String applyNum) {
//        // 版本号
//        String version = versionProperties.getVersion();
//        List<Approval> approvalList = approvalRepository.findAllInfo(applyNum, version);
//        if (approvalList.isEmpty()) {
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
//        }
//        ApplyFromDto resultDto = new ApplyFromDto();
//        try {
//            resultDto = deserialize(ApplyFromDto.class, approvalList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
//        }
//        CustomerInfoDto dto = new CustomerInfoDto();
//        if(resultDto != null){
//            dto.setRealDriverName(resultDto.getIdCardInfoDto().getName());
//            dto.setRealDriverMobile(resultDto.getOtherInfoDto().getPhoneNumber());
//            dto.setMarriage(resultDto.getMateInfoDto() == null ? "1" : "0");
//        }
//
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
//    }

//    /**
//     * 查询优质认证不同状态的数量
//     *
//     * @param userName
//     * @return
//     */
//    public ResponseEntity<Message> getAuthCount(String userName) {
//        // 版本号
//        String version = versionProperties.getVersion();
//        List<ApplyInfoNew> dataList = new ArrayList();
////        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
////        if(sysUser == null){
////            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
////        }
//        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
//        if("ERROR".equals(responseEntity.getBody().getStatus())){
//            return responseEntity;
//        }
//        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
//
//        dataList = applyInfoRepository.findAuthCount(userNameList, version);
//        long auth = 0L;
//        long noAuth = 0L;
//        for (ApplyInfoNew applyInfo : dataList) {
//            String status = applyInfo.getCertificationStatus();
//            if (status == null || status.isEmpty() || CertificationStatus.MOREN.code().equals(status)){
//                noAuth = noAuth + 1L;
//            } else if (status != null && !status.isEmpty() && !CertificationStatus.MOREN.code().equals(status)){
//                auth = auth + 1L;
//            }
//        }
//        AuthCountDto authCountDto = new AuthCountDto();
//        authCountDto.setAuthCount(auth);
//        authCountDto.setNoAuthCount(noAuth);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, authCountDto), HttpStatus.OK);
//    }

    /**
     * 查询优质认证【未认证/已认证】列表
     *
     * @param userName
     * @param type:{1：未认证；2：已认证}
     * @return
     */
//    public ResponseEntity<Message> getAuthInfo(String type, String userName) {
//        // 版本号
//        String version = versionProperties.getVersion();
//        List<Object> dataList = new ArrayList();
////        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
////        if(sysUser == null){
////            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
////        }
//        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
//        if("ERROR".equals(responseEntity.getBody().getStatus())){
//            return responseEntity;
//        }
//        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
//        if ("1".equals(type)) {
//            dataList = approvalRepository.findAuthInfo1(userNameList, version);
//        } else if ("2".equals(type)) {
//           dataList = approvalRepository.findAuthInfo2(userNameList, version);
//        }
//        List<AchieveInfoDto> resultList = new ArrayList();
//        for (Object object : dataList) {
//            Object[] objs = (Object[]) object;
//            AchieveInfoDto result = new AchieveInfoDto(objs);
//            resultList.add(result);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
//    }

    /**
     * 存储优质认证信息
     *
     * @param userName
     * @param type:{1：未认证；2：已认证}
     * @return
     */
//     public ResponseEntity<Message> getAuthParam(String uniqueMark, String sequenceNo, String token, String type, String userName) {
//        GXB01 gxb01 = new GXB01();
//        gxb01.setUniqueMark(uniqueMark);
//        gxb01.setSequenceNo(sequenceNo);
//        gxb01.setToken(token);
//        gxb01.setType(type);
//        gxb01.setCreateUser(userName);
//        gXB01Repository.save(gxb01);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//     }

    /**
     * 根据申请编号获取预审批所有信息（优质认证用）
     *
     * @param applyNum
     * @return
     */
//    public ApplyFromDto getApprovalInfo(String applyNum) {
//        ApplyFromDto resultDto = new ApplyFromDto();
//        // 版本号
//        String version = versionProperties.getVersion();
//        List<Approval> approvalList = approvalRepository.findApprovalInfo(applyNum, version);
//        if (approvalList.isEmpty()) {
//            return resultDto;
//        }
//        try {
//            resultDto = deserialize(ApplyFromDto.class, approvalList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultDto;
//    }

    /**
     * 根据uuid获取预审批所有信息（优质认证用）
     *
     * @param uniqueMark
     * @return
     */
//    public ApplyFromDto getApproval(String uniqueMark) {
//        ApplyFromDto resultDto = new ApplyFromDto();
//        // 版本号
//        String version = versionProperties.getVersion();
//        List<Approval> approvalList = approvalRepository.findByUniqueMarkAndVersion(uniqueMark, version);
//        if (approvalList.isEmpty()) {
//            return resultDto;
//        }
//        try {
//            resultDto = deserialize(ApplyFromDto.class, approvalList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultDto;
//    }

//
//    private List<String> getUserList(String userName, SysUser sysUser) {
//        List<String> userNameList = new ArrayList();
//        Long xtjgid = sysUser.getXTJGID(); //系统机构id
//        userNameList.add(userName);
//        String xtbmmc = sysUser.getXTBMMC() == null ||
//                sysUser.getXTBMMC().isEmpty() ? "ZJL" : sysUser.getXTBMMC(); //经销商部门，为空默认经销商（即总经理）
//        String name  = userName;
//        if("ZJL".equals(xtbmmc)){
//            SysUserRole zjl = sysUserRoleRepository.getZJL(xtjgid);
//            if(zjl == null){
//                return userNameList;
//            } else {
//                name = zjl.getXtczdm();
//            }
//        }
//        List<SysUserRole> roleList = sysUserRoleRepository.getApplyGroupList(name);
//        if(roleList != null){
//            for(SysUserRole item : roleList){
//                userNameList.add(item.getXtczdm());
//            }
//        }
//        return userNameList;
//    }


    //在线助力融产品审批拒绝接收微信推送
//    public ResponseEntity<Message> receiveApplyResult(ApplyResultDto applyResultDto){
//        logger.info("applyResultDto={}", applyResultDto);
//        String version = versionProperties.getVersion();
//        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(applyResultDto.getUniqueMark(),version);
//        //通过主系统申请编号查询
//        if(applyInfo==null && applyResultDto.getApplyNum() != null && !"".equals(applyResultDto.getApplyNum().trim())){
//            applyInfo = applyInfoNewRepository.findByApplyNum(applyResultDto.getApplyNum());
//        }
//        if(applyInfo != null && ("1".equals(applyResultDto.getType()) || "2".equals(applyResultDto.getType()))){
//            applyInfo.setApplyNum(applyResultDto.getApplyNum());
//            applyInfo.setApprovalUuid(applyResultDto.getUniqueMark());
//            applyInfo.setStatus(ApprovalType.REFUSE_NOREASON.code());
//            applyInfo.setReason_weBank(applyResultDto.getResultReason());
//            applyInfo.setUpdateTime(new Date());
//            applyInfoNewRepository.save(applyInfo);
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
//        }
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "审批结果推送失败,未查询到订单信息"), HttpStatus.OK);
//    }

    /**
     * 接收主系统推送过来的微信单子审批结果
     *
     * @param wxDto
     * @return
     */
    @Transactional
    public ResponseEntity<Message> receiveFinancePreApplyResultWx(FinancePreApplyResultWxDto wxDto) {
        logger.info("wxDto={}", wxDto);
        if(wxDto.getApplyNum() == null || wxDto.getApplyNum().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "审批结果推送失败,申请编号不可为空"), HttpStatus.OK);
        }
        ApplyInfoNew applyInfo = new ApplyInfoNew();

        //保存天启和微信
        ApplyRecord tqapplyRecord = new ApplyRecord();
        ApplyRecord wxapplyRecord = new ApplyRecord();

        ApplyInfoNew oldApplyInfo = applyInfoNewRepository.findByApplyNum(wxDto.getApplyNum());
        List<Approval> oldApprovalList = approvalRepository.findUserBasicInfo(wxDto.getUniqueMark(), versionProperties.getVersion());
        //微信申请的单子
        if(oldApplyInfo != null){
            applyInfoNewRepository.delete(oldApplyInfo);
        }
        if(oldApplyInfo == null){
            // 构建apply_info表数据
            applyInfo.setOrigin("1");
            applyInfo.setWxState("0");

            wxapplyRecord.setOrigin(OriginType.WE_BANK.code());
            wxapplyRecord.setStatus("0");
        //在线助力融，推到到微信的单子
        }else {
            //推送到微信的单子，信息已完善
            if(OriginType.ONLINE_HELP.code().equals(applyInfo.getOrigin())){
                applyInfo.setOrigin(OriginType.ONLINE_HELP.code());
                applyInfo.setWxState("1");
                wxapplyRecord.setOrigin(OriginType.ONLINE_HELP.code());
                wxapplyRecord.setStatus("1");
            }else {
                applyInfo.setOrigin("1");
                applyInfo.setWxState("0");
                wxapplyRecord.setOrigin(OriginType.WE_BANK.code());
                wxapplyRecord.setStatus("0");
            }
        }
        if (!oldApprovalList.isEmpty()) {
            approvalRepository.delete(oldApprovalList);
        }


        //构建approval表数据
        List<Approval> approvalList = addInfo(wxDto);
        if(approvalList == null || approvalList.isEmpty()){
            logger.info("审批结果推送失败,必要参数缺失");
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "审批结果推送失败,必要参数缺失"), HttpStatus.OK);
        }
        approvalRepository.save(approvalList);
        //调用订单绑定接口，自己绑定给自己
        ResponseEntity<Message> responseEntity = orderService.orderBind(
                wxDto.getUserId(), wxDto.getUserId(),
                wxDto.getApplyNum(),wxDto.getName());
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        applyInfo.setApprovalUuid(wxDto.getUniqueMark());
        applyInfo.setApplyNum(wxDto.getApplyNum());
        applyInfo.setHplStatus(ApprovalType.PASS.code());
        applyInfo.setHplReason("人工");
        applyInfo.setProductReason("一审通过");
        applyInfo.setCreateUser(wxDto.getUserId());
        applyInfo.setUpdateUser(wxDto.getUserId());
        applyInfo.setVersion(versionProperties.getVersion());
        applyInfoNewRepository.save(applyInfo);

        //天启
        tqapplyRecord.setApplyNum(wxDto.getApplyNum());
        tqapplyRecord.setApprovalUuid(wxDto.getUniqueMark());
        tqapplyRecord.setStatus(ApprovalType.PASS.code());
        tqapplyRecord.setReason("人工");
        tqapplyRecord.setCreateUser(wxDto.getUserId());

        applyRecordRepository.save(tqapplyRecord);

        //微信
        wxapplyRecord.setApplyNum(wxDto.getApplyNum());
        wxapplyRecord.setApprovalUuid(wxDto.getUniqueMark());
        wxapplyRecord.setReason("一审通过");
        wxapplyRecord.setCreateUser(wxDto.getUserId());

        applyRecordRepository.save(wxapplyRecord);

        return responseEntity;
    }

    /**
     * 构建approval表数据
     * 微盟贷 - 微信过来的数据
     * @param wxDto
     * @return
     */
    private List<Approval> addInfo(FinancePreApplyResultWxDto wxDto) {
        String userName = wxDto.getUserId();
        String name = wxDto.getName();
        String idCard = wxDto.getIdCard();
        String phoneNum = wxDto.getPhoneNum();
        String bank = wxDto.getBank();
        String bankNum = wxDto.getBankNum();
        // check主系统推过来的信息是否完整
        if(!StringUtils.isNoneEmpty(userName, name, idCard, phoneNum, bank, bankNum)){
            return null;
        }
        List<Approval> approvalList = new ArrayList();
        Approval approval;
        for(int i=0;i<5;i++){
            approval = new Approval();
            if(i==0){
                // 新增纵向表数据_姓名
                approval.setItemKey("#idCardInfoDto#name");
                approval.setItemStringValue(name);
            }else if(i==1){
                // 新增纵向表数据_身份证号
                approval.setItemKey("#idCardInfoDto#idCardNum");
                approval.setItemStringValue(idCard);
            }else if(i==2){
                // 新增纵向表数据_手机号
                approval.setItemKey("#otherInfoDto#phoneNumber");
                approval.setItemStringValue(phoneNum);
            }else if(i==3){
                // 新增纵向表数据_银行名称
                approval.setItemKey("#bankCardInfoDto#bank");
                approval.setItemStringValue(bank);
            }else if(i==4){
                // 新增纵向表数据_银行卡号
                approval.setItemKey("#bankCardInfoDto#accountNum");
                approval.setItemStringValue(bankNum);
            }
            approval.setCreateUser(userName);
            approval.setItemType(1);
            approval.setUniqueMark(wxDto.getUniqueMark());
            approval.setVersion(versionProperties.getVersion());
            approvalList.add(approval);
        }
        return approvalList;
    }


    /**
     * 构建approval表数据
     * 微盟贷 - 在线助力融
     * @param wxDto
     * @return
     */
    private List<Approval> addOnlineHelpInfo(BasicInfoDto wxDto) {
        String userName = wxDto.getFpName();
        String name = wxDto.getName();
        String idCard = wxDto.getIdCard();
        String phoneNum = wxDto.getPhoneNum();
        String bank = wxDto.getBank();
        String bankNum = wxDto.getBankCardNum();
        // check主系统推过来的信息是否完整
        if(!StringUtils.isNoneEmpty(userName, name, idCard, phoneNum, bank, bankNum)){
            return null;
        }
        List<Approval> oldApprovalList = approvalRepository.findWeBankUserBasicInfo(wxDto.getUniqueMark(), versionProperties.getVersion());
        if (!oldApprovalList.isEmpty()) {
            approvalRepository.delete(oldApprovalList);
        }
        List<Approval> approvalList = new ArrayList();
        Approval approval;
        for(int i=0;i<6;i++){
            approval = new Approval();
            if(i==0){
                // 新增纵向表数据_姓名
                approval.setItemKey("#idCardInfoDto#name");
                approval.setItemStringValue(name);
            }else if(i==1){
                // 新增纵向表数据_身份证号
                approval.setItemKey("#idCardInfoDto#idCardNum");
                approval.setItemStringValue(idCard);
            }else if(i==2){
                // 新增纵向表数据_手机号
                approval.setItemKey("#bankCardInfoDto#bankPhoneNum");
                approval.setItemStringValue(phoneNum);
            }else if(i==3){
                // 新增纵向表数据_银行名称
                approval.setItemKey("#bankCardInfoDto#bank");
                approval.setItemStringValue(bank);
            }else if(i==4){
                // 新增纵向表数据_银行卡号
                approval.setItemKey("#bankCardInfoDto#accountNum");
                approval.setItemStringValue(bankNum);
            } else if(i==5){
            // 新增纵向表数据_银行卡号
            approval.setItemKey("#bankCardInfoDto#name");
            approval.setItemStringValue(name);
            }
            approval.setCreateUser(userName);
            approval.setItemType(1);
            approval.setUniqueMark(wxDto.getUniqueMark());
            approval.setVersion(versionProperties.getVersion());
            approvalList.add(approval);
        }
        return approvalList;
    }




    /**
     * 构建approval表数据
     *两证一卡
     * @param approvalImagesDto
     * @return
     */
    private List<Approval> addFileInfo(ApprovalImagesDto approvalImagesDto, String uniqueMark) {
        String idCardFrontImg = approvalImagesDto.getFrontImg();
        String idCardBehindImg = approvalImagesDto.getBehindImg();
        String driveLicenceImg = approvalImagesDto.getDriveLicenceImg();
        String bankImg = approvalImagesDto.getBankImg();
        // check主系统推过来的信息是否完整
        if(!StringUtils.isNoneEmpty(idCardFrontImg, idCardBehindImg, driveLicenceImg, bankImg)){
            return null;
        }
        List<Approval> approvalList = new ArrayList();
        Approval approval;
        for(int i=0;i<4;i++){
            approval = new Approval();
            if(i==0){
                // 新增纵向表数据_姓名
                approval.setItemKey("#idCardInfoDto#name");
                approval.setItemStringValue(idCardFrontImg);
            }else if(i==1){
                // 新增纵向表数据_身份证号
                approval.setItemKey("#idCardInfoDto#idCardNum");
                approval.setItemStringValue(idCardBehindImg);
            }else if(i==2){
                // 新增纵向表数据_手机号
                approval.setItemKey("#otherInfoDto#phoneNumber");
                approval.setItemStringValue(driveLicenceImg);
            }else if(i==3) {
                // 新增纵向表数据_银行名称
                approval.setItemKey("#bankCardInfoDto#bank");
                approval.setItemStringValue(bankImg);
            }
//            approval.setCreateUser(userName);
            approval.setItemType(1);
            approval.setUniqueMark(uniqueMark);
            approval.setVersion(versionProperties.getVersion());
            approvalList.add(approval);
        }
        return approvalList;
    }



    /**
     * 获取申请列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getApplyList(String userName, String condition){
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> result= approvalRepository.findApplyList(userNameList, version, condition);
        if(result == null || result.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<ApplyLocalInfoDto> resultList = new ArrayList();
        Object[] objs;
        ApplyLocalInfoDto recoveryListDto;
        for (Object object : result) {
            objs = (Object[]) object;
            recoveryListDto = new ApplyLocalInfoDto(objs);
            resultList.add(recoveryListDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 根据审批状态获取申请列表
     * @param userName
     * @param status 状态
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getApplyListByStatus (String userName, String status, String condition){
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = applyService.getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> result= approvalRepository.findApplyListByStatus(userNameList, version, status, condition);
        if(result == null || result.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<ApplyLocalInfoDto> resultList = new ArrayList();
        Object[] objs;
        ApplyLocalInfoDto recoveryListDto;
        for (Object object : result) {
            objs = (Object[]) object;
            recoveryListDto = new ApplyLocalInfoDto(objs);
            resultList.add(recoveryListDto);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    public Bank checkCardNum(String cardNum) {
        Bank bank = null;
        if(cardNum != null && !cardNum.isEmpty()){
            if(cardNum.length() != 19 && cardNum.length() != 16 && cardNum.length() != 18 && cardNum.length() != 17){
                return bank;
            }
            for(int i=10;i>=3;i--){
                String param = cardNum.substring(0,i);
                bank = bankRepository.findByBin(param);
                if(bank != null){
                    break;
                }
            }
        }
        return bank;
    }

    private FirstApplyDto buildFirstApplyDto(BasicInfoDto basicInfoDto) {
        FirstApplyDto result = new FirstApplyDto(basicInfoDto, wzProperties);
        return result;
    }


    //根据银行卡号获取银行名称
    public ResponseEntity<Message> getBank(String bankCardNum){
        Bank bank = checkCardNum(bankCardNum);
        if(bank == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未找到相关银行，请检查银行卡号是否填写正确"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, bank), HttpStatus.OK);
    }

    /**
     * 根据身份证id获取到已经通过微众预审批记录
     * @param idCard
     * @return
     */
    public ResponseEntity<Message> getwzApplyInfoByIdCard(String idCard) {
        // 在太盟宝中查找通过一审的记录，如果没有找到则在微信端去查找 -- 2018/9/13 18:30 By ChengQiChuan
        // 一审通过在微信端是 3，太盟宝APP 是 2
        WzApplyInfo wzApplyInfo = wzApplyInfoRepository.findTop1ByIdCardAndStatusAndCreateTimeAfterOrderByCreateTimeDesc(idCard,
                2+"", DateUtils.getBeforeDateByDay(new Date(),-30));
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, wzApplyInfo), HttpStatus.OK);
    }

}
