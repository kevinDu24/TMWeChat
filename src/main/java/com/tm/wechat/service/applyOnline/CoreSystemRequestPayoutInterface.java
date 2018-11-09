package com.tm.wechat.service.applyOnline;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/** 
* @Description: 主系统请款流程接口
* @Author: ChengQiChuan 
* @Date: 2018/9/26 
*/
//@FeignClient(name = "CoreSystemRequestPayoutInterface", url = "http://10.10.11.227:8767/XftmApp/AppCtrl/")
@FeignClient(name = "coreSystemInterface", url = "${request.XftmAppAppCtrlServerUrl}")
public interface CoreSystemRequestPayoutInterface {

    /**
     * 查询首页数量
     * @return
     */
    @RequestMapping(value = "/getFpPendingCount/{xtczdm}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getFpPendingCount(@PathVariable(value = "xtczdm") String xtczdm);


    /**
     * 查询待请款详细信息
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getRequestPaymentInfo/{applyNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getRequestPaymentInfo(@PathVariable(value = "applyNum") String applyNum);

    /**
     * 合同请款 - 查询车辆信息 - 车架号验证
     * @param
     * @return
     */
    @RequestMapping(value = "/getVehicleInfo/{vin}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getVehicleInfo(@PathVariable(value = "vin") String vin);

    /**
     * 获取首保上传信息
     * @param
     * @return
     */
    @RequestMapping(value = "/getFirstInformation/{applyNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getFirstInformation(@PathVariable(value = "applyNum") String applyNum);

    /**
     * 获取保险公司列表
     * @return
     */
    @RequestMapping(value = "/getInsuranceCompanyList", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getInsuranceCompanyList();

    /**
     * 获取商业险保单信息
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getCommercePolicyInfo/{applyNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCommInsurancePolicyInfo(@PathVariable(value = "applyNum") String applyNum);

    /**
     * 提交商业险保单信息
     * @param args
     * @return
     */
    @RequestMapping(value = "/submitCommercePolicyInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitCommInsurancePolicyInfo(@RequestBody String args);

    /**
     * 获取交强险保单信息
     * @param applyNum
     * @return
     */
    @RequestMapping(value = "/getSaliIsPolicyInfo/{applyNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getSaliInsurancePolicyInfo(@PathVariable(value = "applyNum") String applyNum);


    /**
     * 提交交强险保单信息
     * @param args
     * @return
     */
    @RequestMapping(value = "/submitSaliIsPolicyInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitSaliInsurancePolicyInfo(@RequestBody String args);

    /**
     * 提交请款信息
     * @param args   请款信息
     * @return
     */
    @RequestMapping(value = "/submitRequestPaymentInfo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitRequestPaymentInfo(@RequestBody String args);

    /**
     * 提交保单信息
     * @param args     保单信息
     * @return
     */
    @RequestMapping(value = "/submitFirstInformation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitFirstInformation(@RequestBody String args);


}
