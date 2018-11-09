package com.tm.wechat.controller.applyOnline;

import com.tm.wechat.dto.apply.*;
import com.tm.wechat.dto.apply.file.ApplyFileDto;
import com.tm.wechat.dto.apply.file.GuaranteeFileDto;
import com.tm.wechat.dto.apply.file.JointFileDto;
import com.tm.wechat.dto.apply.file.MateFileDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.applyOnline.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by pengchao on 2017/7/11.
 */
@RestController
@RequestMapping("/onLineApply")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    /**
     * 添加、修改产品方案
     * @param uniqueMark
     * @param productPlanDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addProductPlan", method = RequestMethod.POST)
    public ResponseEntity<Message> addProductPlanInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                      @RequestBody ProductPlanDto productPlanDto, Principal user){
        return applyService.addProductPlan(productPlanDto, uniqueMark, user.getName());
    }

    /**
     * 查询产品方案信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getProductPlan", method = RequestMethod.GET)
    public ResponseEntity<Message> getProductPlan(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getProductPlan(uniqueMark, user.getName());
    }


    /**
     * 添加车辆信息
     * @param uniqueMark
     * @param carInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addCarInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addCarInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                   @RequestBody CarInfoDto carInfoDto, Principal user){
        return applyService.addCarInfo(carInfoDto, uniqueMark, user.getName());
    }

    /**
     * 添加车辆抵押信息
     * @param uniqueMark
     * @param carPledgeDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addCarPledgeInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addCarPledgeInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                              @RequestBody CarPledgeDto carPledgeDto, Principal user){
        return applyService.addCarPledgeInfo(carPledgeDto, uniqueMark, user.getName());
    }

    /**
     * 添加融资信息
     * @param uniqueMark
     * @param financeInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addFinanceInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addFinanceInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                    @RequestBody FinanceInfoDto financeInfoDto, Principal user){
        return applyService.addFinanceInfo(financeInfoDto, uniqueMark, user.getName());
    }

    /**
     * 添加客户详细信息
     * @param uniqueMark
     * @param detailedDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addDetailedInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addDetailedInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                  @RequestBody DetailedDto detailedDto, Principal user){
        return applyService.addDetailedInfo(detailedDto, uniqueMark, user.getName());
    }


    /**
     * 添加客户职业信息
     * @param uniqueMark
     * @param workInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addWorkInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addWorkInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                   @RequestBody WorkInfoDto workInfoDto, Principal user){
        return applyService.addWorkInfo(workInfoDto, uniqueMark, user.getName());
    }

    /**
     * 添加共申人信息
     * @param uniqueMark
     * @param jointInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addJointInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addJointInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                   @RequestBody JointInfoDto jointInfoDto, Principal user){
        return applyService.addJointInfo(jointInfoDto, uniqueMark, user.getName());
    }


    /**
     * 添加客户地址信息
     * @param uniqueMark
     * @param addAddressInfo
     * @param user
     * @return
     */
    @RequestMapping(value = "/addAddressInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addAddressInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                @RequestBody AddressInfoDto addAddressInfo, Principal user){
        return applyService.addAddressInfo(addAddressInfo, uniqueMark, user.getName());
    }


    /**
     * 添加客户附件信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/addApplyFileInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addApplyFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                  @RequestBody ApplyFileDto applyFileDto, Principal user){
        return applyService.addApplyFileInfo(applyFileDto, uniqueMark, user.getName());
    }


    /**
     * 添加共申人附件信息
     * @param uniqueMark
     * @param jointFileDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addJointFileInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addJointFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                  @RequestBody JointFileDto jointFileDto, Principal user){
        return applyService.addJointFileInfo(jointFileDto, uniqueMark, user.getName());
    }


    /**
     * 添加担保人附件信息
     * @param uniqueMark
     * @param guaranteeFileDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addGuaranteeFileInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addGuaranteeFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                    @RequestBody GuaranteeFileDto guaranteeFileDto, Principal user){
        return applyService.addGuaranteeFileInfo(guaranteeFileDto, uniqueMark, user.getName());
    }


    /**
     * 添加配偶附件信息
     * @param uniqueMark
     * @param mateFileDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addMateFileInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addMateFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                   @RequestBody MateFileDto mateFileDto, Principal user){
        return applyService.addMateFileInfo(mateFileDto, uniqueMark, user.getName());
    }

    /**
     * 获取评估单号
     * @return
     */
    @RequestMapping(value = "/getUsedCarEvaluationNum", method = RequestMethod.GET)
    public ResponseEntity<Message> getUsedCarEvaluationNum(){
        return applyService.getUsedCarEvaluationNum();
    }


    /**
     * 添加二手车评估信息
     * @param uniqueMark
     * @param usedCarEvaluationDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addUsedCarEvaluationDto", method = RequestMethod.POST)
    public ResponseEntity<Message> addUsedCarEvaluationDto(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                                @RequestBody UsedCarEvaluationDto usedCarEvaluationDto, Principal user){
        return applyService.addUsedCarEvaluationDto(usedCarEvaluationDto, uniqueMark, user.getName());
    }



    /**
     * 查询车辆信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getCarInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getCarInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getCarInfo(uniqueMark, user.getName());
    }

    /**
     * 添加车辆及抵押信息
     * @param uniqueMark
     * @param carAndPledgeInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/addCarAndPledgeInfo", method = RequestMethod.POST)
    public ResponseEntity<Message> addCarAndPledgeInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark,
                                              @RequestBody CarAndPledgeInfoDto carAndPledgeInfoDto, Principal user){
        return applyService.addCarAndPledgeInfo(carAndPledgeInfoDto, uniqueMark, user.getName());
    }


    /**
     * 查询车辆及抵押信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getCarAndPledgeInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getCarAndPledgeInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getCarAndPledgeInfo(uniqueMark);
    }

    /**
     * 查询车辆抵押信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getCarPledgeInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getCarPledgeInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getCarPledgeInfo(uniqueMark, user.getName());
    }

    /**
     * 查询融资信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getFinanceInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getFinanceInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getFinanceInfo(uniqueMark, user.getName());
    }

    /**
     * 查询客户详细信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getDetailedInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getDetailedInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getDetailedInfo(uniqueMark, user.getName());
    }

    /**
     * 查询客户职业信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getWorkInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getWorkInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getWorkInfo(uniqueMark, user.getName());
    }

    /**
     * 查询共申人信息
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getJointInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getJointInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getJointInfo(uniqueMark, user.getName());
    }


    /**
     * 查询客户地址信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getAddressInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getAddressInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        return applyService.getAddressInfo(uniqueMark);
    }


    /**
     * 查询客户附件信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getApplyFileInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getApplyFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        return applyService.getApplyFileInfo(uniqueMark);
    }

    /**
     * 查询共申人附件信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getJointFileInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getJointFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        return applyService.getJointFileInfo(uniqueMark);
    }


    /**
     * 查询担保人附件信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getGuaranteeFileInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getGuaranteeFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        return applyService.getGuaranteeFileInfo(uniqueMark);
    }


    /**
     * 查询配偶附件信息
     * @param uniqueMark
     * @return
     */
    @RequestMapping(value = "/getMateFileInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getMateFileInfo(@RequestParam(value = "uniqueMark", required = true) String uniqueMark){
        return applyService.getMateFileInfo(uniqueMark);
    }


    /**
     * 查询二手车评估结果
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUsedCarEvaluationResult", method = RequestMethod.GET)
    public ResponseEntity<Message> getUsedCarEvaluationResult(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getUsedCarEvaluationResult(uniqueMark, user.getName());
    }

    /**
     * 查询创建未提交列表
     * @param user
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getLocalInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getLocalInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        return applyService.getLocalInfo(user.getName(), condition);
    }


    /**
     * 查询退回待修改列表
     * @param user:获取当前登录人信息
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getBackInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getBackInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        return applyService.getBackInfo(user.getName(), condition);
    }

    /**
     * 查询申请中列表
     * @param user:获取当前登录人信息
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getSubmitInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getSubmitInfo(Principal user, @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        return applyService.getSubmitInfo(user.getName(), condition);
    }

    /**
     * 查询审批完成列表
     * @param user:获取当前登录人信息
     * @param condition 检索条件(姓名or申请编号)
     * @return
     */
    @RequestMapping(value = "/getAchieveInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> getAchieveInfo(@RequestParam(value = "searchType", required = true) String searchType, Principal user,
                                                  @RequestParam(value = "condition", required = false, defaultValue="") String condition){
        return applyService.getAchieveInfo(searchType, user.getName(), condition);
    }

    /**
     * 新建申请不同状态审批数量
     * @param user:获取当前登录人信息
     * @return
     */
    @RequestMapping(value = "/getNewApprovalCount", method = RequestMethod.GET)
    public ResponseEntity<Message> getNewApprovalCount(Principal user){
        return applyService.getNewApprovalCount(user.getName());
    }

    /**
     * 融资申请结果查询
     * url:222.73.56.22/onLineApply/36157040/log
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/{uniqueMark}/log", method = RequestMethod.GET)
    public ResponseEntity<Message> getContract(@PathVariable String uniqueMark){
        return applyService.getContractMsg(uniqueMark);
    }

    /**
     * 融资申请提交
     * @param uniqueMark 唯一标识
     * @param user
     * @return
     */
    @RequestMapping(value = "/applySubmit", method = RequestMethod.GET)
    public ResponseEntity<Message> applySubmit(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, String deviceId, String ipAddress, String location, String os, String brand,Principal user){
        return applyService.applySubmit(uniqueMark, user.getName(), deviceId, ipAddress, location, os, brand);
    }

    /**
     * 获取融资参数
     * @param uniqueMark
     * @param user
     * @return
     */
    @RequestMapping(value = "/getApplyParam", method = RequestMethod.GET)
    public ResponseEntity<Message> getApplyParam(@RequestParam(value = "uniqueMark", required = true) String uniqueMark, Principal user){
        return applyService.getApplyParam(uniqueMark, user.getName());
    }

}
