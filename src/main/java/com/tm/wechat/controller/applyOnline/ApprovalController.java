package com.tm.wechat.controller.applyOnline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dto.approval.*;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.approval.ApprovalService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 在线申请
 * Created by zcHu on 17/5/10.
 */
@RestController
@RequestMapping("approval")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class ApprovalController {
    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ObjectMapper objectMapper;


    private static final Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    /**
     * 获取当前用户可新建的产品类型
     * @return
     */
    @RequestMapping(value = "/getOriginType", method = RequestMethod.GET)
    public ResponseEntity<Message> getOriginType(Principal user){
        return approvalService.getOriginType(user.getName());
    }

    /**
     * 生成申请唯一标识
     * @return
     */
    @RequestMapping(value = "/getUniqueMark", method = RequestMethod.GET)
    public ResponseEntity<Message> getUniqueMark(){
        return approvalService.getUniqueMark();
    }

//    /**
//     * 新增产品来源信息
//     * @param user:获取当前登录人信息
//     * @return
//     */
//    @RequestMapping(value = "/addProductSourceInfo", method = RequestMethod.POST)
//    public ResponseEntity<Message> addProductSourceInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
//                                                   @RequestBody ProductSourceDto productSourceDto, Principal user){
//        return approvalService.addProductSourceInfo(productSourceDto, uniqueMark, user.getName());
//    }

    /**
     * 新增待提交审批信息（身份证信息）
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/addIdentityInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addIdentityInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                   @RequestBody IdCardInfoDto idCardInfoDto, Principal user){
        return approvalService.addIdentityInfo(idCardInfoDto, uniqueMark, user.getName());
    }


    /**
     * 查询产品来源
     * @param
     * @return
     */
    @RequestMapping(value = "/getProductSource", method = RequestMethod.GET)
    public ResponseEntity<Message> getProductSource(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        try {
            return approvalService.getProductSource(uniqueMark);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询产品来源异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询预审批申请中状态
     * @param
     * @return
     */
    @RequestMapping(value = "/getApprovalSubmitState", method = RequestMethod.GET)
    public ResponseEntity<Message> getApprovalSubmitState(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        try {
            return approvalService.getApprovalSubmitState(uniqueMark);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询预审批申请中状态异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询微众预审结果 (查询微众预审批结果)
     * @param
     * @return
     */
    @RequestMapping(value = "/queryApplyState", method = RequestMethod.GET)
    public ResponseEntity<Message> queryApplyState(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        try {
            return approvalService.queryApplyState(uniqueMark);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询微众预审结果异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

// 问了君杰，驾驶证信息在很久以前有，现在已经不需要驾驶证信息了 -- By ChengQiChuan 2018/10/19 12:58
//    /**
//     * 新增驾驶证信息
//     * @param user:获取当前登录人信息
//     * @return
//     */
//    @RequestMapping(value = "/addDriveInfo", method = RequestMethod.POST)
//    public ResponseEntity<Message> addDriveInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
//            @RequestBody DriveLicenceInfoDto driveLicenceInfoDto, Principal user){
//        return approvalService.addDriveInfo(driveLicenceInfoDto, uniqueMark, user.getName());
//    }

    /**
     * 新增银行卡信息
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/addBankInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addBankInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                               String msgCode, @RequestBody BankCardInfoDto bankCardInfoDto, Principal user){
        return approvalService.addBankInfo(bankCardInfoDto, msgCode, uniqueMark, user.getName());
    }


    /**
     * 新增银行卡信息（在线助力融）
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/addWeBankInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addWeBankInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                               String msgCode, @RequestBody BankCardInfoDto bankCardInfoDto, Principal user){
        try {
            return approvalService.addWeBankInfo(bankCardInfoDto, msgCode, uniqueMark, user.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("新增银行卡信息（在线助力融）异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 获取银行列表 (微盟贷在线助力融)
     * @return
     */
    @RequestMapping(value = "/getBankList", method = RequestMethod.GET)
    public ResponseEntity<Message> getBankList(){
        try {
            return approvalService.getBankList();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取在线助力融银行列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 获取还款借记卡开户行
     * @param user 当前登录的用户
     * @param originType 产品类型
     * @return
     */
    @RequestMapping(value = "/banks", method = RequestMethod.GET)
    public ResponseEntity<Message> getBanks(Principal user,String originType){
        try {
            return approvalService.getBanks(user.getName(),originType);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取银行列表异常"), HttpStatus.OK);
        }
    }

    /**
     * 新增其他信息
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/addOtherInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addOtherInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                @RequestBody OtherInfoDto otherInfoDto, Principal user){
        return approvalService.addOtherInfo(otherInfoDto, uniqueMark, user.getName());
    }


    /**
     * 新增婚姻状况
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/addMaritalStatus", method = RequestMethod.POST)
    public ResponseEntity<Message> addMaritalStatus(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                @RequestBody MaritalStatusDto maritalStatusDto, Principal user){
        return approvalService.addMaritalStatus(maritalStatusDto, uniqueMark, user.getName());
    }

    /**
     * 新增配偶信息
     * @param uniqueMark
     * @param mateInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addMateInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addMateInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                @RequestBody MateInfoDto mateInfoDto, Principal user){
        return approvalService.addMateInfo(mateInfoDto, uniqueMark, user.getName());
    }

    /**
     * 新增紧急联系人信息
     * @param uniqueMark
     * @param addContactInfo
     * @param user
     * @return
     */
    @RequestMapping(value = "/addContactInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addContactInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                @RequestBody ContactInfoDto addContactInfo, Principal user){
        return approvalService.addContactInfo(addContactInfo, uniqueMark, user.getName());
    }



    /**
     * 新增预审附件信息
     * @param uniqueMark
     * @param approvalImagesDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addApprovalImages", method = RequestMethod.POST)
    public ResponseEntity<Message> addApprovalImages(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                  @RequestBody ApprovalImagesDto approvalImagesDto, Principal user){
        return approvalService.addApprovalImages(approvalImagesDto, uniqueMark, user.getName());
    }


    /**
     * 查询待提交列表   （searchKey: 查询创建未提交列表）
     * @param user:获取当前登录人信息
     * @param originType 1：app，2：微信，0或者是空：全部
     * @return
     */
    @RequestMapping(value = "/getLocalInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getLocalInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition, String originType,
                                                @RequestParam(value = "size", required = false ,defaultValue = "200") Integer size, @RequestParam(value = "page", required = false ,defaultValue = "1")Integer page){
        return approvalService.getLocalInfo(user.getName(), condition, originType, size, page);
    }

    /**
     * 查询退回待修改列表
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/getBackInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getBackInfo(Principal user){
        return approvalService.getBackInfo(user.getName());
    }


    /**
     * 查询退回待修改列表（工行、新网附件退回）
     * 之前只有工行，现在还包括新网的  -- By ChengQiChuan 2018/10/24 9:59
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/getICBCAttachmentBackInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getAttachmentBackInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition,
                                                             @RequestParam(value = "size", required = false ,defaultValue = "200") Integer size, @RequestParam(value = "page", required = false ,defaultValue = "1")Integer page){
        try {
            return approvalService.getAttachmentBackInfo(user.getName(), condition, size, page);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询 退回待修改列表异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 查询申请中列表
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/getSubmitInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getSubmitInfo(Principal user,
                                             @RequestParam(value = "size", required = false ,defaultValue = "200") Integer size, @RequestParam(value = "page", required = false ,defaultValue = "1")Integer page){
        return approvalService.getSubmitInfo(user.getName(), size, page);
    }

    /**
     * 查询审批完成列表
     * @param user:
     * @param condition 检索条件(姓名or申请编号)
     * @param originType 1：app，2：微信，0或者是空：全部
     * @return
     */
    @RequestMapping(value = "/getAchieveInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getAchieveInfo(@RequestParam(value = "searchType", required = true) String searchType,
                                                  Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition, String originType,
                                                  @RequestParam(value = "size", required = false ,defaultValue = "200") Integer size, @RequestParam(value = "page", required = false ,defaultValue = "1")Integer page){
        return approvalService.getAchieveInfo(searchType, user.getName(), condition, originType, size, page);
    }


    /**
     * 根据唯一标识查询待提交首页信息（其他信息）
     * @param user:获取当前登录人信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getOtherInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getOtherInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getOtherInfo(uniqueMark, user.getName());
    }

    /**
     * 根据唯一标识查询待提交首页信息（婚姻状况）
     * @param user:获取当前登录人信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getMaritalStatus", method = RequestMethod.GET)
    public ResponseEntity<Message> getMaritalStatus(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getMaritalStatus(uniqueMark, user.getName());
    }



    /**
     * 查询配偶信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getMateInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getMateInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getMateInfo(uniqueMark, user.getName());
    }

    /**
     * 查询紧急联系人信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getContactInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getContactInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getContactInfo(uniqueMark, user.getName());
    }



    /**
     * 根据唯一标识查询身份证信息
     * @param user:获取当前登录人信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getIdCardInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getIdCardInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getIdCardInfo(uniqueMark, user.getName());
    }

    /**
     * 根据唯一标识查询驾驶证信息
     * @param user:获取当前登录人信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getDriveInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getDriveInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getDriveInfo(uniqueMark, user.getName());
    }

    /**
     * 根据唯一标识查询银行卡信息
     * @param user:获取当前登录人信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getBankInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getBankInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getBankInfo(uniqueMark, user.getName());
    }


    /**
     * 根据唯一标识查询预审批附件信息
     * @param user:获取当前登录人信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getApprovalImages", method = RequestMethod.GET)
    public ResponseEntity<Message> getApprovalImages(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.getApprovalImages(uniqueMark);
    }

    /**
     * 自动预审批提交
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/autoFinancingPreApplySubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> autoFinancingPreApplySubmit(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                               @RequestParam(value = "longitude", required = false) Double longitude,
                                                               @RequestParam(value = "latitude", required = false) Double latitude, Principal user){
        try {
            return approvalService.autoFinancingPreApplySubmit(uniqueMark, user.getName(), longitude, latitude);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("自动预审批提交异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

//      现在只有自动预审批提交，没有人工预审批提交 -- By ChengQiChuan 2018/10/16 13:14
//    /**
//     * 人工预审批申请提交
//     * @param uniqueMark
//     * @param user:获取当前登录人信息
//     * @return
//     */
//    @RequestMapping(value = "/submit", method = RequestMethod.POST)
//    public ResponseEntity<Message> submit(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, @RequestBody SubmitProveDto submitProveDto, Principal user){
//        return approvalService.submit(uniqueMark, user.getName(), submitProveDto);
//    }

    /**
     * 查询不同状态审批数量
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/getApprovalCount", method = RequestMethod.GET)
    public ResponseEntity<Message> getApprovalCount(Principal user){
        return approvalService.getApprovalCount(user.getName());
    }

    /**
     * 修改认证状态
     *
     * @param uniqueMark
     * @param authItem
     * @return
     */
//    @RequestMapping(value = "/alterCertificationStatus", method = RequestMethod.GET)
//    public ResponseEntity<Message> alterCertificationStatus(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, @RequestParam(value = "authItem", required = true)String authItem){
//        return  approvalService.alterCertificationStatus(uniqueMark,authItem);
//    }

    /**
     * 在线申请二期客户信息页面初期化
     *
     * @param applyNum:申请编号
     * @return
     */
//    @RequestMapping(value = "/initCustomerInfo", method = RequestMethod.GET)
//    public ResponseEntity<Message> initCustomerInfo(@RequestParam(value = "applyNum", required = true) String applyNum){
//        return  approvalService.initCustomerInfo(applyNum);
//    }

    /**
     * 查询优质认证不同状态的数量
     * @param user:
     * @return
     */
//    @RequestMapping(value = "/getAuthCount", method = RequestMethod.GET)
//    public ResponseEntity<Message> getAuthInfo(Principal user){
//        return approvalService.getAuthCount(user.getName());
//    }

    /**
     * 查询优质认证列表
     * @param user:
     * @return
     */
//    @RequestMapping(value = "/getAuthInfo", method = RequestMethod.GET)
//    public ResponseEntity<Message> getAuthInfo(@RequestParam(value = "type", required = true) String type, Principal user){
//        return approvalService.getAuthInfo(type, user.getName());
//    }

    /**
     * 保存优质认证参数
     * @param user:
     * @return
     */
//    @RequestMapping(value = "/getAuthParam", method = RequestMethod.GET)
//    public ResponseEntity<Message> getAuthParam(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
//                                                @RequestParam(value = "sequenceNo", required = true) String sequenceNo,
//                                                @RequestParam(value = "token", required = true) String token,
//                                                @RequestParam(value = "type", required = true) String type,
//                                                Principal user){
//        return approvalService.getAuthParam(uniqueMark, sequenceNo, token, type, user.getName());
//    }


    /**
     * 获取申请列表
     * @param user
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getApplyList", method = RequestMethod.GET)
    public ResponseEntity<Message> getApplyList(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        return approvalService.getApplyList(user.getName(), condition);
    }



    /**
     * 根据审批状态获取申请列表
     * @param user
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getApplyListByStatus", method = RequestMethod.GET)
    public ResponseEntity<Message> getApplyListByStatus(Principal user, String status, @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        return approvalService.getApplyListByStatus(user.getName(), status, condition);
    }


    /**
     * 查询自动预审批各个信息完善状态
     * @param uniqueMark: 唯一标识
     * @return
     */
    @RequestMapping(value = "/autoApplyState", method = RequestMethod.GET)
    public ResponseEntity<Message> autoApplyState(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return approvalService.autoApplyState(uniqueMark);
    }

// 本来打算在首页增加搜索功能的，不过暂时没有时间，有其他更重要的任务，这个功能暂放吧！
// 上面有一个 获取申请列表 getApplyList 貌似也是查询，有时间可以仔细查看查看  -- 2018/10/10 11:54 By ChengQiChuan
//    /**
//     * 查询在线申请列表
//     * @param user   经销商账户
//     * @param condition 检索条件(姓名or申请编号)
////     * @param productType  产品类型 1：HPL，2：微盟贷，3：工盟贷，0或者是空：全部
////     * @param status  状态
//     * @return
//     */
//    @RequestMapping(value = "/searchApprovalInfo", method = RequestMethod.GET)
//    public ResponseEntity<Message> searchApprovalInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition){
//        //return approvalService.getAchieveInfo(searchType, user.getName(), condition, originType);
//        return approvalService.searchApprovalInfo( user.getName(), condition);
//    }
}

