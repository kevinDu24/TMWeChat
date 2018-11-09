package com.tm.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by wisely on 2015/10/23.
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileUploadProperties {
    private String filePath;
    private String requestFrontPath;
    private String newsPath;

    private String creditImgPath;
    private String requestCreditImgPath;

    private String feedbackSoundPath;
    private String requestfeedbackSoundPath;

    private String headImgPath;
    private String requestHeadImgPath;

    private String deviceImgPath;

    private String annexesImgPath;
    private String requestAnnexesImgPath;

    private String cmdImgPath;
    private String requestCmdImgPath;

    private String cmdOtherImgPath;
    private String requestCmdOtherImgPath;

    private String approvalIdCardPath;
    private String requestApprovalIdCardPath;

    private String approvalDrivePath;
    private String requestApprovalDrivePath;

    private String approvalBankPath;
    private String requestApprovalBankPath;

    private String approvalVerdictPath;
    private String requestApprovalVerdictPath;

    //二手车评估照片
    private String applyUsedCarEvaluationPath;
    private String requestApplyUsedCarEvaluationPath;

    //征信授权书
    private String letterOfCreditPath;
    private String requestLetterOfCreditPath;

    //手持征信授权书
    private String handHoldLetterOfCreditPath;
    private String requestHandHoldLetterOfCreditPath;

    //申请表
    private String applyFormPath;

    //共申人附件
    private String jointFilePath;

    //配偶附件
    private String mateFilePath;

    //担保人附件
    private String guaranteeFile;

    //请款文件信息
    private String requestPaymentFilePath;

    //请款保单信息
    private String insurancePolicyPath;

    //附件下载请求路径
    private String requestFilePath;

    //在线沟通附件
    private String onlineCommunicationPath;


    private String idCardPath;
    private String requestIdCardPath;
    private String faceImagePath;
    private String requestFaceImagePath;
    private String pdfPath;
    private String requestPdfPath;
    private String signedPdfPath;
    private String requestSignedPdfPath;

    //用户大头照
    private String userPhotoPath;
    private String requestUserPhotoPath;

}
