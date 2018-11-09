package com.tm.wechat.service.sign;

import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.result.AddSealResult;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.tm.wechat.config.FileUploadProperties;
import com.tm.wechat.dao.sign.ApplyContractRepository;
import com.tm.wechat.dao.sign.GuaranteeContractRepository;
import com.tm.wechat.dao.sign.JointContractRepository;
import com.tm.wechat.domain.ApplyContract;
import com.tm.wechat.domain.GuaranteeContract;
import com.tm.wechat.domain.JointContract;
import com.tm.wechat.dto.contractsign.ApplicantContractSignDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.sign.FileUrlDto;
import com.tm.wechat.service.applyOnline.CoreSystemInterface;
import com.tm.wechat.utils.esign.EviDocHelper;
import com.tm.wechat.utils.esign.FileHelper;
import com.tm.wechat.utils.esign.SignHelper;
import com.tm.wechat.consts.ContractSignStatus;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pengchao on 2018/3/26.
 */
@Service
public class JointContractService {


    @Autowired
    JointContractRepository jointContractRepository;

    @Autowired
    CoreSystemInterface coreSystemInterface;

    @Autowired
    FileUploadProperties fileUploadProperties;

    @Autowired
    GuaranteeContractRepository guaranteeContractRepository;

    @Autowired
    ApplyContractRepository applyContractRepository;

    private static final Logger logger = LoggerFactory.getLogger(JointContractService.class);


    /**
     * 生成并获取共申人合同
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> getOriginContract(String userName, String applyNum){
        FileUrlDto resultDto = new FileUrlDto();
        JointContract jointContract = jointContractRepository.findByApplyNum(applyNum);
        if(jointContract == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查主贷人是否已签约"), HttpStatus.OK);
        }
        String contract = jointContract.getContactPdf();
        String confirmation = jointContract.getRepaymentPlanPdf();
        if(contract == null || confirmation == null || confirmation.isEmpty() || contract.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "请检查主贷人是否已签约"), HttpStatus.OK);
        }
        resultDto.setContactPdfUrl(contract);
        resultDto.setRepaymentPlanPdfUrl(confirmation);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, resultDto), HttpStatus.OK);
    }


    /**
     * 发送短信（签订合同）
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> sendMessage(String applyNum){
        JointContract jointContract = jointContractRepository.findByApplyNum(applyNum);
        String accountId = jointContract.getAccountId();
        if(accountId == null || accountId.isEmpty()){
            accountId = SignHelper.addPersonAccount(jointContract.getIdCard(), jointContract.getName());
            if(accountId == null || accountId.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信发送失败，创建个人账户失败"), HttpStatus.OK);
            }
        }
        boolean flag = SignHelper.sendMessage(accountId, jointContract.getPhoneNum());
        if(!flag){
            logger.error("发送短信失败，手机号", jointContract.getPhoneNum());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "短信发送失败!"), HttpStatus.OK);
        }
        jointContract.setAccountId(accountId);
        jointContractRepository.save(jointContract);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 签署合同
     * @return
     */
    public ResponseEntity<Message> sign(String applyNum, String code){
        ResponseEntity<Message> responseEntity ;
        JointContract personalInfo = jointContractRepository.findByApplyNum(applyNum);
        if(personalInfo == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签约失败，未找到订单!"), HttpStatus.OK);
        }
        //返回客户端结果
        FileUrlDto signFileUrlDto = new FileUrlDto();
        //签署合同
        responseEntity = signPdf(personalInfo,"1", code);
        //签署失败返回错误信息
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setContactPdfUrl(responseEntity.getBody().getData().toString());

        //抵押合同签署完成后签署还款计划表
        responseEntity = signPdf(personalInfo,"2", code);
        if("ERROR".equals(responseEntity.getBody().getStatus())){
            return responseEntity;
        }
        signFileUrlDto.setRepaymentPlanPdfUrl(responseEntity.getBody().getData().toString());
        //全部签署完成后更新主贷人签约状态为完成状态
        personalInfo.setState(ContractSignStatus.SUBMIT.code());
        jointContractRepository.save(personalInfo);
        //签署完成的主合同和还款计划表交给担保申人签约
        GuaranteeContract guaranteeContract = guaranteeContractRepository.findByApplyNum(applyNum);
        //如果有担保人由担保人继续签
        if(guaranteeContract != null){
            guaranteeContract.setContactPdf(signFileUrlDto.getContactPdfUrl());
            guaranteeContract.setRepaymentPlanPdf(signFileUrlDto.getRepaymentPlanPdfUrl());
            guaranteeContractRepository.save(guaranteeContract);
        }
        ApplyContract applyContract = applyContractRepository.findByApplyNum(applyNum);
        if(applyContract != null){
            applyContract.setContactSignedPdf(signFileUrlDto.getContactPdfUrl());
            applyContract.setRepaymentPlanSignedPdf(signFileUrlDto.getRepaymentPlanPdfUrl());
            applyContractRepository.save(applyContract);
        }
        //签署成功后返回所有合同
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, signFileUrlDto), HttpStatus.OK);
    }


    /**
     * 签署合同pdf
     * @return
     */
    public ResponseEntity<Message> signPdf(JointContract personalInfo, String type, String code){
        String timeStamp = String.valueOf(new Date().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        String srcPdfUrl = "";
        String signType = "";
        if("1".equals(type)){
            srcPdfUrl = personalInfo.getContactPdf();
            signType = "_合同_";
        } else if("2".equals(type)){
            srcPdfUrl = personalInfo.getRepaymentPlanPdf();
            signType = "_还款计划表_";
        }
        URL url1 = null;
        byte[] byt = null;
        try {
            url1 = new URL(srcPdfUrl);
            HttpURLConnection conn = (HttpURLConnection)url1.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            DataInputStream in = new DataInputStream(conn.getInputStream());
            File goalFile = new File(fileUploadProperties.getPdfPath()
                    + uploadDate + "/" + personalInfo.getName() + signType  + timeStamp + ".pdf");
            // 获取父级文件夹
            File fileParent = goalFile.getParentFile();
            if(!fileParent.exists()){
                // 不存在就创建文件夹
                fileParent.mkdirs();
            }
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fileUploadProperties.getPdfPath()
                    + uploadDate + "/" + personalInfo.getName() + signType  + timeStamp + ".pdf"));
            byte[] buffer = new byte[10485760];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同模板下载失败" +signType), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同模板下载失败" + signType), HttpStatus.OK);
        }
        // 待签署的PDF文件路径
        String srcPdfFile = fileUploadProperties.getPdfPath() + uploadDate + "/" + personalInfo.getName() + signType  + timeStamp + ".pdf";

        // 最终签署后的PDF文件路径
        String signedFolder = fileUploadProperties.getSignedPdfPath() + uploadDate + "/";
        // 最终签署后PDF文件名称
        String signedFileName = personalInfo.getName() + signType + timeStamp + ".pdf";
        String url = fileUploadProperties.getSignedPdfPath() + uploadDate + "/" + signedFileName;
        File goalFile = new File(url);
        // 获取父级文件夹
        File fileParent = goalFile.getParentFile();
        if(!fileParent.exists()){
            // 不存在就创建
            fileParent.mkdirs();
        }
        String downLoadUrl = fileUploadProperties.getRequestSignedPdfPath() + uploadDate + "/" + signedFileName;
        FileDigestSignResult result = doSignWithTemplateSealByStream(personalInfo, downLoadUrl, srcPdfFile, signedFolder, signedFileName, personalInfo.getPhoneNum(), code, type);
        if(0 == result.getErrCode()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, (Object)downLoadUrl), HttpStatus.OK);
        } else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "签署合同失败！" + signType + result.getMsg()), HttpStatus.OK);
        }
    }



    /***
     * 签署人之间用文件二进制流传递,标准模板印章签署，所用印章SealData为addTemplateSeal接口创建的模板印章返回的SealData
     *
     * @param srcPdfFile
     * @param signedFolder
     * @param signedFileName
     * @param type 0:确认函 1:合同
     */
    private FileDigestSignResult doSignWithTemplateSealByStream(JointContract personalInfo, String downLoadUrl,
                                                                String srcPdfFile, String signedFolder,
                                                                String signedFileName, String mobile, String code, String type) {
        // 创建个人客户账户
        String userPersonAccountId = personalInfo.getAccountId();
        if(userPersonAccountId == null || userPersonAccountId.isEmpty()){
            userPersonAccountId = SignHelper.addPersonAccount(personalInfo.getIdCard(), personalInfo.getName());
        }
        // 创建个人印章
        AddSealResult userPersonSealData = new AddSealResult();
        String sealData = personalInfo.getSealData();
        if(sealData == null || sealData.isEmpty()){
            userPersonSealData = SignHelper.addPersonTemplateSeal(userPersonAccountId);
        } else {
            userPersonSealData.setSealData(sealData);
        }
        // 签署合同
        if("1".equals(type)){
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreamm(srcPdfFile);

            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,签章1
            FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStream(FileHelper.getBytes(srcPdfFile),
                    userPersonAccountId, userPersonSealData.getSealData(), mobile, code, "3");
            if (0 != userPersonSignResult.getErrCode()) {
                return userPersonSignResult;
            }
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档,签章1
            //还款银行卡共申人不需要签字
//            FileDigestSignResult userPersonSignResult1 = SignHelper.userPersonSignByStream(userPersonSignResult.getStream(),
//                    userPersonAccountId, userPersonSealData.getSealData(), mobile, code, "2");
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignServiceId(userPersonSignResult.getSignServiceId());
                personalInfo.setContactSignedPdf(downLoadUrl);
                jointContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult;
            //签署还款计划表
        } else if("2".equals(type)){
            // 贵公司签署，签署方式,以文件流的方式传递pdf文档
//            FileDigestSignResult platformSignResult = SignHelper.platformSignByStreammConfirm(srcPdfFile);
            PosBean posBean =setKeyPosBean("次承租人签字/盖章");
            // 个人客户签署，签署方式：关键字定位,以文件流的方式传递pdf文档
            FileDigestSignResult userPersonSignResult = SignHelper.userPersonSignByStreamConfirm(FileHelper.getBytes(srcPdfFile),
                   userPersonAccountId, userPersonSealData.getSealData(), posBean);
//            FileDigestSignResult userPersonSignResult = new FileDigestSignResult();
            // 所有签署完成,将最终签署后的文件流保存到本地
            if (0 == userPersonSignResult.getErrCode()) {
                // 首次创建个人账号，保存个人账号信息
                personalInfo.setAccountId(userPersonAccountId);
                personalInfo.setSealData(userPersonSealData.getSealData());
                // 保存签署记录
                personalInfo.setSignRepaymentPlanServiceId(userPersonSignResult.getSignServiceId());
                personalInfo.setRepaymentPlanSignedPdf(downLoadUrl);
                jointContractRepository.save(personalInfo);
                SignHelper.saveSignedByStream(userPersonSignResult.getStream(), signedFolder, signedFileName);
            }
            return userPersonSignResult;
        }
        return null;
    }



    /***
     * 关键字定位签署的PosBean
     */
    public static PosBean setKeyPosBean(String key) {
        PosBean posBean = new PosBean();
        // 定位类型，0-坐标定位，1-关键字定位，默认0，若选择关键字定位，签署类型(signType)必须指定为关键字签署才会生效。
        posBean.setPosType(1);
        // 关键字签署时不可空 */
        posBean.setKey(key);
        // 关键字签署时会对整体pdf文档进行搜索，故设置签署页码无效
        // posBean.setPosPage("1");
        // 签署位置X坐标，以关键字所在位置为原点进行偏移，默认值为0，控制横向移动距离，单位为px
        posBean.setPosX(40);
        // 签署位置Y坐标，以关键字所在位置为原点进行偏移，默认值为0，控制纵向移动距离，单位为px
        posBean.setPosY(-35);
        // 印章展现宽度，将以此宽度对印章图片做同比缩放。详细查阅接口文档的15 PosBean描述
        posBean.setWidth(100);
        return posBean;
    }

    /**
     * 合同提交接口
     * @param applyNum
     * @return
     */
    public ResponseEntity<Message> submitContract(String applyNum,String faceImageUrl, String idCardUrl){
        JointContract personalInfo = jointContractRepository.findByApplyNum(applyNum);
        //调用确认函文档保全服务
        ApplicantContractSignDto applicantContractSignDto = new ApplicantContractSignDto(personalInfo);
        boolean confirmationFlag = EviDocHelper.eviDoc(applicantContractSignDto, 1);
        if(!confirmationFlag){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，确认函文档保全错误"), HttpStatus.OK);
        }
        //调用合同文档保全服务
        boolean contractFlag = EviDocHelper.eviDoc(applicantContractSignDto, 3);
        if(!contractFlag){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败，合同文档保全错误"), HttpStatus.OK);
        }
        String result = coreSystemInterface.pactSubmitBy("pactSubmitBy", personalInfo.getApplyNum(), personalInfo.getContactSignedPdf(),
                personalInfo.getRepaymentPlanSignedPdf(),
                personalInfo.getApplyNum(), personalInfo.getContractNum(), personalInfo.getPhoneNum());
        JSONObject jsonObject =  JSONObject.fromObject(result);
        logger.info("调用主系统合同提交接口开始******************************************");
        if(!MessageType.MSG_TYPE_SUCCESS.equals(jsonObject.getString("status"))){
            logger.error("调用主系统合同提交接口失败error", jsonObject.getString("error"));
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "合同提交失败!"), HttpStatus.OK);
        }
        personalInfo.setState(ContractSignStatus.SUBMIT.code());
        personalInfo.setFaceImageUrl(faceImageUrl);
        personalInfo.setIdCardUrl(idCardUrl);
        jointContractRepository.save(personalInfo);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
    }


}
