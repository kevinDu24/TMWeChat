package com.tm.wechat.service.sysUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dao.SysUserFunctionRepository;
import com.tm.wechat.dao.SysUserInfoRepository;
import com.tm.wechat.dao.SystemFunctionRepository;
import com.tm.wechat.dao.UnableProductRepository;
import com.tm.wechat.domain.SysUserFunction;
import com.tm.wechat.domain.SysUserInfo;
import com.tm.wechat.domain.SystemFunction;
import com.tm.wechat.domain.UnableProduct;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.result.CoreResult;
import com.tm.wechat.dto.sysUser.*;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.service.common.BaseService;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.consts.ContractSignStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengchao on 2018/4/23.
 */
@Service
public class SysUserFunctionService extends BaseService {

    @Autowired
    SystemFunctionRepository systemFunctionRepository;

    @Autowired
    SysUserFunctionRepository sysUserFunctionRepository;

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UnableProductRepository unableProductRepository;

    @Autowired
    SysUserInfoRepository sysUserInfoRepository;


    public ResponseEntity<Message> getRole(String userCode) {
        List<SystemFunction> systemFunctions = systemFunctionRepository.findAll();
        List<SysUserFunctionDto> sysUserFunctionDtoList = new ArrayList<>();
        SysUserFunctionResultDto sysUserFunctionResultDto = new SysUserFunctionResultDto();
        List<SysUserFunction> sysUserFunctions = sysUserFunctionRepository.findByXtczdm(userCode);
        SysUserFunctionDto sysUserFunctionDto;
        for(SystemFunction systemFunction : systemFunctions){
            sysUserFunctionDto = new SysUserFunctionDto();
            sysUserFunctionDto.setDescription(systemFunction.getDescription());
            sysUserFunctionDto.setFunctionId(systemFunction.getId());
            if(sysUserFunctions != null && !sysUserFunctions.isEmpty()){
                for(SysUserFunction sysUserFunction : sysUserFunctions){
                    if(systemFunction.getId().equals(sysUserFunction.getFunctionId())){
                        sysUserFunctionDto.setState(sysUserFunction.getState());
                    }
                }
//                SysUserFunction sysUserFunction = sysUserFunctionRepository.findByXtczdmAndFunctionId(userCode, systemFunction.getId());
//                if(sysUserFunction != null){
//                    sysUserFunctionDto.setState(sysUserFunction.getState());
//                }
            }else {
                sysUserFunctionDto.setState(ContractSignStatus.SUBMIT.code());
            }
            sysUserFunctionDtoList.add(sysUserFunctionDto);
        }
        sysUserFunctionResultDto.setSysUserFunctionDtoList(sysUserFunctionDtoList);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, sysUserFunctionResultDto), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> updateRole(String userCode,SysUserFunctionResultDto sysUserFunctionResultDto) {
        if(userCode == null || userCode.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "修改角色失败"), HttpStatus.OK);
        }
        List<SysUserFunctionDto> sysUserFunctionDtoList = sysUserFunctionResultDto.getSysUserFunctionDtoList();
        SysUserFunction result;
        List<SysUserFunction> resultList = new ArrayList<>();
        for(SysUserFunctionDto sysUserFunctionDto : sysUserFunctionDtoList){
            result = new SysUserFunction();
            result.setFunctionId(sysUserFunctionDto.getFunctionId());
            result.setState(sysUserFunctionDto.getState());
            result.setXtczdm(userCode);
            resultList.add(result);
        }
        if(!resultList.isEmpty()){
            sysUserFunctionRepository.deleteByXtczdm(userCode);
            sysUserFunctionRepository.save(resultList);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 添加车300评估权限接口
     * @param userList
     * @return
     */
    @Transactional
    public ResponseEntity<Message> addUsedCarAnalysis1Power(List<String> userList, String userCode) {
        if(userList == null || userList.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "添加功能权限失败"), HttpStatus.OK);
        }
        List<SysUserFunction> resultList = new ArrayList<>();
        for(String userName : userList){
            SysUserFunction sysUserFunction = new SysUserFunction();
            sysUserFunction.setCreateUser(userCode);
            sysUserFunction.setXtczdm(userName);
            sysUserFunction.setState("1");
            sysUserFunction.setFunctionId(12l);
            sysUserFunction.setUpdateUser(userCode);
            resultList.add(sysUserFunction);
        }
        if(!resultList.isEmpty()){
            sysUserFunctionRepository.save(resultList);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 查询车300评估权限接口
     * @param
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getUsedCarAnalysis1Power(String userName) {
        if(CommonUtils.isNull(userName)){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "用户名不能为空"), HttpStatus.OK);
        }
        String usedCarAnalysis1Power = "0";
        List<SysUserFunction> sysUserFunctions = sysUserFunctionRepository.findByFunctionId(12l);
        if(sysUserFunctions == null || sysUserFunctions.isEmpty()){
            usedCarAnalysis1Power = "1";
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, usedCarAnalysis1Power), HttpStatus.OK);
        }
        SysUserFunction sysUserFunction = sysUserFunctionRepository.findTop1ByXtczdmAndFunctionIdOrderByUpdateTimeDesc(userName,12l);
        if(sysUserFunction == null || CommonUtils.isNull(sysUserFunction.getState())){
            usedCarAnalysis1Power = "0";
        }else {
            usedCarAnalysis1Power = sysUserFunction.getState();
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, null, usedCarAnalysis1Power), HttpStatus.OK);
    }


    /**
     * 查询用户可选产品列表
     * @param userName 当前登录人员
     * @param userCode 要查询可选产品的操作代码
     * @return
     */
    public ResponseEntity<Message> enableUsers(String userName, String userCode){
        if(userCode == null || userCode.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "查询用户权限列表失败"), HttpStatus.OK);
        }
        UserAuthInfoDto userAuthInfoDto = new UserAuthInfoDto();
        //查询登录权限
//        SysUserInfo sysUserInfo = sysUserInfoRepository.findByXtczdm(userCode);
        SysUserInfo sysUserInfo = getSysUserInfo(userCode);
        if(sysUserInfo == null || sysUserInfo.getLoginAuth() == null || sysUserInfo.getLoginAuth().isEmpty()){
            userAuthInfoDto.setLoginAuth(ContractSignStatus.SUBMIT.code());
        }else {
            userAuthInfoDto.setLoginAuth(sysUserInfo.getLoginAuth());
        }
        //查询功能权限
        List<SystemFunction> systemFunctions = systemFunctionRepository.findAll();
        List<SysUserFunctionDto> sysUserFunctionDtoList = new ArrayList<>();
        SysUserFunctionResultDto sysUserFunctionResultDto = new SysUserFunctionResultDto();
        List<SysUserFunction> sysUserFunctions = sysUserFunctionRepository.findByXtczdm(userCode);
        SysUserFunctionDto sysUserFunctionDto;
        for(SystemFunction systemFunction : systemFunctions){
            sysUserFunctionDto = new SysUserFunctionDto();
            sysUserFunctionDto.setDescription(systemFunction.getDescription());
            sysUserFunctionDto.setFunctionId(systemFunction.getId());
            if(sysUserFunctions != null && !sysUserFunctions.isEmpty()){
                for(SysUserFunction sysUserFunction : sysUserFunctions){
                    if(systemFunction.getId().equals(sysUserFunction.getFunctionId())){
                        sysUserFunctionDto.setState(sysUserFunction.getState());
                    }
                }
            }else {
                sysUserFunctionDto.setState(ContractSignStatus.SUBMIT.code());
            }
            sysUserFunctionDtoList.add(sysUserFunctionDto);
        }
        sysUserFunctionResultDto.setSysUserFunctionDtoList(sysUserFunctionDtoList);
        userAuthInfoDto.setSysUserFunctionResultDto(sysUserFunctionResultDto);
        //查询产品权限
        List<UserProductInfoDto> resultList = new ArrayList();
        CoreResult codeResult = new CoreResult();
        UserProductInfoResultDto userProductInfoResultDto = new UserProductInfoResultDto();
        try {
            //获取主系统用户所有可选产品
            String coreResult = coreSystemInterface.enableUsers("EnableUsers", userName);
            codeResult = objectMapper.readValue(coreResult, CoreResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        if("true".equals(codeResult.getResult().getIsSuccess())){
            resultList = codeResult.getProductInfoLylist();
            List<UnableProduct> unableProductList = unableProductRepository.findByXtczdm(userCode);
            for(UserProductInfoDto userProductInfoDto : resultList){
                if(unableProductList != null && !unableProductList.isEmpty()){
                    for(UnableProduct unableProduct : unableProductList){
                        //有禁止选择记录，则返回禁用状态
                        if(unableProduct.getProductId().equals(userProductInfoDto.getProductId())){
                            userProductInfoDto.setState(unableProduct.getState());
                        }else {
                            userProductInfoDto.setState(ContractSignStatus.SUBMIT.code());
                        }
                    }
                }else {
                    //默认所有产品可选
                    userProductInfoDto.setState(ContractSignStatus.SUBMIT.code());
                }
            }
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, codeResult.getResult().getResultMsg()), HttpStatus.OK);
        }
        userProductInfoResultDto.setUserProductInfoDtoList(resultList);
        userAuthInfoDto.setUserProductInfoResultDto(userProductInfoResultDto);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, userAuthInfoDto), HttpStatus.OK);
    }

    /**
     * 更新用户产品
     * @param userCode  要更新的用户名
     * @param
     * @return
     */
    @Transactional
    public ResponseEntity<Message> updateUserProduct(String userCode,UserAuthInfoDto userAuthInfoDto) {
        if(userCode == null || userCode.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "用户名不能为空"), HttpStatus.OK);
        }
        //更新用户登录权限
//        SysUserInfo oldSysUserInfo = sysUserInfoRepository.findByXtczdm(userCode);
        SysUserInfo oldSysUserInfo = getSysUserInfo(userCode);
        if(oldSysUserInfo == null){
            SysUserInfo sysUserInfo = new SysUserInfo();
            sysUserInfo.setLoginAuth(userAuthInfoDto.getLoginAuth());
            sysUserInfo.setXtczdm(userCode);
            sysUserInfoRepository.save(sysUserInfo);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }
        oldSysUserInfo.setLoginAuth(userAuthInfoDto.getLoginAuth());
        sysUserInfoRepository.save(oldSysUserInfo);
        //更新用户功能权限
        List<SysUserFunctionDto> sysUserFunctionDtoList = userAuthInfoDto.getSysUserFunctionResultDto().getSysUserFunctionDtoList();
        SysUserFunction result;
        List<SysUserFunction> sysUserFunctionList = new ArrayList<>();
        for(SysUserFunctionDto sysUserFunctionDto : sysUserFunctionDtoList){
            result = new SysUserFunction();
            result.setFunctionId(sysUserFunctionDto.getFunctionId());
            result.setState(sysUserFunctionDto.getState());
            result.setXtczdm(userCode);
            sysUserFunctionList.add(result);
        }
        if(!sysUserFunctionList.isEmpty()){
            sysUserFunctionRepository.deleteByXtczdm(userCode);
            sysUserFunctionRepository.save(sysUserFunctionList);
        }
        //更新用户产品权限
        List<UserProductInfoDto> userProductInfoDtoList = userAuthInfoDto.getUserProductInfoResultDto().getUserProductInfoDtoList();
        UnableProduct unableProduct;
        List<UnableProduct> resultList = new ArrayList<>();
        for(UserProductInfoDto userProductInfoDto : userProductInfoDtoList){
            unableProduct = new UnableProduct();
            if(ContractSignStatus.NEW.code().equals(userProductInfoDto.getState())){
                unableProduct.setProductId(userProductInfoDto.getProductId());
                unableProduct.setProductName(userProductInfoDto.getProductName());
                unableProduct.setState(userProductInfoDto.getState());
                unableProduct.setXtczdm(userCode);
                resultList.add(unableProduct);
            }
        }
        if(!resultList.isEmpty()){
            unableProductRepository.deleteByXtczdm(userCode);
            unableProductRepository.save(resultList);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }
}
