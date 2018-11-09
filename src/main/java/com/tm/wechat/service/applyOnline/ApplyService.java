package com.tm.wechat.service.applyOnline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.RedisProperties;
import com.tm.wechat.config.VersionProperties;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dao.*;
import com.tm.wechat.domain.*;
import com.tm.wechat.dto.apply.*;
import com.tm.wechat.dto.apply.file.ApplyFileDto;
import com.tm.wechat.dto.apply.file.GuaranteeFileDto;
import com.tm.wechat.dto.apply.file.JointFileDto;
import com.tm.wechat.dto.apply.file.MateFileDto;
import com.tm.wechat.dto.approval.ApplyFromDto;
import com.tm.wechat.dto.approval.ApprovalCountDto;
import com.tm.wechat.dto.communication.CommunicationFileDto;
import com.tm.wechat.dto.communication.OnlineMessageListDto;
import com.tm.wechat.dto.contract.ContractMsgDto;
import com.tm.wechat.dto.contract.ContractStateDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.order.OrganizationDto;
import com.tm.wechat.dto.order.OrganizationResult;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.dto.result.UsedCarResult;
import com.tm.wechat.service.ContractService;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.utils.apply.ApplyItemDeserialize;
import com.tm.wechat.utils.apply.ApplyItemSerialize;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.ItemDeserialize;
import com.tm.wechat.utils.commons.TaiMengDES;
import com.tm.wechat.consts.ApprovalType;
import com.tm.wechat.consts.UsedCarEvaluationStatus;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tm.wechat.utils.commons.ItemDeserialize.deserialize;


/**
 * Created by pengchao on 2017/7/11.
 */
@Service
public class ApplyService extends BaseService {

    @Autowired
    private ContractService contractInterface;

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private VersionProperties versionProperties;

    @Autowired
    private ApplyInfoRepository applyInfoRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsedCarEvaluationInterface usedCarEvaluationInterface;

    @Autowired
    private CoreSystemInterface coreSystemInterface;

    @Autowired
    private CoreSystemService coreSystemService;

    // redis 缓存 -- By ChengQiChuan 2018/10/11 11:02
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private WechatProperties wechatProperties;

    @Autowired
    private ApplyInfoNewRepository applyInfoNewRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApplyService.class);
    /**
     * 添加、修改产品方案
     * @param productPlanDto
     * @param uniqueMark
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addProductPlan(ProductPlanDto productPlanDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setProductPlanDto(productPlanDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findProductPlan(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
            //修改产品方案时删除车辆信息和车辆、车辆抵押信息、融资信息
            List<Apply> applyList = applyRepository.findProductInfo(uniqueMark, version);
            if(!applyList.isEmpty()){
                applyRepository.delete(applyList);
            }
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
//        ApplyInfo applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
//        ApplyInfo newApplyInfo = new ApplyInfo();
//        if(applyInfo == null){
//            newApplyInfo.setApprovalUuid(uniqueMark);
//            newApplyInfo.setStatus(ApprovalType.NEW.code());
//            newApplyInfo.setCreateUser(userName);
//            newApplyInfo.setVersion(version);
//            applyInfoRepository.save(newApplyInfo);
//        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询产品方案信息
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getProductPlan(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findProductPlan(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new ProductPlanDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getProductPlanDto()), HttpStatus.OK);
    }


    /**
     * 添加车辆信息
     * @param carInfoDto
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addCarInfo(CarInfoDto carInfoDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setCarInfoDto(carInfoDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findCarInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
            //修改车辆信息时删除融资信息
            List<Apply> applyList = applyRepository.findFinanceInfo(uniqueMark, version);
            if(!applyList.isEmpty()){
                applyRepository.delete(applyList);
            }
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询车辆信息
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getCarInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findCarInfo(uniqueMark, version);
        List<Apply> usedCarEvaluationResult = applyRepository.findUsedCarEvaluationResult(uniqueMark, version);
        if (approvalList.isEmpty()){
            //二手车评估结果为空
            if(usedCarEvaluationResult.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new CarInfoDto()), HttpStatus.OK);
            }
            ApplyDto totalDto = new ApplyDto();
            try {
                totalDto = ApplyItemDeserialize.deserialize(ApplyDto.class, usedCarEvaluationResult);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "反序列化异常"), HttpStatus.OK);
            }
            UsedCarEvaluationResultDto usedCarEvaluationResultDto = totalDto.getUsedCarEvaluationResultDto();
            //添加评估结果至车辆信息
            CarInfoDto carInfoDto = new CarInfoDto();
            carInfoDto.setSecondDate(usedCarEvaluationResultDto.getDateOfProduction());
            carInfoDto.setSecondOfficialPrice(usedCarEvaluationResultDto.getPrice());
            carInfoDto.setSecondYears(usedCarEvaluationResultDto.getYears());
            totalDto.setCarInfoDto(carInfoDto);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,carInfoDto), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "反序列化异常"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getCarInfoDto()), HttpStatus.OK);
    }

    /**
     * 添加车辆及抵押信息
     * @param carAndPledgeInfoDto
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addCarAndPledgeInfo(CarAndPledgeInfoDto carAndPledgeInfoDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setCarInfoDto(carAndPledgeInfoDto.getCarInfoDto());
        totalDto.setCarPledgeDto(carAndPledgeInfoDto.getCarPledgeDto());
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        //查询之前的车辆及抵押信息
        approvalListOld = applyRepository.findProductInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询车辆及抵押信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getCarAndPledgeInfo(String uniqueMark) {
        // 版本号
        CarAndPledgeInfoDto carAndPledgeInfoDto = new CarAndPledgeInfoDto();
        String version = versionProperties.getVersion();
        List<Apply> carInfo = applyRepository.findCarInfo(uniqueMark, version);
        List<Apply> pledgeInfo = applyRepository.findCarPledgeInfo(uniqueMark, version);
        List<Apply> usedCarEvaluationResult = applyRepository.findUsedCarEvaluationResult(uniqueMark, version);
        //车辆信息为空判断二手车评估信息是否为空
        if (carInfo.isEmpty()){
            //二手车评估结果不为空
            if(!usedCarEvaluationResult.isEmpty()){
                ApplyDto totalDto = new ApplyDto();
                try {
                    totalDto = ApplyItemDeserialize.deserialize(ApplyDto.class, usedCarEvaluationResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "反序列化异常"), HttpStatus.OK);
                }
                UsedCarEvaluationResultDto usedCarEvaluationResultDto = totalDto.getUsedCarEvaluationResultDto();
                //添加评估结果至车辆信息
                CarInfoDto carInfoDto = new CarInfoDto();
                carInfoDto.setSecondDate(usedCarEvaluationResultDto.getDateOfProduction());
                carInfoDto.setSecondOfficialPrice(usedCarEvaluationResultDto.getPrice());
                carInfoDto.setSecondYears(usedCarEvaluationResultDto.getYears());
                totalDto.setCarInfoDto(carInfoDto);
                carAndPledgeInfoDto.setCarInfoDto(carInfoDto);
            }
            carAndPledgeInfoDto.setCarInfoDto(new CarInfoDto());
        }
        if (pledgeInfo.isEmpty()){
            carAndPledgeInfoDto.setCarPledgeDto(new CarPledgeDto());
        }
        ApplyDto carInfoDto = new ApplyDto();
        ApplyDto pledgeInfoDto = new ApplyDto();
        try {
            if(!carInfo.isEmpty()){
                carInfoDto = ApplyItemDeserialize.deserialize(ApplyDto.class, carInfo);
                carAndPledgeInfoDto.setCarInfoDto(carInfoDto.getCarInfoDto());
            }
            if(!pledgeInfo.isEmpty()){
                pledgeInfoDto = ApplyItemDeserialize.deserialize(ApplyDto.class, pledgeInfo);
                carAndPledgeInfoDto.setCarPledgeDto(pledgeInfoDto.getCarPledgeDto());
            }
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "反序列化异常"), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, carAndPledgeInfoDto), HttpStatus.OK);
    }

    /**
     * 添加车辆抵押信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addCarPledgeInfo(CarPledgeDto carPledgeDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setCarPledgeDto(carPledgeDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findCarPledgeInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询车辆抵押信息
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getCarPledgeInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findCarPledgeInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new CarPledgeDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getCarPledgeDto()), HttpStatus.OK);
    }


    /**
     * 添加融资信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addFinanceInfo(FinanceInfoDto financeInfoDto, String uniqueMark, String userName){
        //融资金额
        double financeAmount = Double.parseDouble(financeInfoDto.getFinanceAmount());
        if(financeAmount > 100000){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资额超出融资上限10万！"), HttpStatus.OK);
        }
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setFinanceInfoDto(financeInfoDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findFinanceInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询融资信息信息
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getFinanceInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findFinanceInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new FinanceInfoDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getFinanceInfoDto()), HttpStatus.OK);
    }

    /**
     * 添加客户详细信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addDetailedInfo(DetailedDto detailedDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setDetailedDto(detailedDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findDetailedInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询客户详细信息
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getDetailedInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findDetailedInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new DetailedDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getDetailedDto()), HttpStatus.OK);
    }

    /**
     * 添加客户职业信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addWorkInfo(WorkInfoDto workInfoDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setWorkInfoDto(workInfoDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findWorkInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询客户职业信息
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getWorkInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findWorkInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new WorkInfoDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getWorkInfoDto()), HttpStatus.OK);
    }

    /**
     * 添加共申人信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addJointInfo(JointInfoDto jointInfoDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setJointInfoDto(jointInfoDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        List<Approval> approvalList = approvalRepository.findIdCardInfo(uniqueMark, version);
        approvalListOld = applyRepository.findJointInfo(uniqueMark, version);
        ApplyFromDto applyFromDto = new ApplyFromDto();
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
            applyFromDto = ItemDeserialize.deserialize(ApplyFromDto.class, approvalList);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        //主审人、共申人不能是同一个人
        if("身份证".equals(jointInfoDto.getJointIdType())){
            if(applyFromDto.getIdCardInfoDto().getName().equals(jointInfoDto.getJointName()) && applyFromDto.getIdCardInfoDto().getIdCardNum().equals(jointInfoDto.getJointIdty())){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"共申人不能是本人"), HttpStatus.OK);
            }
        }else if(applyFromDto.getIdCardInfoDto().getName().equals(jointInfoDto.getJointName())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"共申人不能是本人"), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }



    /**
     * 查询共申人信息
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getJointInfo(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findJointInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new JointInfoDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getJointInfoDto()), HttpStatus.OK);
    }

    /**
     * 添加客户地址信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addAddressInfo(AddressInfoDto addressInfoDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setAddressInfoDto(addressInfoDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findAddressInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询客户地址信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getAddressInfo(String uniqueMark) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findAddressInfo(uniqueMark, version);
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS,new AddressInfoDto()), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getAddressInfoDto()), HttpStatus.OK);
    }



    /**
     * 添加客户附件信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addApplyFileInfo(ApplyFileDto applyFileDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setApplyFileDto(applyFileDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findApplyFileInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }



    /**
     * 查询客户附件信息（在线沟通用）
     * @param applyNum 申请编号
     * @return
     */
    public List<OnlineMessageListDto> getFileInfo(String applyNum) {
        List<OnlineMessageListDto> resultList = new ArrayList();
        // 版本号
        ApplyInfoNew applyInfo = applyInfoNewRepository.findByApplyNum(applyNum);
        if(applyInfo == null){
            return resultList;
        }
        String uniqueMark = applyInfo.getApprovalUuid();
        String version = versionProperties.getVersion();
        List<Apply> applyList = applyRepository.findApplyFileInfo(uniqueMark, version);
        //若无附件信息，到详细信息页查找征信附件，到预审批中查找两证一卡附件（第一次进入附件页时，applyList为空）
        if (applyList.isEmpty()){
            applyList = applyRepository.findDetailedInfo(uniqueMark, version);
            List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
            OnlineMessageListDto onlineMessageListDto;
            for(Apply apply : applyList){
                if("#detailedDto#letterOfCredit".equals(apply.getItemKey()) ||
                   "#detailedDto#handHoldLetterOfCredit".equals(apply.getItemKey())){
                    onlineMessageListDto = new OnlineMessageListDto();
                    onlineMessageListDto.setOperationTime((CommonUtils.getStrDate(apply.getUpdateTime(), CommonUtils.simpleDateFormat_14)));
                    onlineMessageListDto.setOperator(apply.getCreateUser());
                    onlineMessageListDto.setUrl(apply.getItemStringValue());
                    if("#detailedDto#letterOfCredit".equals(apply.getItemKey())){
                        onlineMessageListDto.setType("征信授权书");
                    }
                    if("#detailedDto#handHoldLetterOfCredit".equals(apply.getItemKey())){
                        onlineMessageListDto.setType("手持征信授权书");
                    }
                    resultList.add(onlineMessageListDto);
                }
            }
            for(Approval approval : approvalList){
                if("#idCardInfoDto#frontImg".equals(approval.getItemKey()) ||
                        "#idCardInfoDto#behindImg".equals(approval.getItemKey()) ||
                        "#bankCardInfoDto#bankImg".equals(approval.getItemKey()) ||
                        "#driveLicenceInfoDto#licenceImg".equals(approval.getItemKey())){
                    onlineMessageListDto = new OnlineMessageListDto();
                    onlineMessageListDto.setOperationTime((CommonUtils.getStrDate(approval.getUpdateTime(), CommonUtils.simpleDateFormat_14)));
                    onlineMessageListDto.setOperator(approval.getCreateUser());
                    onlineMessageListDto.setUrl(approval.getItemStringValue());
                    if("#idCardInfoDto#frontImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("身份证");
                    }
                    if("#idCardInfoDto#behindImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("身份证");
                    }
                    if("#bankCardInfoDto#bankImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("银行卡");
                    }
                    if("#driveLicenceInfoDto#licenceImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("驾驶证");
                    }
                    resultList.add(onlineMessageListDto);
                }
            }
            return resultList;
        }
        //若有附件信息，则直接返回
        OnlineMessageListDto onlineMessageListDto;
        for(Apply apply : applyList){
            onlineMessageListDto = new OnlineMessageListDto();
            onlineMessageListDto.setOperationTime((CommonUtils.getStrDate(apply.getUpdateTime(), CommonUtils.simpleDateFormat_14)));
            onlineMessageListDto.setOperator(apply.getCreateUser());
            onlineMessageListDto.setUrl(apply.getItemStringValue());
            if("#applyFileDto#letterOfCredit".equals(apply.getItemKey())){
                onlineMessageListDto.setType("征信授权书");
            }
            if("#applyFileDto#handHoldLetterOfCredit".equals(apply.getItemKey())){
                onlineMessageListDto.setType("手持征信授权书");
            }
            if("#applyFileDto#applyForm".equals(apply.getItemKey())){
                onlineMessageListDto.setType("申请表");
            }
            if("#applyFileDto#frontImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("身份证");
            }
            if("#applyFileDto#behindImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("身份证");
            }
            if("#applyFileDto#driveLicenceImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("驾驶证");
            }
            if("#applyFileDto#bankImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("银行卡");
            }
            resultList.add(onlineMessageListDto);
        }
        return resultList;
    }


    /**
     * 查询预审批未提交到主系统的附件
     * @param applyNum
     * @return
     */
    public List<CommunicationFileDto> getApprovalFileInfo(String applyNum) {
        List<CommunicationFileDto> resultList = new ArrayList();
        // 版本号
        ApplyInfoNew applyInfo = applyInfoNewRepository.findByApplyNum(applyNum);
        //若预审批附件已提交到主系统则，不在提交
        if(applyInfo == null || "1".equals(applyInfo.getApprovalFileState())){
            return resultList;
        }
        String uniqueMark = applyInfo.getApprovalUuid();
        String version = versionProperties.getVersion();
        List<Apply> applyList = applyRepository.findApplyFileInfo(uniqueMark, version);
        //若无附件信息，到详细信息页查找征信附件，到预审批中查找两证一卡附件（第一次进入附件页时，applyList为空）
        if (applyList.isEmpty()){
            applyList = applyRepository.findDetailedInfo(uniqueMark, version);
            List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
            CommunicationFileDto onlineMessageListDto;
            for(Apply apply : applyList){
                if("#detailedDto#letterOfCredit".equals(apply.getItemKey()) ||
                        "#detailedDto#handHoldLetterOfCredit".equals(apply.getItemKey())){
                    onlineMessageListDto = new CommunicationFileDto();
                    onlineMessageListDto.setUrl(apply.getItemStringValue());
                    if("#detailedDto#letterOfCredit".equals(apply.getItemKey())){
                        onlineMessageListDto.setType("征信授权书");
                    }
                    if("#detailedDto#handHoldLetterOfCredit".equals(apply.getItemKey())){
                        onlineMessageListDto.setType("手持征信授权书");
                    }
                    resultList.add(onlineMessageListDto);
                }
            }
            for(Approval approval : approvalList){
                if("#idCardInfoDto#frontImg".equals(approval.getItemKey()) ||
                        "#idCardInfoDto#behindImg".equals(approval.getItemKey()) ||
                        "#bankCardInfoDto#bankImg".equals(approval.getItemKey()) ||
                        "#driveLicenceInfoDto#licenceImg".equals(approval.getItemKey())){
                    onlineMessageListDto = new CommunicationFileDto();
                    onlineMessageListDto.setUrl(approval.getItemStringValue());
                    if("#idCardInfoDto#frontImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("身份证");
                    }
                    if("#idCardInfoDto#behindImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("身份证");
                    }
                    if("#bankCardInfoDto#bankImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("银行卡");
                    }
                    if("#driveLicenceInfoDto#licenceImg".equals(approval.getItemKey())){
                        onlineMessageListDto.setType("驾驶证");
                    }
                    resultList.add(onlineMessageListDto);
                }
            }
            return resultList;
        }
        //若有附件信息，则直接返回
        CommunicationFileDto onlineMessageListDto;
        for(Apply apply : applyList){
            onlineMessageListDto = new CommunicationFileDto();
            onlineMessageListDto.setUrl(apply.getItemStringValue());
            if("#applyFileDto#letterOfCredit".equals(apply.getItemKey())){
                onlineMessageListDto.setType("征信授权书");
            }
            if("#applyFileDto#handHoldLetterOfCredit".equals(apply.getItemKey())){
                onlineMessageListDto.setType("手持征信授权书");
            }
            if("#applyFileDto#applyForm".equals(apply.getItemKey())){
                onlineMessageListDto.setType("申请表");
            }
            if("#applyFileDto#frontImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("身份证");
            }
            if("#applyFileDto#behindImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("身份证");
            }
            if("#applyFileDto#driveLicenceImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("驾驶证");
            }
            if("#applyFileDto#bankImg".equals(apply.getItemKey())){
                onlineMessageListDto.setType("银行卡");
            }
            resultList.add(onlineMessageListDto);
        }
        return resultList;
    }

    /**
     * 查询客户附件信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getApplyFileInfo(String uniqueMark) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> applyList = applyRepository.findApplyFileInfo(uniqueMark, version);
        ApplyFileDto applyFileDto = new ApplyFileDto();
        ApplyDto resultDto = null;
        ApplyFromDto applyFromDto = null;
        //若无附件信息，到详细信息页查找征信附件，到预审批中查找两证一卡附件（第一次进入附件页时，applyList为空）
        if (applyList.isEmpty()){
            applyList = applyRepository.findDetailedInfo(uniqueMark, version);
            List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
            try {
                if(!approvalList.isEmpty()){
                    applyFromDto = deserialize(ApplyFromDto.class, approvalList);
                }
                if(!applyList.isEmpty()){
                    resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, applyList);
                }
            } catch(Exception e){
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            }
            //获取预审批2证一卡图片
            if(applyFromDto != null){
                applyFileDto.setFrontImg(applyFromDto.getIdCardInfoDto().getFrontImg());
                applyFileDto.setBehindImg(applyFromDto.getIdCardInfoDto().getBehindImg());
                applyFileDto.setDriveLicenceImg(applyFromDto.getDriveLicenceInfoDto().getLicenceImg());
                applyFileDto.setBankImg(applyFromDto.getBankCardInfoDto().getBankImg());
            }
            //获取详细信息中的征信附件
            if(resultDto != null){
                applyFileDto.setLetterOfCredit(resultDto.getDetailedDto().getLetterOfCredit());
                applyFileDto.setHandHoldLetterOfCredit(resultDto.getDetailedDto().getHandHoldLetterOfCredit());
            }
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyFileDto), HttpStatus.OK);
        }
        //若有附件信息，则直接返回
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, applyList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getApplyFileDto()), HttpStatus.OK);
    }


    /**
     * 添加共申人附件信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addJointFileInfo(JointFileDto jointFileDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setJointFileDto(jointFileDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findJointFileInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }



    /**
     * 添加担保人附件信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addGuaranteeFileInfo(GuaranteeFileDto guaranteeFileDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setGuaranteeFileDto(guaranteeFileDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findGuaranteeFileInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 添加配偶附件信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addMateFileInfo(MateFileDto mateFileDto, String uniqueMark, String userName){
        // 版本号
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        totalDto.setMateFileDto(mateFileDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        approvalListOld = applyRepository.findMateFileInfo(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 查询共申人附件信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getJointFileInfo(String uniqueMark) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findJointFileInfo(uniqueMark, version);
        ApplyDto resultDto = new ApplyDto();
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, new JointFileDto()), HttpStatus.OK);
        }
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getJointFileDto()), HttpStatus.OK);
    }


    /**
     * 查询担保人附件信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getGuaranteeFileInfo(String uniqueMark) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findGuaranteeFileInfo(uniqueMark, version);
        ApplyDto resultDto = new ApplyDto();
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, new GuaranteeFileDto()), HttpStatus.OK);
        }
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getGuaranteeFileDto()), HttpStatus.OK);
    }


    /**
     * 查询配偶附件信息
     * @param uniqueMark 申请编号
     * @return
     */
    public ResponseEntity<Message> getMateFileInfo(String uniqueMark) {
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> approvalList = applyRepository.findMateFileInfo(uniqueMark, version);
        ApplyDto resultDto = new ApplyDto();
        if (approvalList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, new MateFileDto()), HttpStatus.OK);
        }
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto.getMateFileDto()), HttpStatus.OK);
    }

    /**
     *获取二手车评估单号
     * @return
     */
    public ResponseEntity<Message> getUsedCarEvaluationNum(){
        UsedCarResult usedCarResult = null;
        try {
            String result = usedCarEvaluationInterface.getCarBillId(CommonUtils.usedCarUsername, CommonUtils.usedCarFromSource);
            usedCarResult = objectMapper.readValue(result, UsedCarResult.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if(usedCarResult == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "预获取评估单号失败"), HttpStatus.OK);
        }
        if("true".equals(usedCarResult.getSuccess())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, usedCarResult.getObject()), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, usedCarResult.getMessage()), HttpStatus.OK);
        }
    }

    /**
     * 添加二手车评估信息
     * @param
     * @param uniqueMark 申请编号
     * @param userName
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addUsedCarEvaluationDto(UsedCarEvaluationDto usedCarEvaluationDto, String uniqueMark, String userName){
        if(usedCarEvaluationDto == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请完善信息！"), HttpStatus.OK);
        }
        if(usedCarEvaluationDto.getImages().size() >5){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "最多可上传5张照片，请勿重复提交！"), HttpStatus.OK);
        }
        String version = versionProperties.getVersion();
        ApplyDto totalDto = new ApplyDto();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到该订单信息"), HttpStatus.OK);
        }
        totalDto.setUsedCarEvaluationDto(usedCarEvaluationDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = new ArrayList();
        List<Apply> evaluationResult = new ArrayList();
        approvalListOld = applyRepository.findUsedCarEvaluation(uniqueMark, version);
        evaluationResult = applyRepository.findUsedCarEvaluationResult(uniqueMark, version);
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, uniqueMark, version, userName);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if(!evaluationResult.isEmpty()){
            applyRepository.delete(evaluationResult);
        }
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        UsedCarResult usedCarResult = null;
        try {
            JSONObject jsonObject = JSONObject.fromObject(usedCarEvaluationDto);
            String param = TaiMengDES.encryptBase64(jsonObject.toString(), CommonUtils.encryptKey);
            System.out.println("密文："+ param);
            System.out.println("解密："+ TaiMengDES.decryptBase64(param, CommonUtils.encryptKey));
//            String result = HttpRequestUtils.TestPost(param, CommonUtils.usedCarUsername, CommonUtils.usedCarFromSource);
            String result = usedCarEvaluationInterface.submitEvaluation(CommonUtils.usedCarUsername, CommonUtils.usedCarFromSource, param);
            usedCarResult = objectMapper.readValue(result, UsedCarResult.class);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "预评估提交失败"), HttpStatus.OK);
        }

        if(usedCarResult == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "预评估提交失败"), HttpStatus.OK);
        }
        if("true".equals(usedCarResult.getSuccess())){
            //更新评估状态为评估中
//            applyInfo.setUsedCarEvaluationStatus(UsedCarEvaluationStatus.SUBMIT.code());
            applyInfoNewRepository.save(applyInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, usedCarResult.getObject()), HttpStatus.OK);
        }else {
//            applyInfo.setUsedCarEvaluationStatus(UsedCarEvaluationStatus.NOT_SUBMIT.code());
            applyInfoNewRepository.save(applyInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, usedCarResult.getMessage()), HttpStatus.OK);
        }
    }


    /**
     * 查询二手车评估结果
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getUsedCarEvaluationResult(String uniqueMark, String userName) {
        // 版本号
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        List<Apply> usedCarEvaluation = applyRepository.findUsedCarEvaluation(uniqueMark, version);
        UsedCarEvaluationStatusDto usedCarEvaluationStatusDto = new UsedCarEvaluationStatusDto();
        //未提交预评估
        if(usedCarEvaluation.isEmpty()){
//            applyInfo.setUsedCarEvaluationStatus(UsedCarEvaluationStatus.NOT_SUBMIT.code());
            applyInfoNewRepository.save(applyInfo);
            usedCarEvaluationStatusDto.setStatus(UsedCarEvaluationStatus.NOT_SUBMIT.code());
            usedCarEvaluationStatusDto.setUsedCarEvaluationResultDto(new UsedCarEvaluationResultDto());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarEvaluationStatusDto), HttpStatus.OK);
        }
        List<Apply> approvalList = applyRepository.findUsedCarEvaluationResult(uniqueMark, version);
        //预评估审批中
        if (approvalList.isEmpty()){
//            usedCarEvaluationStatusDto.setStatus(applyInfo.getUsedCarEvaluationStatus());
            usedCarEvaluationStatusDto.setUsedCarEvaluationResultDto(new UsedCarEvaluationResultDto());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarEvaluationStatusDto), HttpStatus.OK);
        }
        ApplyDto resultDto = new ApplyDto();
        try {
            resultDto = ApplyItemDeserialize.deserialize(ApplyDto.class, approvalList);
        } catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
//        usedCarEvaluationStatusDto.setStatus(applyInfo.getUsedCarEvaluationStatus());
        usedCarEvaluationStatusDto.setUsedCarEvaluationResultDto(resultDto.getUsedCarEvaluationResultDto());
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, usedCarEvaluationStatusDto), HttpStatus.OK);
    }


    /**
     * 查询待提交列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getLocalInfo(String userName, String condition){
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> result= applyRepository.findLocalInfo(userNameList, version, condition);
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
     * 查询返回待修改列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getBackInfo(String userName, String condition){
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> dataList = applyRepository.findBackInfo(userNameList, version, condition);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<ApplyBackInfoDto> resultList = new ArrayList();
        Object[] objs;
        ApplyBackInfoDto result;
        for(Object object :dataList){
            objs = (Object[]) object;
            result = new ApplyBackInfoDto(objs);
            resultList.add(result);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }


    /**
     * 查询申请中列表
     * @param userName
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getSubmitInfo(String userName, String condition){
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<Object> dataList = applyRepository.findSubmitInfo(userNameList, version, condition);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<ApplySubmitInfoDto> resultList = new ArrayList();
        Object[] objs;
        ApplySubmitInfoDto result;
        for(Object object :dataList){
            objs = (Object[]) object;
            result = new ApplySubmitInfoDto(objs);
            resultList.add(result);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }

    /**
     * 查询审批完成列表
     * @param userName
     * @param searchType:{1:查询审批通过；2:查询拒绝；0:查询全部审批完成}
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    public ResponseEntity<Message> getAchieveInfo(String searchType, String userName, String condition){
        condition = CommonUtils.likePartten(condition);
        // 版本号
        String version = versionProperties.getVersion();
//        SysUser sysUser = sysUserRepository.findByXtczdmAndCustomer(userName,"taimeng");
//        if(sysUser == null){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
//        }
        ResponseEntity<Message> responseEntity = getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<String> status = new ArrayList();
        if ("1".equals(searchType)){
            status.add(ApprovalType.APPROVAL_PASS.code());
        } else if("2".equals(searchType)){
            status.add(ApprovalType.APPROVAL_REFUSE.code());
        } else if("0".equals(searchType)){
            status.add(ApprovalType.APPROVAL_PASS.code());
            status.add(ApprovalType.APPROVAL_REFUSE.code());
        }
        List<Object> dataList = applyRepository.findAchieveInfo(userNameList, version, status, condition);
        if(dataList == null || dataList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到数据"), HttpStatus.OK);
        }
        List<ApplyAchieveInfoDto> resultList = new ArrayList();
        Object[] objs;
        ApplyAchieveInfoDto result;
        for(Object object :dataList){
            objs = (Object[]) object;
            result = new ApplyAchieveInfoDto(objs);
            resultList.add(result);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultList), HttpStatus.OK);
    }

    /**
     * 新建申请不同审批状态的申请数量
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getNewApprovalCount(String userName){
        // 版本号
        String version = versionProperties.getVersion();
        int submitCount = 0;
        int backCount = 0;
        int approvalCount = 0;
        int passCount = 0;

        SysUser sysUser = getSysUser(userName, "taimeng");

        if(sysUser == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该账户不存在"), HttpStatus.OK);
        }

        ResponseEntity<Message> responseEntity = getUserList(userName);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        List<String> userNameList = (List<String>) responseEntity.getBody().getData();
        List<ApplyInfo> applyList = applyInfoRepository.findCount(userNameList, version);
        for(ApplyInfo applyInfo : applyList){
            if(ApprovalType.PASS.code().equals(applyInfo.getStatus()) && !"0".equals(applyInfo.getWxState())){
                submitCount++;
            }else if(ApprovalType.APPROVAL_BACK.code().equals(applyInfo.getStatus())){
                backCount++;
            }else if(ApprovalType.WAIT_APPROVAL.code().equals(applyInfo.getStatus())){
                approvalCount++;
            }else if(ApprovalType.APPROVAL_PASS.code().equals(applyInfo.getStatus()) || ApprovalType.APPROVAL_REFUSE.code().equals(applyInfo.getStatus())){
                passCount++;
            }
        }
        ApprovalCountDto approvalCountDto = new ApprovalCountDto();
        approvalCountDto.setToSubmitListCount(submitCount);
        approvalCountDto.setBackListCount(backCount);
        approvalCountDto.setApprovalListCount(approvalCount);
        approvalCountDto.setPassListCount(passCount);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, approvalCountDto), HttpStatus.OK);
    }


    /**
     * 加密方式接受二手车评估接口
     * @param data
     * @return
     */
    public ResponseEntity<Message> receiveEvaluationResult(String data){
        if(data == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"推送内容为空"), HttpStatus.OK);
        }
        UsedCarEvaluationResultDto usedCarEvaluationResultDto = new UsedCarEvaluationResultDto();
        try {
//            String str = TaiMengDES.encryptBase64(data, CommonUtils.encryptKey);
            String result = TaiMengDES.decryptBase64(data, CommonUtils.encryptKey);
            System.out.println("评估结果:" + result + ";" + data);
            usedCarEvaluationResultDto = objectMapper.readValue(result, UsedCarEvaluationResultDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"推送异常"), HttpStatus.OK);
        }
        if(usedCarEvaluationResultDto == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"推送失败"), HttpStatus.OK);
        }
        String version = versionProperties.getVersion();
        Apply apply = applyRepository.findUsedCarEvaluationNum(usedCarEvaluationResultDto.getCarBillId(), version);
        if(apply == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"未找到该单号信息"), HttpStatus.OK);
        }
        ApplyDto totalDto = new ApplyDto();
        String mainType = applyRepository.findMainType(apply.getUniqueMark(), version);
        //评估完成保存评估结果至车辆信息,若产品方案修改为新车不保存
        if("二手车".equals(mainType)){
            CarInfoDto carInfoDto = new CarInfoDto();
            carInfoDto.setSecondDate(usedCarEvaluationResultDto.getDateOfProduction());
            carInfoDto.setSecondOfficialPrice(usedCarEvaluationResultDto.getPrice());
            //车龄单位年转换为月
            if(usedCarEvaluationResultDto.getYears()!=null && !"".equals(usedCarEvaluationResultDto.getYears())){
                carInfoDto.setSecondYears(String.valueOf(Integer.parseInt(usedCarEvaluationResultDto.getYears()) * 12));
            }
            totalDto.setCarInfoDto(carInfoDto);
        }
        totalDto.setUsedCarEvaluationResultDto(usedCarEvaluationResultDto);
        List<Apply> approvalListNew = new ArrayList();
        List<Apply> approvalListOld = applyRepository.findUsedCarEvaluationResult(apply.getUniqueMark(), version);
        List<Apply> carInfo = applyRepository.findCarInfo(apply.getUniqueMark(), version);
        if(!approvalListOld.isEmpty()){
            applyRepository.delete(approvalListOld);
        }
        if(!carInfo.isEmpty() && "二手车".equals(mainType)){
            applyRepository.delete(carInfo);
            //修改车辆信息时删除融资信息
            List<Apply> applyList = applyRepository.findFinanceInfo(apply.getUniqueMark(), version);
            if(!applyList.isEmpty()){
                applyRepository.delete(applyList);
            }
        }
        try {
            approvalListNew =  ApplyItemSerialize.serialize(totalDto, apply.getUniqueMark(), version, apply.getCreateUser());
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }

        if(!approvalListNew.isEmpty()) {
            applyRepository.save(approvalListNew);
        }
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(apply.getUniqueMark(), version);
        if(usedCarEvaluationResultDto.getResultReason() != null && !"".equals(usedCarEvaluationResultDto.getResultReason())){
//            applyInfo.setUsedCarEvaluationStatus(UsedCarEvaluationStatus.REFUSE.code());
        }else {
//            applyInfo.setUsedCarEvaluationStatus(UsedCarEvaluationStatus.PASS.code());
        }
        applyInfoNewRepository.save(applyInfo);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 申请提交
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> applySubmit(String uniqueMark, String userName, String deviceId, String ipAddress, String location, String os, String brand) {
        String version = versionProperties.getVersion();
        List<Approval> approvalList = approvalRepository.findFullInfo(uniqueMark, version);
        List<Apply> applyList = applyRepository.findFullInfo(uniqueMark, version);
        List<Apply> info;
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if (approvalList.isEmpty() || applyList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到该订单申请信息"), HttpStatus.OK);
        }
        info = applyRepository.findCarInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查车辆信息是否填写！"), HttpStatus.OK);
        }
        info = applyRepository.findCarPledgeInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查车辆抵押信息是否填写！"), HttpStatus.OK);
        }
        info = applyRepository.findFinanceInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查融资信息是否填写！"), HttpStatus.OK);
        }
        info = applyRepository.findDetailedInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查客户详细信息是否填写！"), HttpStatus.OK);
        }
        info = applyRepository.findWorkInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查客户职业信息是否填写！"), HttpStatus.OK);
        }
        info = applyRepository.findAddressInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查客户地址信息是否填写！"), HttpStatus.OK);
        }
        info = applyRepository.findApplyFileInfo(uniqueMark, version);
        if(info.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "提交失败，请检查客户征信及其他附件是否上传！"), HttpStatus.OK);
        }
        ApplyFromDto applyFromDto = new ApplyFromDto();
        ApplyDto applyDto = new ApplyDto();
        try {
            applyFromDto = deserialize(ApplyFromDto.class, approvalList);
            applyDto = ApplyItemDeserialize.deserialize(ApplyDto.class, applyList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        //没有共申人信息时，判断年龄是否小于23岁，小于23岁必须填写共申人信息
        List<Apply> jointInfo = applyRepository.findJointInfo(uniqueMark, version);
        if(jointInfo.size() == 0){
            String idCardNum = applyFromDto.getIdCardInfoDto().getIdCardNum();
            if (idCardNum != null && !"".equals(idCardNum.trim())){
                int age = CommonUtils.getAgeByIdCardNum(idCardNum);
                if(age<23){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "客户年龄小于23，必须填写共申人信息！"), HttpStatus.OK);
                }
            }
            //主系统共申人不能为null
            JointInfoDto jointInfoDto = new JointInfoDto();
            jointInfoDto.setJointIdty("");
            jointInfoDto.setJointIdType("");
            jointInfoDto.setJointMobile("");
            jointInfoDto.setJointName("");
            jointInfoDto.setJointRelationship("");
            applyDto.setJointInfoDto(jointInfoDto);
        }
        //查询二手车登记证和行驶证
        List<Object> usedCarImageUrlList = applyRepository.getUsedCarImageUrl(uniqueMark);
        List<UsedCarImagesUrlDto> resultList = new ArrayList();
        Object[] objs;
        UsedCarImagesUrlDto urlDto;
        for(Object object :usedCarImageUrlList){
            objs = (Object[]) object;
            urlDto = new UsedCarImagesUrlDto(objs);
            resultList.add(urlDto);
        }
        String drivingLicense = "";
        String registrationCertificate = "";
        for(UsedCarImagesUrlDto usedCarImagesUrlDto: resultList){
            if("行驶证".equals(usedCarImagesUrlDto.getImageClass())){
                drivingLicense = usedCarImagesUrlDto.getImageUrl();
            }
            if("登记证".equals(usedCarImagesUrlDto.getImageClass())){
                registrationCertificate = usedCarImagesUrlDto.getImageUrl();
            }
        }
        CoreResult codeResult = new CoreResult();
        try {

            //融资信息参数转换(首付、服务费、租赁管理费费率取小数（20% -> 0.2），手续费、保证金费率去百分号(20% -> 20))
            Float paymentRatio = CommonUtils.changePercentToPoint(applyDto.getFinanceInfoDto().getPaymentRatio());
            Float serviceRatio =  CommonUtils.changePercentToPoint(applyDto.getFinanceInfoDto().getServiceRatio());
            Float rentRatio =  CommonUtils.changePercentToPoint(applyDto.getFinanceInfoDto().getRentRatio());
            Float poundageRatio =  CommonUtils.changePercentToNumber(applyDto.getFinanceInfoDto().getPoundageRatio());
            Float bondRatio =  CommonUtils.changePercentToNumber(applyDto.getFinanceInfoDto().getBondRatio());
            //二手车日期格式化，转换为yyyyMMdd
            String secondDate = "";
            if(applyDto.getCarInfoDto().getSecondDate() != null){
                String[] secondDates =applyDto.getCarInfoDto().getSecondDate().split("-");
                for (int i = 0; i < secondDates.length; i++) {
                    secondDate = secondDate + secondDates[i];
                }
            }
            //身份证、驾驶证出生日期转换为yyyy年MM月dd日
            String idCardDateDateOfBirth = CommonUtils.dateConvert(applyFromDto.getIdCardInfoDto().getDateOfBirth());
            String driveLicenceDateOfBirth = CommonUtils.dateConvert(applyFromDto.getDriveLicenceInfoDto().getDateOfBirth());
            String driveLicenceFirstIssueDate = CommonUtils.dateConvert(applyFromDto.getDriveLicenceInfoDto().getFirstIssueDate());
            //提交
            logger.info("submitTime={}", new Date());
//            applyInfo.setSubmitTime(new Date());
            applyInfoNewRepository.save(applyInfo);
            String result = coreSystemInterface.applySubmit(
                    "addinfo",wechatProperties.getSign(), wechatProperties.getTimestamp(),
                    applyInfo.getCreateUser(),
                    applyDto.getProductPlanDto().getSpecificTypeId(),
                    //身份证信息
                    applyFromDto.getIdCardInfoDto().getName(), applyFromDto.getIdCardInfoDto().getSex(),
                    idCardDateDateOfBirth, applyFromDto.getIdCardInfoDto().getNation(),
                    applyFromDto.getIdCardInfoDto().getAddress(), applyFromDto.getIdCardInfoDto().getIdCardNum(),
                    applyFromDto.getIdCardInfoDto().getEffectiveTerm(),
                    //两证一卡移至applyFileDto
                    //applyFromDto.getIdCardInfoDto().getFrontImg(),applyFromDto.getIdCardInfoDto().getBehindImg(),
                    applyDto.getApplyFileDto().getFrontImg(), applyDto.getApplyFileDto().getBehindImg(),
                    //驾驶证信息
                    applyFromDto.getDriveLicenceInfoDto().getName(),applyFromDto.getDriveLicenceInfoDto().getSex(),
                    applyFromDto.getDriveLicenceInfoDto().getNationality(),driveLicenceDateOfBirth,
                    driveLicenceFirstIssueDate,applyFromDto.getDriveLicenceInfoDto().getAddress(),
                    applyFromDto.getDriveLicenceInfoDto().getQuasiDriveType(),applyFromDto.getDriveLicenceInfoDto().getLicenceNum(),
                    applyFromDto.getDriveLicenceInfoDto().getEffectiveTerm(),
                    //两证一卡移至applyFileDto
                    //applyFromDto.getDriveLicenceInfoDto().getLicenceImg(),
                    applyDto.getApplyFileDto().getDriveLicenceImg(),
                    //还款借记卡信息
                    applyFromDto.getBankCardInfoDto().getName(),applyFromDto.getBankCardInfoDto().getBank(),
                    applyFromDto.getBankCardInfoDto().getAccountNum(),
                    //两证一卡移至applyFileDto
                    //applyFromDto.getBankCardInfoDto().getBankImg(),
                    applyDto.getApplyFileDto().getBankImg(),
                    //产品方案信息
                    applyDto.getProductPlanDto().getMainType(), applyDto.getProductPlanDto().getProductTypeId(),
                    applyDto.getProductPlanDto().getRoot(), applyDto.getProductPlanDto().getType(),
                    //车辆信息
                    applyDto.getCarInfoDto().getMfrs(), applyDto.getCarInfoDto().getBrand(),
                    applyDto.getCarInfoDto().getType(),applyDto.getCarInfoDto().getOfficialPrice(),
                    applyDto.getCarInfoDto().getSalePrice(), applyDto.getCarInfoDto().getSecondOfficialPrice(),
                    secondDate, applyDto.getCarInfoDto().getSecondDistance(),
                    applyDto.getCarInfoDto().getSecondYears(),
                    //车辆抵押信息
                    applyDto.getCarPledgeDto().getSaler(), applyDto.getCarPledgeDto().getLicenseAttribute(),
                    applyDto.getCarPledgeDto().getPledgeCity(),  applyDto.getCarPledgeDto().getPledgeCompany(),
                    //融资信息
                    paymentRatio.toString(),
                    applyDto.getFinanceInfoDto().getPaymentAmount(), serviceRatio.toString(),
                    applyDto.getFinanceInfoDto().getServiceAmount(), poundageRatio.toString(),
                    applyDto.getFinanceInfoDto().getPoundageAmount(), bondRatio.toString(),
                    applyDto.getFinanceInfoDto().getBondAmount(), rentRatio.toString(),
                    applyDto.getFinanceInfoDto().getRentAmount(), applyDto.getFinanceInfoDto().getFinanceAmount(),
                    applyDto.getFinanceInfoDto().getFinanceTerm(), applyDto.getFinanceInfoDto().getTotalInvestment(),
                    applyDto.getFinanceInfoDto().getPurchaseTaxFlag(), applyDto.getFinanceInfoDto().getPurchaseTax(),
                    applyDto.getFinanceInfoDto().getInsuranceFlag(), applyDto.getFinanceInfoDto().getCompulsoryInsurance(),
                    applyDto.getFinanceInfoDto().getCommercialInsurance(), applyDto.getFinanceInfoDto().getVehicleTax(),
                    applyDto.getFinanceInfoDto().getGpsFlag(), applyDto.getFinanceInfoDto().getGps(),
                    applyDto.getFinanceInfoDto().getXfwsFlag(), applyDto.getFinanceInfoDto().getXfws(),
                    applyDto.getFinanceInfoDto().getXfwsTerm(), applyDto.getFinanceInfoDto().getUnexpectedGear(),
                    applyDto.getFinanceInfoDto().getUnexpectedFlag(), applyDto.getFinanceInfoDto().getUnexpected(),
                    //客户详细信息
                    applyDto.getDetailedDto().getDiploma(), applyDto.getDetailedDto().getMarriage(),
                    applyDto.getDetailedDto().getHouseOwnership(), applyDto.getDetailedDto().getRealDriverName(),
                    applyDto.getDetailedDto().getRealDriverMobile(), applyDto.getDetailedDto().getRealDrivingProv(),
                    applyDto.getDetailedDto().getRealDrivingCity(), applyDto.getDetailedDto().getAccountType(),
                    //客户职业信息
                    applyDto.getWorkInfoDto().getWorkUnit(), applyDto.getWorkInfoDto().getWorkUnitPost(),
                    applyDto.getWorkInfoDto().getWorkUnitNature(), applyDto.getWorkInfoDto().getWorkingLife(),
                    applyDto.getWorkInfoDto().getWorkUnitTitle(), applyDto.getWorkInfoDto().getWorkUnitIndustry(),
                    applyDto.getWorkInfoDto().getWorkUnitPhone(), applyDto.getWorkInfoDto().getAnnualPayAfterTax(),
                    applyDto.getWorkInfoDto().getWorkUnitProv(), applyDto.getWorkInfoDto().getWorkUnitCity(),
                    applyDto.getWorkInfoDto().getWorkUnitAddress(),
                    //共申人信息
                    applyDto.getJointInfoDto().getJointName(),
                    applyDto.getJointInfoDto().getJointMobile(), applyDto.getJointInfoDto().getJointIdType(),
                    applyDto.getJointInfoDto().getJointIdty(), applyDto.getJointInfoDto().getJointRelationship(),
                    //其他信息
                    applyInfo.getApplyNum(), applyDto.getCarInfoDto().getTypeId(),
                    applyDto.getAddressInfoDto().getActualAddress(),
                    //征信附近移至applyFileDto
                    //applyDto.getDetailedDto().getLetterOfCredit(), applyDto.getDetailedDto().getHandHoldLetterOfCredit(),
                    applyDto.getApplyFileDto().getLetterOfCredit(), applyDto.getApplyFileDto().getHandHoldLetterOfCredit(),
                    drivingLicense, registrationCertificate,
                    //地址信息
                    applyDto.getAddressInfoDto().getLivingState(), applyDto.getAddressInfoDto().getPropertyArea(),
                    applyDto.getAddressInfoDto().getPropertyPledgeState(), applyDto.getAddressInfoDto().getPropertyAcreage(),
                    applyDto.getAddressInfoDto().getHouseholdRegistrationProv(), applyDto.getAddressInfoDto().getHouseholdRegistrationCity(),
                    applyDto.getAddressInfoDto().getActualAddressProv(), applyDto.getAddressInfoDto().getActualAddressCity(),
                    applyDto.getFinanceInfoDto().getPoundageRatioMonthlyFlag(), applyDto.getFinanceInfoDto().getGpsMonthlyFlag(),
                    applyDto.getFinanceInfoDto().getLimitOfLiabilityInsurance(),
                    //申请表
                    applyDto.getApplyFileDto().getApplyForm(),
                    //共申人附件
                    applyDto.getJointFileDto().getJointIdCardFrontImg(), applyDto.getJointFileDto().getJointIdCardFrontImg(),
                    applyDto.getJointFileDto().getJointHandHoldLetterOfCredit(), applyDto.getJointFileDto().getJointLetterOfCredit(),
                    //配偶附件
                    applyDto.getMateFileDto().getMateIdCardFrontImg(), applyDto.getMateFileDto().getMateIdCardBehindImg(),
                    applyDto.getMateFileDto().getMateHandHoldLetterOfCredit(), applyDto.getMateFileDto().getMateLetterOfCredit(),
                    //设备信息
                    deviceId, ipAddress, location, os, brand
            );
            logger.info("applyInfoSubmitResult={}", result);
            codeResult = objectMapper.readValue(result, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        logger.info("applyInfoSubmitCoreResult={}", codeResult);
        //更新申请状态
        if("true".equals(codeResult.getResult().getIsSuccess())){
//            applyInfo.setSubmitTime(new Date());
            applyInfo.setUpdateTime(new Date());
            applyInfo.setHplStatus(ApprovalType.WAIT_APPROVAL.code());
            applyInfoNewRepository.save(applyInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
    }

    /**
     * 获取融资参数
     * @param uniqueMark
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getApplyParam(String uniqueMark, String userName) {
        String version = versionProperties.getVersion();
        List<Apply> applyList = applyRepository.findFullInfo(uniqueMark, version);
        if (applyList.isEmpty()) {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到该订单申请信息"), HttpStatus.OK);
        }
        ApplyDto applyDto = new ApplyDto();
        ApplyParamDto applyParamDto = new ApplyParamDto();
        try {
            applyDto = ApplyItemDeserialize.deserialize(ApplyDto.class, applyList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if(applyDto != null){
            if(applyDto.getProductPlanDto() !=null || applyDto.getProductPlanDto().toString().isEmpty()){
                applyParamDto.setMainType(applyDto.getProductPlanDto().getMainType());
                applyParamDto.setCarType(applyDto.getProductPlanDto().getType());
                applyParamDto.setSpecificTypeId(applyDto.getProductPlanDto().getSpecificTypeId());
                applyParamDto.setSpecificTypeName(applyDto.getProductPlanDto().getSpecificTypeName());
                applyParamDto.setProductTypeName(applyDto.getProductPlanDto().getProductTypeName());
                applyParamDto.setRoot(applyDto.getProductPlanDto().getRoot());
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请先选择产品方案"), HttpStatus.OK);
            }
            if(applyDto.getCarInfoDto() != null){
                applyParamDto.setSalePrice(applyDto.getCarInfoDto().getSalePrice());
                applyParamDto.setDisplacement(applyDto.getCarInfoDto().getDisplacement());
                applyParamDto.setSeatNumber(applyDto.getCarInfoDto().getSeatNumber());
                applyParamDto.setGuidancePrices(applyDto.getCarInfoDto().getOfficialPrice());
                applyParamDto.setUsedCarGuidancePrices(applyDto.getCarInfoDto().getSecondOfficialPrice());
                applyParamDto.setReferenceCarPurchaseTax(applyDto.getCarInfoDto().getCarPurchaseTax());
            }
            if(applyDto.getCarPledgeDto() != null){
                applyParamDto.setLeaseType(applyDto.getCarPledgeDto().getLicenseAttribute());
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, applyParamDto), HttpStatus.OK);
    }


    /**
     * 获取融资申请结果
     * @param uniqueMark
     * @return
     */
    public ResponseEntity<Message> getContractMsg(String uniqueMark) {
        String version = versionProperties.getVersion();
        ApplyInfoNew applyInfo = applyInfoNewRepository.findTopByApprovalUuidAndVersion(uniqueMark, version);
        if(applyInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到订单审核状态！"), HttpStatus.OK);
        }
        String result = contractInterface.query("queryContractState", "", wechatProperties.getTimestamp(),
                wechatProperties.getSign(), applyInfo.getApplyNum(), null, null, null, null, null, null, null, null);
        ContractMsgDto contractMsg = null;
        try {
            contractMsg = objectMapper.readValue(result, ContractMsgDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询审核状态失败！"), HttpStatus.OK);
        }
        if(contractMsg.getContractinfo() == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到订单审核状态！"), HttpStatus.OK);
        }
        ContractStateDto contractInfo =contractMsg.getContractinfo();
        List<Map> list = (List<Map>) contractInfo.getContractstatelist();
        if(list.isEmpty()){
            Map resultMap = new HashMap();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String firstCommitTime = sdf.format(applyInfo.getSubmitTime());
            resultMap.put("BASQZT", "申请待审核");
//            resultMap.put("BASHRQ", firstCommitTime);
            resultMap.put("XTCZRY", applyInfo.getCreateUser());
            resultMap.put("BAGQZT", "");
            resultMap.put("BATHYY", "");
            resultMap.put("RGTHYY", "");
            list.add(resultMap);
            contractMsg.getContractinfo().setContractstatelist(list);
        }
        Collections.sort(list, (arg0, arg1) -> Long.parseLong(arg0.get("BASHRQ").toString()) < Long.parseLong(arg1.get("BASHRQ").toString()) ? 1 : -1);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, contractMsg), HttpStatus.OK);
    }

    /**
     * 根据申请编号获取完善申请所有信息（优质认证用）
     *
     * @param applyNum
     * @return
     */
    public ApplyDto getApprovalInfo(String applyNum) {
        ApplyDto applyDto = new ApplyDto();
        // 版本号
        String version = versionProperties.getVersion();
        List<Apply> applyList = applyRepository.findApplyInfo(applyNum, version);
        if (applyList.isEmpty()) {
            return applyDto;
        }
        try {
            applyDto = ApplyItemDeserialize.deserialize(ApplyDto.class, applyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applyDto;
    }

    private List<String> getUserList1(String userName, SysUser sysUser) {
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


    /**
     * 查询组织架构
     *
     * 由于很多地方需要调用此方法，组织架构一般不会变化，
     * 所以此处使用redis缓存组织架构，优化程序运行速度
     * -- By ChengQiChuan 2018/10/11 11:39
     *
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getUserList(String userName){
        List<String> userNameList = new ArrayList();

        // 从redis中获取用户信息 -- By ChengQiChuan 2018/10/11 11:21
        Object redisObj =  redisRepository.get(RedisProperties.userList_userName+ userName);
        // 如果在redis中没有获取到组织架构就去主系统查 -- By ChengQiChuan 2018/10/11 11:26
        if(redisObj != null){
            userNameList = (List<String>) redisObj;
        }else{
            // 接受主系统的结果集
            OrganizationDto result = new OrganizationDto();
            String coreResult = coreSystemInterface.getOrganizationAndCount("LowerLevelOrganization", userName);
            try {
                result = objectMapper.readValue(coreResult, OrganizationDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询用户下级异常"), HttpStatus.OK);
            }
            if("true".equals(result.getResult().getIsSuccess().toString())){
                List<OrganizationResult> dtos = result.getUserLowers();
                if(dtos == null || dtos.isEmpty()){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询用户下级失败"), HttpStatus.OK);
                }
                for(OrganizationResult organizationResult : dtos){
                    userNameList.add(organizationResult.getUserName());
                }
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "主系统:"+result.getResult().getResultMsg()), HttpStatus.OK);
            }

            //保存用户组织架构5分钟，5分钟超时之后需要重新查
            redisRepository.save(RedisProperties.userList_userName + userName, userNameList,  RedisProperties.userList_userNameTime);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, userNameList), HttpStatus.OK);
    }


    /**
     * 根据经销商代码查询报单报表
     *
     * 由于很多地方需要调用此方法，组织架构一般不会变化，
     * 所以此处使用redis缓存组织架构，优化程序运行速度
     * -- By ChengQiChuan 2018/10/11 11:39
     *
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getUserInfoList(String userName){
        List<OrganizationResult> organizationResultList = new ArrayList();

        // 从redis中获取用户信息 -- By ChengQiChuan 2018/10/11 11:21
        Object redisObj =  redisRepository.get(RedisProperties.userList_userInfo+ userName);
        // 如果在redis中没有获取到组织架构就去主系统查 -- By ChengQiChuan 2018/10/11 11:26
        if(redisObj != null){
            organizationResultList = (List<OrganizationResult>) redisObj;
        }else{
            // 接受主系统的结果集
            OrganizationDto result = new OrganizationDto();
            String coreResult = coreSystemInterface.getOrganizationAndCount("LowerLevelOrganization", userName);
            //主系统返回的数据有问题，这里清理一下
            coreResult = coreResult.replace("username","userName");
            try {
                result = objectMapper.readValue(coreResult, OrganizationDto.class);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询用户下级异常"), HttpStatus.OK);
            }
            if("true".equals(result.getResult().getIsSuccess().toString())){
                List<OrganizationResult> dtos = result.getUserLowers();
                if(dtos == null || dtos.isEmpty()){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询用户下级失败"), HttpStatus.OK);
                }
                for(OrganizationResult organizationResult : dtos){
                    organizationResultList.add(organizationResult);
                }
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, result.getResult().getResultMsg()), HttpStatus.OK);
            }

            //保存用户组织架构5分钟，5分钟超时之后需要重新查
            redisRepository.save(RedisProperties.userList_userInfo + userName, organizationResultList,  RedisProperties.userList_userInfoTime);
        }

        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, organizationResultList), HttpStatus.OK);
    }

}
