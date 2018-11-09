package com.tm.wechat.service;

import com.google.common.collect.Maps;
import com.tm.wechat.config.FileUploadProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengchao on 2016/3/3.
 */
@Service
public class CfsService {

    @Autowired
    private FileUploadProperties fileUploadProperties;

    /**
     * 文件上传转发器
     * @param type
     * @param file
     * @return
     */
    public ResponseEntity<Message> uploadFile(String name,String type, MultipartFile file){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        if(type.equals("creditImg")){
            return saveFile(name,fileUploadProperties.getCreditImgPath(), file, fileUploadProperties.getRequestCreditImgPath());
        }else if(type.equals("feedbackSound")){
            return saveFile(name,fileUploadProperties.getFeedbackSoundPath(), file, fileUploadProperties.getRequestfeedbackSoundPath());
        }else if(type.equals("headImg")){
            return saveFile(name,fileUploadProperties.getHeadImgPath(), file, fileUploadProperties.getRequestHeadImgPath());
        }else if(type.equals("annexesImg")){
            return saveFile(name,fileUploadProperties.getAnnexesImgPath(), file, fileUploadProperties.getRequestAnnexesImgPath());
        }else if(type.equals("cmdImg")){
            return saveFile(name,fileUploadProperties.getCmdImgPath() + uploadDate + "/", file, fileUploadProperties.getRequestCmdImgPath() +  type + "/" + uploadDate + "/" );
        }else if ("idCard".equals(type)){//预审批身份证照片
            return saveFile(name,fileUploadProperties.getApprovalIdCardPath() + uploadDate + "/", file, fileUploadProperties.getRequestApprovalIdCardPath()+ uploadDate + "/");
        }else if ("drivingLicence".equals(type)){//预审批驾驶证照片
            return saveFile(name,fileUploadProperties.getApprovalDrivePath() + uploadDate + "/", file, fileUploadProperties.getRequestApprovalDrivePath()+ uploadDate + "/");
        }else if ("bankCard".equals(type)){//预审批银行卡照片
            return saveFile(name,fileUploadProperties.getApprovalBankPath() + uploadDate + "/", file, fileUploadProperties.getRequestApprovalBankPath()+ uploadDate + "/");
        }else if ("verdict".equals(type)){//预审批法院判决书
            return saveFile(name,fileUploadProperties.getApprovalVerdictPath() + uploadDate + "/", file, fileUploadProperties.getRequestApprovalVerdictPath()+ uploadDate + "/");
        }else if("usedCarEvaluation".equals(type)){
            return saveFile(name,fileUploadProperties.getApplyUsedCarEvaluationPath() + uploadDate + "/", file, fileUploadProperties.getRequestApplyUsedCarEvaluationPath() + type + "/" + uploadDate + "/" );
        }else if ("letterOfCredit".equals(type)){//征信授权书
            return saveFile(name,fileUploadProperties.getLetterOfCreditPath() + uploadDate + "/", file, fileUploadProperties.getRequestLetterOfCreditPath()+ uploadDate + "/");
        }else if("handHoldLetterOfCredit".equals(type)){//手持授权书
            return saveFile(name,fileUploadProperties.getHandHoldLetterOfCreditPath() + uploadDate + "/", file, fileUploadProperties.getRequestHandHoldLetterOfCreditPath() + uploadDate + "/" );
        }else if(type.equals("applyForm")){//申请表
            return saveFile(name,fileUploadProperties.getApplyFormPath() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() +  type + "/" + uploadDate + "/" );
        }else if ("jointFile".equals(type)){//共申人附件
            return saveFile(name,fileUploadProperties.getJointFilePath() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() +  type + "/" + uploadDate + "/");
        }else if ("guaranteeFile".equals(type)){//担保人附件
            return saveFile(name,fileUploadProperties.getGuaranteeFile() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() +  type + "/" + uploadDate + "/");
        }else if ("mateFile".equals(type)){//配偶附件
            return saveFile(name,fileUploadProperties.getMateFilePath() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() +  type + "/" + uploadDate + "/");
        } else if("signIdCard".equals(type)){//身份证照片
            return saveFile(name,fileUploadProperties.getIdCardPath() + uploadDate + "/", file, fileUploadProperties.getRequestIdCardPath() + uploadDate + "/");
        } else if("faceImage".equals(type)){//人脸照片
            return saveFile(name,fileUploadProperties.getFaceImagePath() + uploadDate + "/", file, fileUploadProperties.getRequestFaceImagePath() + uploadDate + "/");
        }else if("requestPaymentFile".equals(type)){//请款文件信息
            return saveFile(name,fileUploadProperties.getRequestPaymentFilePath() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() + type + "/" +  uploadDate + "/");
        } else if("insurancePolicyPath".equals(type)){//保单文件
            return saveFile(name,fileUploadProperties.getInsurancePolicyPath() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() + type + "/"  + uploadDate + "/");
        } else if("onlineCommunication".equals(type)){//保单文件
            return saveFile(name,fileUploadProperties.getOnlineCommunicationPath() + uploadDate + "/", file, fileUploadProperties.getRequestFilePath() + type + "/"  + uploadDate + "/");
        } else if("userPhoto".equals(type)){//用户大头照
            return saveFile(name,fileUploadProperties.getUserPhotoPath() + uploadDate + "/", file, fileUploadProperties.getRequestUserPhotoPath() + type + "/"  + uploadDate + "/");
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未指定上传类型"), HttpStatus.OK);
    }

    public ResponseEntity<Message> multifile(String type, MultipartFile[] files){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String uploadDate = sdf.format(new Date());
        if(type.equals("cmdOtherImg")){
            return saveFiles(fileUploadProperties.getCmdOtherImgPath() + uploadDate + "/", files, fileUploadProperties.getRequestCmdOtherImgPath() + type + "/" + uploadDate + "/");
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未指定上传类型"), HttpStatus.OK);
    }

    /**
     * 保存文件a4f7a5ce99ad
     * @param savePath
     * @param file
     * @param serverPath
     * @return
     */
    public ResponseEntity<Message> saveFile(String name,String savePath, MultipartFile file, String serverPath){
        Message message = null;
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + Utils.getFileSuffix(file.getOriginalFilename());
            //如果页面上传过来的名字不为空，则拼接上时间戳成为新的文件名字
            if(!name.isEmpty()){
                fileName = name+"_"+System.currentTimeMillis() + Utils.getFileSuffix(file.getOriginalFilename());
            }
            try {
                FileUtils.writeByteArrayToFile(new File(savePath + fileName), file.getBytes());
                Map map = Maps.newHashMap();
                map.put("url", serverPath + fileName);
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, map), HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件上传失败"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
    }

    /**
     * 保存多文件
     * @param savePath
     * @param files
     * @param serverPath
     * @return
     */
    public ResponseEntity<Message> saveFiles(String savePath, MultipartFile[] files, String serverPath){
        List<Map> fileList = new ArrayList<Map>();
        if(files.length == 0 ){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
        }
        for (int i=0; i<files.length; i++ ){
            if (files[i] != null && !files[i].isEmpty()) {
                String fileName = UUID.randomUUID().toString() + Utils.getFileSuffix(files[i].getOriginalFilename());
                try {
                    Map map = Maps.newHashMap();
                    System.out.println(serverPath + fileName);
                    System.out.println(savePath + fileName);
                    FileUtils.writeByteArrayToFile(new File(savePath + fileName), files[i].getBytes());
                    map.put("url", serverPath + fileName);
                    fileList.add(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件上传失败"), HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "文件为空,上传失败"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, fileList), HttpStatus.OK);
    }


    /**
     * 附件下载
     * @param response
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public byte[] downloadFile(HttpServletResponse response, String fileName, String loadDate, String type) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String path = null;
        if("cmdImg".equals(type)){
            path = fileUploadProperties.getCmdImgPath() +  loadDate + "/" + fileName;
        } else if("cmdOtherImg".equals(type)){
            path = fileUploadProperties.getCmdOtherImgPath() + loadDate + "/" + fileName;
        } else if("usedCarEvaluation".equals(type)) {
            path = fileUploadProperties.getApplyUsedCarEvaluationPath() + loadDate + "/" + fileName;
        }else if("applyForm".equals(type)){
            path = fileUploadProperties.getApplyFormPath() + loadDate + "/" + fileName;
        } else if("jointFile".equals(type)){
            path = fileUploadProperties.getJointFilePath() + loadDate + "/" + fileName;
        } else if("guaranteeFile".equals(type)){
            path = fileUploadProperties.getGuaranteeFile() + loadDate + "/" + fileName;
        } else if("mateFile".equals(type)){
            path = fileUploadProperties.getMateFilePath() + loadDate + "/" + fileName;
        } else if("signedPdf".equals(type)){
            path = fileUploadProperties.getSignedPdfPath() +  loadDate + "/" + fileName;
        } else if("signIdCard".equals(type)){
            path = fileUploadProperties.getIdCardPath() +  loadDate + "/" + fileName;
        } else if("faceImage".equals(type)){
            path = fileUploadProperties.getFaceImagePath() +  loadDate + "/" + fileName;
        } else if("drivingLicence".equals(type)){
            path = fileUploadProperties.getApprovalDrivePath() + loadDate + "/" + fileName;
        } else if("bankCard".equals(type)){
            path = fileUploadProperties.getApprovalBankPath() +  loadDate + "/" + fileName;
        } else if("idCard".equals(type)){
            path = fileUploadProperties.getApprovalIdCardPath() +  loadDate + "/" + fileName;
        } else if("letterOfCredit".equals(type)) {
            path = fileUploadProperties.getLetterOfCreditPath() + loadDate + "/" + fileName;
        }  else if("handHoldLetterOfCredit".equals(type)) {
            path = fileUploadProperties.getHandHoldLetterOfCreditPath() + loadDate + "/" + fileName;
        } else if("requestPaymentFile".equals(type)) {
            path = fileUploadProperties.getRequestPaymentFilePath() + loadDate + "/" + fileName;
        } else if("insurancePolicyPath".equals(type)) {
            path = fileUploadProperties.getInsurancePolicyPath() + loadDate + "/" + fileName;
        } else if("onlineCommunication".equals(type)) {
            path = fileUploadProperties.getOnlineCommunicationPath() + loadDate + "/" + fileName;
        } else if("userPhoto".equals(type)) {   //用户大头照
            path = fileUploadProperties.getUserPhotoPath() + loadDate + "/" + fileName;
        } else {
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        File file = new File(path);
        if(!file.exists()){
            String errorMessage = "抱歉. 你访问的文件不存在！";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return null;
        }

        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        return null;
    }

}
