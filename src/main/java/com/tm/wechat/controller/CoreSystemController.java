package com.tm.wechat.controller;

import com.tm.wechat.dto.apply.LeaseCalculationDto;
import com.tm.wechat.dto.apply.ProductInfoDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.applyOnline.CoreSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by pengchao on 2017/7/11.
 */
@RestController
@RequestMapping("/core")
@PreAuthorize("@permission.isDealerUser(authentication.principal.username)")
public class CoreSystemController {

    @Autowired
    private CoreSystemService coreSystemService;

    /**
     * 获取车辆类型接口
     * @param carType
     * @param root 来源（HPL、先锋特惠）
     * @return
     */
    @RequestMapping(value = "/getCarType", method = RequestMethod.GET)
    public ResponseEntity<Message> getCarType(@RequestParam(value = "carType", required = true) String carType, @RequestParam(value = "root", required = true) String root){
        return coreSystemService.getCarType(carType,root);
    }

    /**
     *获取细分产品
     * @param type 车辆类型（乘用车）
     * @param mainType 车辆新旧(新车、二手车)
     * @param productTypeId
     * @param root 来源（HPL、先锋特惠）
     * @return
     */
    @RequestMapping(value = "/getSpecificType", method = RequestMethod.GET)
    public ResponseEntity<Message> getSpecificType(String type, String mainType, @RequestParam(required = false) String productTypeId,  @RequestParam(value = "root", required = true) String root){
        return coreSystemService.getSpecificType(type, mainType, productTypeId, root);
    }

    /**
     * 获取产品类型
     * @param type 车辆类型（乘用车）
     * @param mainType 车辆新旧(新车、二手车)
     * @param root 来源（HPL、先锋特惠）
     * @return
     */
    @RequestMapping(value = "/getProductType", method = RequestMethod.GET)
    public ResponseEntity<Message> getProductType(@RequestParam(value = "type", required = true) String type,
                                                  @RequestParam(value = "mainType", required = true) String mainType,  @RequestParam(value = "root", required = true) String root){
        return coreSystemService.getProductType(type, mainType, root);
    }

    /**
     * 获取制造商接口
     * @param carType
     * @return
     */
    @RequestMapping(value = "/getProductor", method = RequestMethod.GET)
    public ResponseEntity<Message> getProductor(@RequestParam(value = "productId", required = true) String productId,
                                                @RequestParam(value = "carType", required = true) String carType,
                                                @RequestParam(value = "searchName", required = false) String searchName,
                                                @RequestParam(value = "version", required = false) String version){
        return coreSystemService.getProductor(carType, productId, searchName, version);
    }

    /**
     * 获取品牌列表接口
     * @param carType
     * @param productId
     * @param productor
     * @return
     */
    @RequestMapping(value = "/getBrand", method = RequestMethod.GET)
    public ResponseEntity<Message> getBrand(@RequestParam(value = "productId", required = true) String productId,
                                                @RequestParam(value = "carType", required = true) String carType,
                                            @RequestParam(value = "productor", required = true) String productor,
                                            @RequestParam(value = "version", required = false) String version){
        return coreSystemService.getBrand(carType, productId, productor, version);
    }

    /**
     * 获取车型款式
     * @param carType
     * @param styleName
     * @param searchName 搜索条件
     * @param mainType
     * @param productId 产品ID
     * @return
     */
    @RequestMapping(value = "/getStyle", method = RequestMethod.GET)
    public ResponseEntity<Message> getStyle(@RequestParam(value = "carType", required = true) String carType,
                                            @RequestParam(value = "styleName", required = true) String styleName,
                                            @RequestParam(value = "searchName", required = false) String searchName,
                                            @RequestParam(value = "mainType", required = true) String mainType,
                                            @RequestParam(value = "productId", required = false) String productId,
                                            @RequestParam(value = "version", required = false) String version){
        return coreSystemService.getStyle(carType, styleName, searchName, mainType, productId, version);
    }
    /**
     * 获取车辆经销商名称接口
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getSeller", method = RequestMethod.GET)
    public ResponseEntity<Message> getSeller(@RequestParam(value = "productId", required = true) String productId,
                                             @RequestParam(value = "searchName", required = false) String searchName){
        return coreSystemService.getSeller(productId, searchName);
    }

    /**
     * 获取回租抵押城市接口
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getLeaseBackCity", method = RequestMethod.GET)
    public ResponseEntity<Message> getLeaseBackCity(@RequestParam(value = "productId", required = true) String productId){
        return coreSystemService.getLeaseBackCity(productId);
    }
    /**
     * 保险试算
     * url:http://domain/core/getInsuranceTrial?styleName=雅阁&carType=乘用车&baclzd=236000&bapail=1.6&bxzwsl=5
     * @param carType
     * @param baclzd
     * @param bapail
     * @param bxzwsl
     * @return
     */
    @RequestMapping(value = "/getInsuranceTrial", method = RequestMethod.GET)
    public ResponseEntity<Message> getInsuranceTrial(@RequestParam(value = "carType", required = true) String carType,
                                                     @RequestParam(value = "baclzd", required = true) String baclzd,
                                                     @RequestParam(value = "bapail", required = true) String bapail,
                                                     @RequestParam(value = "bxzwsl", required = true) String bxzwsl){
        return coreSystemService.getInsuranceTrial(carType, baclzd, bapail, bxzwsl);
    }

    /**
     * 获取所属行业接口
     * @return
     */
    @RequestMapping(value = "/getIndustry", method = RequestMethod.GET)
    public ResponseEntity<Message> getIndustry(@RequestParam(value = "searchName", required = false) String searchName) {
        return coreSystemService.getIndustry(searchName);
    }


    /**
     * 获取产品详细信息
     * @param productInfoDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/getProductDetail", method = RequestMethod.POST)
    public ResponseEntity<Message> getProductDetail(@RequestBody ProductInfoDto productInfoDto, Principal user) {
        return coreSystemService.getProductDetail(productInfoDto, user.getName());
    }
    /**
     * 获取融资信息的接口
     * @param leaseCalculationDto
     * @param user
     * @return
     */
    @RequestMapping(value = "/getLeaseCalculation", method = RequestMethod.POST)
    public ResponseEntity<Message> getLeaseCalculation(@RequestBody LeaseCalculationDto leaseCalculationDto, Principal user) {
        return coreSystemService.getLeaseCalculation(leaseCalculationDto, user.getName());
    }

}
