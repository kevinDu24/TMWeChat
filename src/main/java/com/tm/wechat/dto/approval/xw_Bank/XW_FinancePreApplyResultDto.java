package com.tm.wechat.dto.approval.xw_Bank;

import com.tm.wechat.consts.ApprovalType;
import lombok.Data;

/**
 * @program: TMWeChat
 * @description: 新网预审批主系统返回结果
 * @author: ChengQC
 * @create: 2018-10-22 09:52
 **/
@Data
public class XW_FinancePreApplyResultDto {

    //uniqueMark
    private String applyId;
    //申请编号applyNumber
    private String basqbh;
    //申请状态 0:拒绝 1:通过 2:处理中 3:异常
    private String status;
    //备注
    private String reason;
    //经销商编号(用户名)
    private String userid;
    //用户照片比对结果  0:通过 1:拒绝 2.建议拒绝 3:人工审核中 4:超时
    private String userPhotores;
    //用户照片比对结果描述
    private String PhotoMsg;


    /**
     * 转换code为太盟宝使用的
     * @param xw_financePreApplyResultDto
     * @return
     */
    public static String changeCode(XW_FinancePreApplyResultDto xw_financePreApplyResultDto){
        String code = "100";
        String xwStatus = xw_financePreApplyResultDto.getStatus();
        String wxuserPhotoresStatus = xw_financePreApplyResultDto.getUserPhotores();
        //拒绝
        if("0".equals(xwStatus)){
            code = ApprovalType.REFUSE_NOREASON.code();
        //通过
        }else if("1".equals(xwStatus) && "0".equals(wxuserPhotoresStatus)){
            code = ApprovalType.PASS.code();
        //退回（目前只有大头照附件比对失败）
        }else if("1".equals(xwStatus) && "1".equals(wxuserPhotoresStatus)){
            code = ApprovalType.BACK.code();
        }
        return code;
    }

    /**
     * 获取到退回原因
     * @param xw_financePreApplyResultDto
     * @return
     */
    public static String getProductReason(XW_FinancePreApplyResultDto xw_financePreApplyResultDto){
        String productReason = "";
        if(!xw_financePreApplyResultDto.getReason().isEmpty()){
            productReason += "信息："+xw_financePreApplyResultDto.getReason();
        }
        if(!xw_financePreApplyResultDto.getReason().isEmpty() && !xw_financePreApplyResultDto.getPhotoMsg().isEmpty()){
            productReason += ",";
        }
        if(!xw_financePreApplyResultDto.getPhotoMsg().isEmpty()){
            productReason += "大头照："+xw_financePreApplyResultDto.getPhotoMsg();
        }
        return productReason;
    }
}