package com.tm.wechat.controller.sign;

import com.tm.wechat.dto.approval.FinancePreApplyResultDto;
import com.tm.wechat.dto.approval.FinancePreApplyResultWxDto;
import com.tm.wechat.dto.approval.xw_Bank.XW_FinancePreApplyResultDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.push.PushMessageDto;
import com.tm.wechat.dto.sign.AddressInputInfoDto;
import com.tm.wechat.dto.sign.BankCardSignInfoDto;
import com.tm.wechat.dto.sign.BasicInfoAuthDto;
import com.tm.wechat.dto.webank.BasicInfoDto;
import com.tm.wechat.service.PushMessageService;
import com.tm.wechat.service.addressinput.AddressInputService;
import com.tm.wechat.service.applyOnline.ApplyService;
import com.tm.wechat.service.approval.ApprovalService;
import com.tm.wechat.service.sale.SaleService;
import com.tm.wechat.service.sign.ApplyContractService;
import com.tm.wechat.service.sysUser.LeaseWeChatInterface;
import com.tm.wechat.service.sysUser.SysUserService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Created by pengchao on 2017/6/4.
 */

@RestController
@RequestMapping("/apply")
public class ReceiveFinancePreApplyResultController {
    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private ApplyContractService applyContractService;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private AddressInputService addressInputService;

    @Autowired
    private LeaseWeChatInterface leaseWeChatInterface;


    private static final Logger logger = LoggerFactory.getLogger(ReceiveFinancePreApplyResultController.class);

    /**
     * 接收hpl(天启)预审批结果
     * @param financePreApplyResultDto
     * @return
     */
    @RequestMapping(value = "/receiveFinancePreApplyResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveFinancePreApplyResult(@RequestBody FinancePreApplyResultDto financePreApplyResultDto){
        return  approvalService.receiveFinancePreApplyResult(financePreApplyResultDto);
    }


    /**
     * 接收预审批结果(工行)
     * @param financePreApplyResultDto
     * @return
     */
    @RequestMapping(value = "/receiveICBCFinancePreApplyResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveICBCFinancePreApplyResult(@RequestBody FinancePreApplyResultDto financePreApplyResultDto){
        return  approvalService.receiveICBCFinancePreApplyResult(financePreApplyResultDto);
    }

    /**
     * 接收预审批结果(新网)
     * @param financePreApplyResultDto
     * @return
     */
    @RequestMapping(value = "/receiveXWBankFinancePreApplyResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveXWBankFinancePreApplyResult(@RequestBody XW_FinancePreApplyResultDto financePreApplyResultDto){
        return  approvalService.receiveXWBankFinancePreApplyResult(financePreApplyResultDto);
    }

    /**
     * 接收二手车评估结果
     * @param
     * @return
     */
    @RequestMapping(value = "/receiveEvaluationResult", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveEvaluationResult(String content){
        return  applyService.receiveEvaluationResult(content);
    }


    /**
     * 查询审批结果
     * @param
     * @return
     */
    @RequestMapping(value = "/queryFinancePreApplyResult", method = RequestMethod.GET)
    public ResponseEntity<Message> queryFinancePreApplyResult(){
        return  approvalService.queryFinancePreApplyResult();
    }

    /**
     * 查询审批结果(工行)
     * @param
     * @return
     */
    @RequestMapping(value = "/queryICBCFinancePreApplyResult", method = RequestMethod.GET)
    public ResponseEntity<Message> queryICBCFinancePreApplyResult(){
        return  approvalService.queryICBCFinancePreApplyResult();
    }

    /**
     * 查询微信在线助力融审批拒绝结果
     * @param
     * @return
     */
    @RequestMapping(value = "/queryWxApplyResult", method = RequestMethod.GET)
    public ResponseEntity<Message> queryWxApplyResult(){
        return  approvalService.queryWxApplyResult();
    }

    /**
     * 查询审批结果(新网)
     * @param
     * @return
     */
    @RequestMapping(value = "/queryXWBankFinancePreApplyResult", method = RequestMethod.GET)
    public ResponseEntity<Message> queryXWBankFinancePreApplyResult(){
        return  approvalService.queryXWBankFinancePreApplyResult();
    }

    /**
     * 查询完善申请审批结果
     * @param
     * @return
     */
    @RequestMapping(value = "/queryApplyStates", method = RequestMethod.GET)
    public ResponseEntity<Message> queryApplyStates(){
        return  approvalService.queryApplyStates();
    }

    /**
     * 微信单子通过的接收审批结果
     * @param wxDto
     * @return
     */
    @RequestMapping(value = "/receiveFinancePreApplyResultWx", method = RequestMethod.POST)
    public ResponseEntity<Message> receiveFinancePreApplyResultWx(@RequestBody FinancePreApplyResultWxDto wxDto){
        return approvalService.receiveFinancePreApplyResultWx(wxDto);
    }


    /**
     * 在线助力融拒绝的接收微信推送过来的审批结果
     * @param applyResultDto
     * @return
     */
//    @RequestMapping(value = "/receiveApplyResult", method = RequestMethod.POST)
//    public ResponseEntity<Message> receiveApplyResult(@RequestBody ApplyResultDto applyResultDto){
//        return approvalService.receiveApplyResult(applyResultDto);
//    }


    /**
     * 获取微众银行
     * @return
     */
    @RequestMapping(value = "/getBank", method = RequestMethod.GET)
    public ResponseEntity<Message> getBank(String bankCardNum){
        return approvalService.getBank(bankCardNum);
    }

    /**
     * 短信验证
     * @param phoneNum 手机号
     * @param code 验证码
     * @return
     */
    @RequestMapping(value = "/checkMsgCode", method = RequestMethod.POST)
    public ResponseEntity<Message> checkMsgCode(String phoneNum, String code){
        return sysUserService.checkMessageCode(phoneNum, code);
    }



    /**
     * 获取APP用户基本信息
     * @return
     */
    @RequestMapping(value = "/searchUserBasicInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> searchAppUserBasicInfo(String uniqueMark){
        return approvalService.searchAppUserBasicInfo(uniqueMark);
    }


    /**
     * 提交微众一审  （提交微众预审批）
     * @return
     */
    @RequestMapping(value = "/weBankSubmit", method = RequestMethod.POST)
    public ResponseEntity<Message> weBankSubmit(@RequestBody BasicInfoDto bankCardInfoDto){
        return approvalService.weBankSubmit(bankCardInfoDto);
    }


    /**
     * 根据身份证id获取到已经通过微众预审批记录
     * @param idCard
     * @return
     */
    @RequestMapping(value = "/getwzApplyInfoByIdCard", method = RequestMethod.GET)
    public ResponseEntity<Message> getwzApplyInfoByIdCard(String idCard){
        return approvalService.getwzApplyInfoByIdCard(idCard);
    }

    /**
     * 获取还款借记卡开户行
     * @return
     */
    @RequestMapping(value = "/banks", method = RequestMethod.GET)
    public ResponseEntity<Message> getBanks(){
        return saleService.getBanks("admin");
    }


    /**
     * 电子签约银行卡信息四要素验证（通用）
     * @param
     * @return
     */
    @RequestMapping(value = "/submitBankCardInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitBankCardInfo(@RequestBody BankCardSignInfoDto bankCardInfoDto){
        try {
            return applyContractService.webSubmitBankCardInfo(bankCardInfoDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("银行卡四要素验证信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 四要素验证（通用）
     * @return
     */
    @RequestMapping(value = "/fourFactorVerification", method = RequestMethod.POST)
    public ResponseEntity<Message> fourFactorVerification(@RequestBody BasicInfoAuthDto basicInfoAuthDto){
        try {
            return applyContractService.webFourFactorVerification(basicInfoAuthDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("四要素验证异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 获取用户四要素信息
     * @param
     * @return
     */
    @RequestMapping(value = "/fourFactorVerificationInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> submitBankCardInfo(String applyNum){
        try {
            return applyContractService.fourFactorVerificationInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取用户信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 友盟消息推送
     * @return
     */
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public  ResponseEntity<Message> push(@RequestBody PushMessageDto pushMessageDto){
        try {
            return pushMessageService.push(pushMessageDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("友盟推送异常异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

    /**
     * 友盟消息推送状态查询
     * @return
     */
    @RequestMapping(value = "/querySendStatus", method = RequestMethod.GET)
    public ResponseEntity<Message> querySignInfo(String iosTaskId, String androidTaskId){
        try {
            return pushMessageService.querySendStatus(iosTaskId, androidTaskId);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询友盟推送状态异常异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 提交用户还款卡收货地址信息（web端）
     * @param
     * @return
     */
    @RequestMapping(value = "/submitAddressInputInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> submitAddressInputInfo(@RequestBody AddressInputInfoDto addressInputInfoDto){
        try {
            return addressInputService.submitAddressInputInfo(addressInputInfoDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("提交用户还款卡地址信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



    /**
     * 获取用户收货地址信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getAddressInputInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getAddressInputInfo(String applyNum){
        try {
            return addressInputService.getAddressInputInfo(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取用户收货地址信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getAddressInputInfo(String openId, String lang){
        try {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, leaseWeChatInterface.getUserInfo(openId, lang)), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("获取用户信息异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }



    /**
     * 电子签约退回 （退回重新签约）
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/signBack", method = RequestMethod.GET)
    public ResponseEntity<Message> signBack(@RequestParam(required = true) String applyNum){
        try {
            return applyContractService.signBack(applyNum);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("电子签约退回更新状态异常error", ex);
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }

}
