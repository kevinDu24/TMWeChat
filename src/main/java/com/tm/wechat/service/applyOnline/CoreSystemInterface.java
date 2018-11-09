package com.tm.wechat.service.applyOnline;

import com.tm.wechat.dto.approval.QueryICBCFinancePreApplyResultDto;
import com.tm.wechat.dto.approval.icbc.ApprovalSubmitDto;
import com.tm.wechat.dto.approval.xw_Bank.XW_ApprovalSubmitDto;
import com.tm.wechat.dto.approval.xw_Bank.XW_QueryFinancePreApplyResultDto;
import com.tm.wechat.dto.communication.PostOnlineCommunicationDto;
import com.tm.wechat.dto.gpsconvention.GpsSubmitInfoDto;
import com.tm.wechat.dto.requestpayment.*;
import com.tm.wechat.dto.sign.*;
import com.tm.wechat.dto.sms.SendShortMessageDto;
import com.tm.wechat.dto.sysUser.AuthSubmitDto;
import com.tm.wechat.dto.sysUser.UserAddDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/28.
 */
//@FeignClient(name = "coreSystemInterface", url = "http://116.236.234.246:18080/XFTM_ZL/")
//@FeignClient(name = "coreSystemInterface", url = "http://116.228.224.59:8766/TMZL")
@FeignClient(name = "coreSystemInterface", url = "${request.coreApplyServerUrl}")
public interface CoreSystemInterface {


    //上级开设下级
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String superiorOpeningAccount(@RequestParam(value = ".url", required = false) String url,
                                  @RequestBody UserAddDto userAddDto);


    //认证信息提交
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String userActivationSubmit(@RequestParam(value = ".url", required = false) String url,
                                @RequestBody AuthSubmitDto authSubmitDto);


    //实名认证接口提交
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String identityVerificationJK(@RequestParam(value = ".url", required = false) String url,
                                @RequestBody AuthSubmitDto authSubmitDto);


    //查询待签约列表
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryListToSign(@RequestParam(value = ".url", required = true) String url,
                           @RequestParam(value = "userName", required = true) String userName,
                           @RequestParam(value = "beginTime", required = true) String beginTime,
                           @RequestParam(value = "endTime", required = true) String endTime,
                           @RequestParam(value = "condition", required = true) String condition);


    //查询可选产品列表
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String enableUsers(@RequestParam(value = ".url", required = true) String url,
                           @RequestParam(value = "xtczdm", required = true) String xtczdm);


    //获取车辆信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getVehicleComplement(@RequestParam(value = ".url", required = false) String url,
                                 @RequestParam(value = "applyNum", required = false) String applyNum);

    //获取银行卡信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getBankCardInformationComplementJK(@RequestParam(value = ".url", required = false) String url,
                                       @RequestParam(value = "applyNum", required = false) String applyNum);

    //获取还款卡收货地址信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAddressInputInfo(@RequestParam(value = ".url", required = false) String url,
                                              @RequestParam(value = "applyNum", required = false) String applyNum);


    //补充车辆信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String vehicleComplement(@RequestParam(value = ".url", required = true) String url,
                             @RequestBody CarSignInfoDto carSignInfoDto);

    //补充银行卡信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String bankCardInformationComplementJK(@RequestParam(value = ".url", required = true) String url,
                                           @RequestBody BankCardSignInfoDto bankCardSignInfoDto);

    //提交还款卡收货地址
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitAddressInputInfo(@RequestParam(value = ".url", required = true) String url,
                                           @RequestBody AddressInputInfoDto addressInputInfoDto);


    //四要素验证
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String fourFactorVerificationJK(@RequestParam(value = ".url", required = true) String url,
                                           @RequestBody BasicInfoAuthDto basicInfoAuthDto);

    //查询四要素验证结果
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String userVerificationResult(@RequestParam(value = ".url", required = true) String url,
                                    @RequestBody BankCardSignInfoDto bankCardSignInfoDto);


    //查询订单主、共、担保人信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String orderInformation(@RequestParam(value = ".url", required = false) String url,
                            @RequestParam(value = "applyNum", required = false) String applyNum);

    //生成合同接口
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String createContract(@RequestParam(value = ".url", required = false) String url,
                                 @RequestParam(value = "applyNum", required = false) String applyNum);

    //获取合同接口
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String acquisitionOfContract(@RequestParam(value = ".url", required = false) String url,
                                 @RequestParam(value = "applyNum", required = false) String applyNum);


    //打印合同接口
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getContractPrinting(@RequestParam(value = ".url", required = false) String url,
                                 @RequestParam(value = "applyNum", required = false) String applyNum);

    //获取GPS_SN号
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String gpsActivationConfirmationJK(@RequestParam(value = ".url", required = false) String url,
                                 @RequestParam(value = "applyNum", required = false) String applyNum);


    //合同提交接口
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String signAontractTo(@RequestParam(value = ".url", required = true) String url,
                          @RequestBody SignFileUrlDto signFileUrlDto);

    //查询待请款列表列表
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String forPaymentList(@RequestParam(value = ".url", required = true) String url,
                          @RequestParam(value = "userName", required = true) String userName,
                          @RequestParam(value = "beginTime", required = true) String beginTime,
                          @RequestParam(value = "endTime", required = true) String endTime,
                          @RequestParam(value = "condition", required = true) String condition);



    //查询待请款列表列表
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getPleaseDocument(@RequestParam(value = ".url", required = true) String url,
                             @RequestParam(value = "applyNum", required = true) String applyNum);

    //补充请款文件信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String requestFileSubmitJK(@RequestParam(value = ".url", required = true) String url,
                               @RequestBody FileInfoDto fileInfoDto);


    //补充请款保单信息
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String policyInformationSubmissionJK(@RequestParam(value = ".url", required = true) String url,
                                         @RequestBody InsurancePolicyDto carSignInfoDto);


    /**
     * 获取订单列表
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getOrderList(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "userName") String userName,
                        @RequestParam(value = "condition") String condition,
                        @RequestParam(value = "beginTime") String beginTime,
                        @RequestParam(value = "endTime") String endTime);

    /**
     * 获取组织架构
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getOrganizationAndCount(@RequestParam(value = ".url", required = false) String url,
                                   @RequestParam(value = "userName") String userName);

    /**
     * 获取组织架构及订单数量
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getOrganizationAndCount(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "userName") String userName,
                        @RequestParam(value = "beginTime") String beginTime,
                        @RequestParam(value = "endTime") String endTime);

    /**
     * 订单绑定
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String orderBind(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "sourceUser") String sourceUser,
                        @RequestParam(value = "targetUser") String targetUser,
                        @RequestParam(value = "applyNum") String applyNum);

    /**
     * 获取GPS邀约列表
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getGpsList(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "userName") String userName,
                        @RequestParam(value = "condition") String condition,
                        @RequestParam(value = "beginTime") String beginTime,
                        @RequestParam(value = "endTime") String endTime);

    /**
     * 获取省、市、县列表
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAreaList(@RequestParam(value = ".url", required = false) String url,
                      @RequestParam(value = "type") String type,
                      @RequestParam(value = "id") String id);

    /**
     * 获取GPS安装品牌、方式
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getGpsInfo(@RequestParam(value = ".url", required = false) String url,
                       @RequestParam(value = "applyNum") String applyNum,
                       @RequestParam(value = "installProvince") String installProvince,
                       @RequestParam(value = "installCity") String installCity,
                       @RequestParam(value = "username") String name);

    /**
     * GPS邀约信息提交
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitGpsInfo(@RequestParam(value = ".url", required = false) String url,@RequestBody GpsSubmitInfoDto submitInfo);

    //查询在线沟通列表
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getOnlineCommunicationJK(@RequestParam(value = ".url", required = true) String url,
                                    @RequestParam(value = "condition", required = true) String condition,
                                    @RequestParam(value = "loginUser", required = true) String loginUser);

    //查询在线沟通消息历史
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String historyMessageQuery(@RequestParam(value = ".url", required = true) String url,
                                    @RequestParam(value = "applyNum", required = true) String applyNum);

    //查询在线沟通附件历史
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String historyAttachmentQuery(@RequestParam(value = ".url", required = true) String url,
                               @RequestParam(value = "applyNum", required = true) String applyNum);

    /**
     * 在线沟通提交接口
     * @param url
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String postOnlineCommunication(@RequestParam(value = ".url", required = false) String url,@RequestBody PostOnlineCommunicationDto postOnlineCommunicationDto);


    /**
     * 工行征信接口
     * @param url
     * @param approvalSubmitDto
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String approvalSubmit(@RequestParam(value = ".url", required = false) String url,
                          @RequestBody ApprovalSubmitDto approvalSubmitDto);


    /**
     * 工行征信状态查询接口
     * @param url
     * @param queryFinancePreApplyResultDto
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String approvalStateQuery(@RequestParam(value = ".url", required = false) String url,
                              @RequestBody QueryICBCFinancePreApplyResultDto queryFinancePreApplyResultDto);


    //发送短信接口
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String SendShortMessage(@RequestParam(value = ".url", required = false) String url,
                                @RequestBody SendShortMessageDto sendShortMessageDto);


    /**
     * 查询是否需要视频面签
     * @param url
     * @param
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String iSvisaInterview(@RequestParam(value = ".url", required = false) String url,
                           @RequestParam(value = "applyNum") String applyNum);


    /**
     * 工行面签报告提交
     * @param url
     * @param
     * @return
     */
    @RequestMapping(value = "/HPLServletRequest", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String submitVisaInterview(@RequestParam(value = ".url", required = false) String url,
                           @RequestBody VideoSignAccountDto videoSignAccountDto);



    @RequestMapping(value = "/ADDSQServlet", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getProductDetail(@RequestParam(value = ".url") String url,
                           @RequestParam(value = "BAKHDM") String BAKHDM,
                           @RequestParam(value = "BACPID") String BACPID,
                           @RequestParam(value = "BACPMC") String BACPMC,
                           @RequestParam(value = "BASQLX") String BASQLX,
                           @RequestParam(value = "BAZLSX") String BAZLSX,
                           @RequestParam(value = "BACPCX") String BACPCX,
                           @RequestParam(value = "BACLLX") String BACLLX,
                           @RequestParam(value = "BACLXSJ") String BACLXSJ,
                           @RequestParam(value = "sign") String sign,
                           @RequestParam(value = "timestamp") String timestamp);


    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String pactSubmitBy(@RequestParam(value = ".url", required = false) String url,
                        @RequestParam(value = "lssqbh", required = false) String lssqbh,
                        @RequestParam(value = "contractUrl", required = false) String contractUrl,
                        @RequestParam(value = "confirmationUrl", required = false) String confirmationUrl,
                        @RequestParam(value = "basqbh", required = false) String basqbh,
                        @RequestParam(value = "baddbh", required = false) String baddbh,
                        @RequestParam(value = "xtczry", required = false) String xtczry);

    @RequestMapping(value = "/ADDSQServlet", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getLeaseCalculation(@RequestParam(value = ".url") String url,
                            @RequestParam(value = "BAKHDM") String BAKHDM,
                            @RequestParam(value = "BACPID") String BACPID,
                            @RequestParam(value = "BACPMC") String BACPMC,
                            @RequestParam(value = "BARZQX") String BARZQX,
                            @RequestParam(value = "BASQLX") String BASQLX,
                            @RequestParam(value = "BAZLSX") String BAZLSX,
                            @RequestParam(value = "BACPCX") String BACPCX,
                            @RequestParam(value = "BACLLX") String BACLLX,
                               @RequestParam(value = "BASFBL") String BASFBL,
                               @RequestParam(value = "BAWFBL") String BAWFBL,
                               @RequestParam(value = "BAFWFL") String BAFWFL,
                               @RequestParam(value = "BASXFL") String BASXFL,
                               @RequestParam(value = "BAZLGLFL") String BAZLGLFL,
                               @RequestParam(value = "BABZJL") String BABZJL,
                               @RequestParam(value = "A2") String A2,
                               @RequestParam(value = "A3") String A3,
                               @RequestParam(value = "A4") String A4,
                               @RequestParam(value = "A6") String A6,
                               @RequestParam(value = "A7") String A7,
                               @RequestParam(value = "A8") String A8,
                               @RequestParam(value = "A9") String A9,
                               @RequestParam(value = "A15") String A15,
                               @RequestParam(value = "A14") String A14,
                               @RequestParam(value = "A16") String A16,
                               @RequestParam(value = "A18") String A18,
                               @RequestParam(value = "A1") String A1,
                               @RequestParam(value = "sign") String sign,
                               @RequestParam(value = "timestamp") String timestamp,
                               @RequestParam(value = "SFRXFWS") String SFRXFWS,
                               @RequestParam(value = "XFWSQX") String XFWSQX);

    @RequestMapping(value = "/ADDSQServlet", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String applySubmit(@RequestParam(value = ".url") String url,
                       @RequestParam(value = "sign") String sign,
                       @RequestParam(value = "timestamp") String timestamp,
                       @RequestParam(value = "BAKHDM") String BAKHDM,
                       @RequestParam(value = "BACPID") String BACPID,
                       //身份证信息
                       @RequestParam(value = "BASQXM") String BASQXM,
                       @RequestParam(value = "BAXB") String BAXB,
                       @RequestParam(value = "BACSRQ") String BACSRQ,
                       @RequestParam(value = "BAMZ") String BAMZ,
                       @RequestParam(value = "BAHJDZ") String BAHJDZ,
                       @RequestParam(value = "BAZJHM") String BAZJHM,
                       @RequestParam(value = "BAYXQX") String BAYXQX,
                       @RequestParam(value = "SFZZM_URL") String SFZZM_URL,
                       @RequestParam(value = "SFZFM_URL") String SFZFM_URL,
                       //驾驶证信息
                       @RequestParam(value = "BAJSZXM") String BAJSZXM,
                       @RequestParam(value = "JSZXM") String JSZXM,
                       @RequestParam(value = "JSZGJ") String JSZGJ,
                       @RequestParam(value = "JSZCSRQ") String JSZCSRQ,
                       @RequestParam(value = "JSZCCLZRQ") String JSZCCLZRQ,
                       @RequestParam(value = "JSZDZ") String JSZDZ,
                       @RequestParam(value = "JSZLX") String JSZLX,
                       @RequestParam(value = "JSZDAH") String JSZDAH,
                       @RequestParam(value = "JSZYXQX") String JSZYXQX,
                       @RequestParam(value = "JSZ_URL") String JSZ_URL,
                       //借记卡信息
                       @RequestParam(value = "JJKXM") String JJKXM,
                       @RequestParam(value = "JJKKHH") String JJKKHH,
                       @RequestParam(value = "JJKKH") String JJKKH,
                       @RequestParam(value = "JJK_URL") String JJK_URL,
                       //产品方案
                       @RequestParam(value = "BACLLX") String BACLLX,
                       @RequestParam(value = "BADLID") String BADLID,
                       @RequestParam(value = "BALY") String BALY,
                       @RequestParam(value = "BACPCX") String BACPCX,
                       //车辆信息
                       @RequestParam(value = "BAZZS") String BAZZS,
                       @RequestParam(value = "BACLPP") String BACLPP,
                       @RequestParam(value = "BACLCX") String BACLCX,
                       @RequestParam(value = "BAZDJG") String BAZDJG,
                       @RequestParam(value = "BAXSJG") String BAXSJG,
                       @RequestParam(value = "ESCZDJ") String ESCZDJ,
                       @RequestParam(value = "ESCCCRQ") String ESCCCRQ,
                       @RequestParam(value = "ESCGLS") String ESCGLS,
                       @RequestParam(value = "ESCCL") String ESCCL,
                       //车辆抵押信息
                       @RequestParam(value = "BAKHMC") String BAKHMC,
                       @RequestParam(value = "BAPZSX") String BAPZSX,
                       @RequestParam(value = "BADYCS") String BADYCS,
                       @RequestParam(value = "BADYGS") String BADYGS,
                       //融资信息
                       @RequestParam(value = "BASFBL") String BASFBL,
                       @RequestParam(value = "BASFJE") String BASFJE,
                       @RequestParam(value = "BAFWFL") String BAFWFL,
                       @RequestParam(value = "BAFWJE") String BAFWJE,
                       @RequestParam(value = "BASXFL") String BASXFL,
                       @RequestParam(value = "BASXJE") String BASXJE,
                       @RequestParam(value = "BABZJL") String BABZJL,
                       @RequestParam(value = "BABZJJE") String BABZJJE,
                       @RequestParam(value = "BAZLGLFL") String BAZLGLFL,
                       @RequestParam(value = "BAZLGLFY") String BAZLGLFY,
                       @RequestParam(value = "BARZJE") String BARZJE,
                       @RequestParam(value = "BARZQX") String BARZQX,
                       @RequestParam(value = "BATZZE") String BATZZE,
                       @RequestParam(value = "SFRGZS") String SFRGZS,
                       @RequestParam(value = "GZSFY") String GZSFY,
                       @RequestParam(value = "SFRBX") String SFRBX,
                       @RequestParam(value = "JQXFY") String JQXFY,
                       @RequestParam(value = "SXYFY") String SXYFY,
                       @RequestParam(value = "CCSFY") String CCSFY,
                       @RequestParam(value = "SFRGPS") String SFRGPS,
                       @RequestParam(value = "GPSJG") String GPSJG,
                       @RequestParam(value = "SFRXFWS") String SFRXFWS,
                       @RequestParam(value = "XFWSJE") String XFWSJE,
                       @RequestParam(value = "XFWSQX") String XFWSQX,
                       @RequestParam(value = "RSYWDW") String RSYWDW,
                       @RequestParam(value = "SFRRSYWX") String SFRRSYWX,
                       @RequestParam(value = "RSYWXJE") String RSYWXJE,
                       //客户详细信息
                       @RequestParam(value = "BAXUEL") String BAXUEL,
                       @RequestParam(value = "BAHYZK") String BAHYZK,
                       @RequestParam(value = "BAYWFC") String BAYWFC,
                       @RequestParam(value = "BASJYCR") String BASJYCR,
                       @RequestParam(value = "BASJYCRSJ") String BASJYCRSJ,
                       @RequestParam(value = "BACLSYSF") String BACLSYSF,
                       @RequestParam(value = "CLSYCS") String CLSYCS,
                       @RequestParam(value = "HJLX") String HJLX,
                       //客户职业信息
                       @RequestParam(value = "GZDWMC") String GZDWMC,
                       @RequestParam(value = "BAZW") String BAZW,
                       @RequestParam(value = "BAQYXZ") String BAQYXZ,
                       @RequestParam(value = "BAZZNX") String BAZZNX,
                       @RequestParam(value = "BZZC") String BZZC,
                       @RequestParam(value = "BASSHY") String BASSHY,
                       @RequestParam(value = "BADWDH") String BADWDH,
                       @RequestParam(value = "BASHNX") String BASHNX,
                       @RequestParam(value = "DWSZSF") String DWSZSF,
                       @RequestParam(value = "DWSZCS") String DWSZCS,
                       @RequestParam(value = "DWXXDZ") String DWXXDZ,
                       //共申人信息
                       @RequestParam(value = "GSRXM") String GSRXM,
                       @RequestParam(value = "GSRSJ") String GSRSJ,
                       @RequestParam(value = "GSRZJLX") String GSRZJLX,
                       @RequestParam(value = "GSRZJHM") String GSRZJHM,
                       @RequestParam(value = "GSRGX") String GSRGX,
                       //申请编号
                       @RequestParam(value = "BASQBH") String BASQBH,
                       //车型id
                       @RequestParam(value = "BACLID") String BACLID,
                        //实际居住地址
                       @RequestParam(value = "BAJZDZ") String BAJZDZ,
                       //征信授权
                       @RequestParam(value = "BAZXSQ") String BAZXSQ,
                       //手持征信授权书
                       @RequestParam(value = "BASCZX") String BASCZX,
                       //行驶证URL
                       @RequestParam(value = "BAXSZ") String BAXSZ,
                       //登记证URL
                       @RequestParam(value = "BADJZ") String BADJZ,
                       //居住状况
                       @RequestParam(value = "BAJZZK") String BAJZZK,
                       //房产区域
                       @RequestParam(value = "BAFCQY") String BAFCQY,
                       //房产抵押
                       @RequestParam(value = "BAFCDY") String BAFCDY,
                       //房产面积
                       @RequestParam(value = "BAFCMJ") String BAFCMJ,
                       //户籍所在省份
                       @RequestParam(value = "BAHJSF") String BAHJSF,
                       //户籍所在城市
                       @RequestParam(value = "BAHJCS") String BAHJCS,
                       //实际居住省份
                       @RequestParam(value = "BASJSF") String BASJSF,
                       //实际居住城市
                       @RequestParam(value = "BASJCS") String BASJCS,
                       //服务费是否月结
                       @RequestParam(value = "FWFSFKP") String FWFSFKP,
                       //GPS佣金是否月结
                       @RequestParam(value = "GPSSFKP") String GPSSFKP,
                       //第三方责任险限额
                       @RequestParam(value = "BXDSXE") String BXDSXE,
                       //申请表
                       @RequestParam(value = "applyFormImg") String applyFormImg,
                       //共申人身份证正面
                       @RequestParam(value = "jointIdCardFrontImg") String jointIdCardFrontImg,
                       //共申人身份证反面
                       @RequestParam(value = "jointIdCardBehindImg") String jointIdCardBehindImg,
                       //共申人手持征信授权书
                       @RequestParam(value = "jointHandHoldLetterOfCredit") String jointHandHoldLetterOfCredit,
                       //共申人授权书
                       @RequestParam(value = "jointLetterOfCredit") String jointLetterOfCredit,
                       //配偶身份证正面
                       @RequestParam(value = "mateIdCardFrontImg") String mateIdCardFrontImg,
                       //配偶身份证反面
                       @RequestParam(value = "mateIdCardBehindImg") String mateIdCardBehindImg,
                       //配偶手持征信授权书
                       @RequestParam(value = "mateHandHoldLetterOfCredit") String mateHandHoldLetterOfCredit,
                       //配偶征信授权书
                       @RequestParam(value = "mateLetterOfCredit") String mateLetterOfCredit,

                       //手机设备号
                       @RequestParam(value = "deviceId") String deviceId,
                       //ip地址
                       @RequestParam(value = "ipAddress") String ipAddress,
                       //地理位置经纬度
                       @RequestParam(value = "latLon") String latLon,
                       //操作系统
                       @RequestParam(value = "systermType") String systermType,
                       //手机品牌
                       @RequestParam(value = "deviceType") String deviceType
                       );



    /**
     * 新网预审批
     * @param url
     * @param approvalSubmitDto
     * @return
     */
    @RequestMapping(value = "/XW/bank", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String xw_approvalSubmit(@RequestParam(value = ".url", required = false) String url,
                             @RequestBody XW_ApprovalSubmitDto approvalSubmitDto);



    /**
     * 新网预审批状态查询接口
     * @param url
     * @param queryFinancePreApplyResultDto
     * @return
     */
    @RequestMapping(value = "/XW/bank", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String xw_approvalStateQuery(@RequestParam(value = ".url", required = false) String url,
                              @RequestBody XW_QueryFinancePreApplyResultDto queryFinancePreApplyResultDto);
}
