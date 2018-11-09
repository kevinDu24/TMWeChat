package com.tm.wechat.service.applyOnline;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dto.apply.LeaseCalculationDto;
import com.tm.wechat.dto.apply.ProductInfoDto;
import com.tm.wechat.dto.core.dto.CarTypePriceChildDto;
import com.tm.wechat.dto.core.dto.CarTypePriceDto;
import com.tm.wechat.dto.core.dto.SpecificProductDto;
import com.tm.wechat.dto.core.dto.SpecificProductListDto;
import com.tm.wechat.dto.core.result.*;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.JsonDecoder;
import com.tm.wechat.utils.apply.CarInfoSortUtil;
import com.tm.wechat.utils.commons.CommonUtils;
import com.tm.wechat.utils.commons.HttpUtilTMZL;
import com.tm.wechat.utils.commons.UrlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by pengchao on 2017/7/11.
 */
@Service
public class CoreSystemService {

    @Autowired
    private HttpUtilTMZL httpUtilTMZL;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoreSystemInterface coreSystemInterface;

    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private CarInfoSortUtil carInfoSortUtil;

    @Autowired
    private WechatProperties wechatProperties;


    /**
     * 获取车辆类型接口
     * @param carType
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getCarType(String carType, String root){
        CarTypeResult dto = new CarTypeResult();
        try {
            Map<String,String> paramMap = new HashMap();
            paramMap.put("BACXMC",root);
            paramMap.put("BASQLX","个人");
            paramMap.put("BACLLX",carType);
            paramMap.put("_",String.valueOf(new Date().getTime()));
            String result = httpUtilTMZL.getCoreData(UrlUtils.getCarType, RequestMethod.GET,"",paramMap,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, CarTypeResult.class);
            // 筛选结果集
            List<CarTypeChildDto> hqlhyhlist = dto.getHqlhyhlist();
            if(hqlhyhlist.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
            }
            //来源未先锋特惠不筛选
            if("XFTH".equals(root)){
                dto.setHqlhyhlist(hqlhyhlist);
            }else {
                List<CarTypeChildDto> carTypeChildDtos = new ArrayList<>();
                String[] carTypes = CommonUtils.CARTYPES;
                for(CarTypeChildDto carTypeChildDto : hqlhyhlist){
                    if(Arrays.asList(carTypes).contains(carTypeChildDto.getBadlmc())){
                        carTypeChildDtos.add(carTypeChildDto);
                    }
                }
                dto.setHqlhyhlist(carTypeChildDtos);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }

    /**
     * 获取细分产品
     * @param type
     * @param mainType
     * @param productType
     * @return
     */
    public ResponseEntity<Message> getSpecificType(String type,String mainType,String productType, String root){
        SpecificTypeListDto specificTypeListDto = null;
        SpecificProductListDto specificProductListDto = new SpecificProductListDto();
        try {
            Map<String,String> paramMap = new HashMap();
            paramMap.put("BACXMC",type);
            paramMap.put("BASQLX","个人");
            paramMap.put("BACLLX",mainType);
            if(productType !=null && !productType.isEmpty()){
                paramMap.put("BACPDL",productType);
            }
            paramMap.put("TYPE",root);
            paramMap.put("_",String.valueOf(new Date().getTime()));
            Map<String,String> bodyMap = new HashMap();
            String result = httpUtilTMZL.getCoreData(UrlUtils.getSpecificType, RequestMethod.GET,"",paramMap,bodyMap);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            specificTypeListDto  = objectMapper.readValue(result, SpecificTypeListDto.class);
            List<SpecificTypeDto> cpxlbycxlist = specificTypeListDto.getCpxlbycxlist();
            if(cpxlbycxlist.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
            }
            //过滤细分产品
            if("XFTH".equals(root)){
                specificTypeListDto.setCpxlbycxlist(cpxlbycxlist);
            }else {
                List<SpecificTypeDto> specificTypeDtos = new ArrayList<>();
                String[] specificTypes = CommonUtils.SPECIFICTYPES;
                for(SpecificTypeDto specificTypeDto : cpxlbycxlist){
//                if(Arrays.asList(specificTypes).contains(specificTypeDto.getBacpmc())){
//                    specificTypeDtos.add(specificTypeDto);
//                }
                    //筛选细分产品
                    for(int i = 0; i< specificTypes.length; i++ ){
                        if(specificTypeDto.getBacpmc().indexOf(specificTypes[i]) != -1){
                            specificTypeDtos.add(specificTypeDto);
                        }
                    }
                }
                if(specificTypeDtos.isEmpty()){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该产品暂无可用细分产品"), HttpStatus.OK);
                }
                specificTypeListDto.setCpxlbycxlist(specificTypeDtos);
            }
            if(specificTypeListDto == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
            }
            //去除/r /n
            List<SpecificTypeDto> resultList = specificTypeListDto.getCpxlbycxlist();
            List<SpecificProductDto> specificProductDtos = new ArrayList<>();
            SpecificProductDto specificProductDto;
            String specificType;
            String[] strings;
            for(SpecificTypeDto specificTypeDto : resultList){
                specificProductDto = new SpecificProductDto();
                specificType = specificTypeDto.getBacpbz().replaceAll ("\\r\\n", "\n");
                strings = specificType.split("\\n");
                specificProductDto.setDepict(strings);
                specificProductDto.setSpecificProductName(specificTypeDto.getBacpmc());
                specificProductDto.setSpecificProductId(specificTypeDto.getId());
                specificProductDtos.add(specificProductDto);
            }
            specificProductListDto.setSpecificProductDtos(specificProductDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, specificProductListDto), HttpStatus.OK);
    }
    /**
     * 获取产品分类
     * @param type
     * @param mainType
     * @return
     */
    public ResponseEntity<Message> getProductType(String type,String mainType, String root){
        ProductTypeListDto productTypeListDto = new ProductTypeListDto();
        try {
            Map<String,String> paramMap = new HashMap();
            paramMap.put("BACXMC",type);
            paramMap.put("BASQLX","个人");
            paramMap.put("BACLLX",mainType);
            paramMap.put("TYPE",root);
            paramMap.put("_",String.valueOf(new Date().getTime()));
            Map<String,String> bodyMap = new HashMap();
            String result = httpUtilTMZL.getCoreData(UrlUtils.getProductType, RequestMethod.GET,"",paramMap,bodyMap);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            productTypeListDto  = objectMapper.readValue(result, ProductTypeListDto.class);
            //过滤产品类型
            List<ProductTypeDto> productTypeDtos = productTypeListDto.getCpfabycxlist();
            if(productTypeDtos.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "未查询到结果"), HttpStatus.OK);
            }
            if("XFTH".equals(root)){
                productTypeListDto.setCpfabycxlist(productTypeDtos);
            }else {
                List<ProductTypeDto> productTypeDtoList = new ArrayList<>();
                String[] producttypes = CommonUtils.PRODUCTTYPES;
                for(ProductTypeDto productTypeDto : productTypeDtos){
                    if(Arrays.asList(producttypes).contains(productTypeDto.getBadlmc())){
                        productTypeDtoList.add(productTypeDto);
                    }
                }
                if(productTypeDtoList.isEmpty()){
                    return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该车辆类型暂无可用产品类型"), HttpStatus.OK);
                }
                productTypeListDto.setCpfabycxlist(productTypeDtoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, productTypeListDto), HttpStatus.OK);
    }

    /**
     * 获取制造商接口
     * @param carType
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getProductor(String carType, String productId, String searchName, String version){
        CommonDdlResult dto = new CommonDdlResult();
        try {
            String url = UrlUtils.getProductor;
            url = url.replace("车型",carType);
            url = url.replace("产品",productId);
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            if(searchName == null || searchName.isEmpty()){
                url = url.replace("gs_BACLMC:制造商;","");
            }else{
                url = url.replace("gs_BACLMC:制造商;","gs_BACLMC:制造商;".replace("制造商",searchName));
            }
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, CommonDdlResult.class);
            if(dto.getDataRows().size() == 0){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该产品品暂无可选择的制造商"), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if("2".equals(version)){
            List<CommonDdlResult>  commonDdlResults = carInfoSortUtil.sortBrand(dto.getDataRows());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, commonDdlResults), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }
    /**
     * 获取品牌列表接口
     * @param carType
     * @param productId
     * @param productor
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getBrand(String carType, String productId,String productor, String version){
        CommonDdlResult dto = new CommonDdlResult();
        try {
            String url = UrlUtils.getBrand;
            url = url.replace("车型",carType);
            url = url.replace("产品",productId);
            url = url.replace("制造商",productor);
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, CommonDdlResult.class);
            if(dto.getDataRows().size() == 0){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该产品制造商暂无可选择的品牌"), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if("2".equals(version)){
            List<CommonDdlResult>  commonDdlResults = carInfoSortUtil.sortBrand(dto.getDataRows());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, commonDdlResults), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }

    /**
     * 获取车型款式接口
     * @param carType
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getStyle(String carType, String styleName, String searchName, String mainType, String productId, String version){
        CarTypePriceResult dto = new CarTypePriceResult();
        CarTypePriceDto goalDto = new CarTypePriceDto();
        if(productId == null || productId.isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR,"系统错误，请更新版本后再试！"), HttpStatus.OK);
        }
        try {
            String url = UrlUtils.getCarStyle;
            url = url.replace("车型",carType);
            url = url.replace("款式",styleName);
            url = url.replace("产品",productId);
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            if(searchName == null || searchName.isEmpty()){
                url = url.replace("gs_BACLMC:检索条件;","");
            }else{
                url = url.replace("gs_BACLMC:检索条件;","gs_BACLMC:检索条件;".replace("检索条件",searchName));
            }
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, CarTypePriceResult.class);
            if(dto.getDataRows().size() == 0){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "该产品暂无可选择的车型"), HttpStatus.OK);
            }
            if(dto != null && dto.getDataRows() != null && !dto.getDataRows().isEmpty()){
                List<CarTypePriceChildDto> infos = new ArrayList();
                CarTypePriceChildDto info;
                BigDecimal rate;
                BigDecimal tax;
                for(CarTypePriceChildResult row : dto.getDataRows()){
                    //座位数量为0时设置默认值
                    if(row.getBazwsl() == null ||BigDecimal.ZERO.compareTo(row.getBazwsl())==0){
                        row.setBazwsl(new BigDecimal("5"));
                    }
                    info = new CarTypePriceChildDto(row);
                    BigDecimal price = row.getCxzdjg();
//                    if("新车".equals(mainType) && "乘用车".equals(carType)){
//                        rate = new BigDecimal("0.075");
//                    }else {
//                        rate = new BigDecimal("0.1");
//                    }
                    //2018年起购置税调整为10%
                    rate = new BigDecimal("0.1");
                    tax = price.divide(new BigDecimal("1.17"),2).multiply(rate).setScale(0, BigDecimal.ROUND_HALF_UP);
                    info.setBackgz(tax);
                    infos.add(info);
                }
                goalDto.setDataRows(infos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        if("2".equals(version)){
            List<CarTypePriceDto> carTypePriceDtoList = carInfoSortUtil.sortStyle(goalDto.getDataRows());
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, carTypePriceDtoList), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, goalDto), HttpStatus.OK);
    }
    /**
     * 获取车辆经销商名称接口
     * @param productId
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getSeller(String productId, String searchName){
        SellerListDto dto = new SellerListDto();
        try {
            String url = UrlUtils.getSeller;
            if(searchName == null || searchName.isEmpty()){
                url = url.replace("gs_BADMMC:渠道名称;","");
            }else{
                url = url.replace("gs_BADMMC:渠道名称;","gs_BADMMC:渠道名称;".replace("渠道名称",searchName));
            }
            url = url.replace("产品",productId);
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, SellerListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }

    /**
     * 获取回租抵押城市接口
     * @param productId
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getLeaseBackCity(String productId){
        LeaseBackCityListDto dto = new LeaseBackCityListDto();
        try {
            String url = UrlUtils.getLeaseBackCity;
            url = url.replace("产品",productId);
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, LeaseBackCityListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }

    /**
     * 保险试算接口
     * @param carType
     * @param baclzd
     * @param bapail
     * @param bxzwsl
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getInsuranceTrial(String carType, String baclzd, String bapail, String bxzwsl){
        InsuranceTrialResult dto = new InsuranceTrialResult();
        try {
            //若车辆无排量，给出默认值
            if(bapail == null || bapail.isEmpty()){
                bapail = "1.6";
            }
            //若车辆无座位数，给出默认值
            if(bxzwsl == null || bxzwsl.isEmpty()){
                bxzwsl = "5";
            }
            String url = UrlUtils.getInsuranceTrial;
            url = url.replace("车型",carType);
            url = url.replace("车辆指导价",baclzd);
            url = url.replace("排量",bapail);
            url = url.replace("座位数",bxzwsl);
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null || result.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            String[] arr =  result.split(",");
            if(arr.length != 3){
                dto.setCommercialInsurance("0");
                dto.setCompulsoryInsurance("0");
                dto.setVehicleTax("0");
            }
            dto.setCommercialInsurance(arr[0]);
            dto.setCompulsoryInsurance(arr[1]);
            dto.setVehicleTax(arr[2]);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo ), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }
    /**
     * 获取所属行业接口
     * @return
     */
    @Transactional
    public ResponseEntity<Message> getIndustry(String searchName){
        IndustryListDto dto = new IndustryListDto();
        try {
            String url = UrlUtils.getIndustry;
            url = url.replace("时间",String.valueOf(new Date().getTime()));
            if(searchName == null || searchName.isEmpty()){
                url = url.replace("gs_BASJDM:行业;","");
            }else{
                url = url.replace("gs_BASJDM:行业;","gs_BASJDM:行业;".replace("行业",searchName));
            }
            String result = httpUtilTMZL.getCoreData(url, RequestMethod.GET,"",null,null);
            if(result == null){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
            } else if(CommonUtils.errorInfo.equals(result)){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
            }
            // 筛选结果集
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            dto  = objectMapper.readValue(result, IndustryListDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR), HttpStatus.OK);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, dto), HttpStatus.OK);
    }

    /**
     * 获取融资产品详细信息
     * @param productInfoDto
     * @return
     */
    public ResponseEntity<Message> getProductDetail(ProductInfoDto productInfoDto, String userName){
        try{
            String result = coreSystemInterface.getProductDetail("rzcpxx", userName, productInfoDto.getProductId(),
                    productInfoDto.getProductName(), productInfoDto.getApplyType(), productInfoDto.getLeaseType(),
                    productInfoDto.getCarType(), productInfoDto.getMainType(), productInfoDto.getSalePrice(), wechatProperties.getSign(), wechatProperties.getTimestamp());
            if(!JsonPath.from(result).get("result.isSuccess").equals("true")){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "获取产品信息异常"), HttpStatus.OK);
            }
//            CoreResult codeResult = new CoreResult();
//            codeResult = objectMapper.readValue(result, CoreResult.class);
//            ProductDetailDto productDetailDto = codeResult.getRzcpxxRe();
//            String[] financeInfos = productDetailDto.getRzxmsz().replaceAll("\\s*", "").split("-");
//            String[] financeInfoList = removeitem(financeInfos, "a16");
//            StringBuffer finance = new StringBuffer();
//            for (int i = 0; i <financeInfoList.length -1; i++) {
//                if(i!=financeInfoList.length-1){
//                    finance.append(financeInfoList[i]).append("-");
//                }else {
//                    finance.append(financeInfoList[i]);
//                }
//            }
//            System.out.println(finance);
//            productDetailDto.setRzxmsz(finance.toString());
//            System.out.println(productDetailDto);
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, productDetailDto), HttpStatus.OK);
            return jsonDecoder.singlePropertyDecoder(result,"rzcpxxRe");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
    }


    /**
     * 融资试算
     * @param leaseCalculationDto
     * @return
     */
    public ResponseEntity<Message> getLeaseCalculation(LeaseCalculationDto leaseCalculationDto, String userName){
        ProductInfoDto productInfoDto = new ProductInfoDto();
        BeanUtils.copyProperties(leaseCalculationDto, productInfoDto);
        //获取该产品所有融资项
        String gpsFeeFlag = leaseCalculationDto.getGpsFeeFlag();
        //去除GPS是否融按钮，GPS费用必须融
        Float gpsFee = Float.parseFloat(leaseCalculationDto.getGpsFee());
        //新版本融gps和老版本gps费用不为0(代表融)gps价格限制
        if((gpsFeeFlag != null && "1".equals(gpsFeeFlag)) || (gpsFeeFlag == null && gpsFee != 0.0)){
            String mainType = leaseCalculationDto.getMainType();
            String productTypeName = leaseCalculationDto.getProductTypeName();
            String root = leaseCalculationDto.getRoot();
            Float salePrice = Float.parseFloat(leaseCalculationDto.getSalePrice());
            //不同产品GPS价格限制
            String gpsMessage = checkGpsPrice(mainType, salePrice, gpsFee, productTypeName, root);
            if(!gpsMessage.isEmpty()){
                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, gpsMessage), HttpStatus.OK);
            }
        }
        //融资信息参数转换手续费、保证金费率等去百分号(20% -> 20))
        Float paymentRatio = CommonUtils.changePercentToNumber(leaseCalculationDto.getPaymentRatio());
        Float serviceRatio =  CommonUtils.changePercentToNumber(leaseCalculationDto.getServiceRatio());
        Float tailPay =  CommonUtils.changePercentToNumber(leaseCalculationDto.getTailPay());
        Float rentRatio =  CommonUtils.changePercentToNumber(leaseCalculationDto.getRentRatio());
        Float poundageRatio =  CommonUtils.changePercentToNumber(leaseCalculationDto.getPoundageRatio());
        Float bondRatio =  CommonUtils.changePercentToNumber(leaseCalculationDto.getBondRatio());

        String result = coreSystemInterface.getLeaseCalculation("rzetzss", userName,
                leaseCalculationDto.getProductId(), leaseCalculationDto.getProductName(),
                leaseCalculationDto.getFinanceTerm(), leaseCalculationDto.getApplyType(),
                leaseCalculationDto.getLeaseType(), leaseCalculationDto.getCarType(),
                leaseCalculationDto.getMainType(), paymentRatio.toString(),
                tailPay.toString(), serviceRatio.toString(),
                poundageRatio.toString(), rentRatio.toString(),
                bondRatio.toString(), leaseCalculationDto.getPurchaseTax(),
                leaseCalculationDto.getInstallationFee(), leaseCalculationDto.getExtendedWarrantyFee(),
                leaseCalculationDto.getGpsFee(), leaseCalculationDto.getCompulsoryInsurance(),
                leaseCalculationDto.getCommercialInsurance(), leaseCalculationDto.getVehicleTax(),
                leaseCalculationDto.getUnexpected(), leaseCalculationDto.getQingHongBao(),
                leaseCalculationDto.getXfws(), leaseCalculationDto.getCompetitiveProducts(),
                leaseCalculationDto.getSalePrice(), wechatProperties.getSign(), wechatProperties.getTimestamp(),
                leaseCalculationDto.getXfwsFlag(), leaseCalculationDto.getXfwsTerm());
        ResponseEntity<Message> data = jsonDecoder.singlePropertyDecoder(result,"rzetessRe");
        Map temp = (Map) data.getBody().getData();
        if(temp == null){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, CommonUtils.errorInfo), HttpStatus.OK);
        }
        String carType = leaseCalculationDto.getCarType();
        if(temp.get("BARZZE") == null || temp.get("BARZZE").toString().trim().isEmpty()
                || temp.get("FXRZE") == null || temp.get("FXRZE").toString().trim().isEmpty()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资试算错误，请联系网站管理人员！"), HttpStatus.OK);
        }
        //融资金额
//        double financeAmount = Double.parseDouble(temp.get("BARZZE").toString());
        //风险融资额
//        double riskAmount = Double.parseDouble(temp.get("FXRZE").toString());
//        if(financeAmount > 100000){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资额超出融资上限10万！"), HttpStatus.OK);
//        }
//        if("乘用车".equals(carType)){
//            if(riskAmount>= 60000){
//                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "风险融资额超出融资上限！(乘用车上限为6万、非乘用车上限为8万)"), HttpStatus.OK);
//            }
//        }else {
//            if(riskAmount >= 80000){
//                return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "风险融资额超出融资上限！(乘用车上限为6万、非乘用车上限为8万)"), HttpStatus.OK);
//            }
//        }
        return data;
    }

    /**
     *GPS价格限制
     * @param mainType 新车二手车
     * @param salePrice 车辆售价
     * @param gpsFee GPS费用
     * @param productTypeName 产品类型名称
     * @param root 产品来源
     * @return
     */
    public String checkGpsPrice(String mainType, Float salePrice, Float gpsFee, String productTypeName, String root) {
       //新增根据产品类型名称和来源判断是否为特惠融，限制其gps价格(跳过老版本)
        //老版本保持原先的check
        if(productTypeName == null || productTypeName.isEmpty() || root == null || root.isEmpty()){
            return checkGpsFee(mainType, salePrice, gpsFee);
        }
        String message = "";
        //GPS价格下限不低于650元
        if(gpsFee < 650){
            message =  "GPS下限为650元";
            return message;
        }
        //GPS上限check
        //HPL特惠融无GPS上限，其他产品有上限
        if(!("HPL".equals(root) && productTypeName.contains("特惠融"))){
            message = checkGpsFee(mainType, salePrice, gpsFee);
        }
        return message;
    }


    /**
     *GPS价格限制(老版本)
     * @param mainType 新车二手车
     * @param salePrice 车辆售价
     * @param gpsFee GPS费用
     * @return
     */
    public String checkGpsFee(String mainType, Float salePrice, Float gpsFee) {
        String message = "";
        if("新车".equals(mainType)){
            if(salePrice<20000 && gpsFee>1000){
                message = "GPS上限为1000";
            }else if(salePrice >= 20000 && salePrice<35000 && gpsFee > 3000){
                message = "GPS上限为3000";
            }else if(salePrice >= 35000 && gpsFee > 4000){
                message = "GPS上限为4000";
            }
        }
        if("二手车".equals(mainType)){
            if(salePrice<20000 && gpsFee>1000){
                message = "GPS上限为1000";
            }else if(salePrice >= 20000 && salePrice<35000 && gpsFee > 3000){
                message = "GPS上限为3000";
            }else if(salePrice >= 35000 && salePrice < 55000 && gpsFee > 4500){
                message = "GPS上限为4500";
            }else if(salePrice >= 55000 && gpsFee > 6000){
                message = "GPS上限为6000";
            }
        }
        return message;
    }

//    获取该产品所有的可融资项
    public String[] getFinanceInfo(ProductInfoDto productInfoDto,  String userName){
        String[] financeInfos = {};
        String result = coreSystemInterface.getProductDetail("rzcpxx", userName, productInfoDto.getProductId(),
                productInfoDto.getProductName(), productInfoDto.getApplyType(), productInfoDto.getLeaseType(),
                productInfoDto.getCarType(), productInfoDto.getMainType(), productInfoDto.getSalePrice(), wechatProperties.getSign(), wechatProperties.getTimestamp());
        if(!JsonPath.from(result).get("result.isSuccess").equals("true")){
            return  financeInfos;
        }
        Map map = (Map) JsonPath.from(result).get("rzcpxxRe");
        financeInfos = map.get("RZXMSZ").toString().replaceAll("\\s*", "").split("-");
        String[] financeInfoList = removeitem(financeInfos, "a16");
        return financeInfos;
    }


    public static String[] removeitem(String[] arrays,String str){
        String[] tempArr = new String[arrays.length];
        int i = 0;
        for(String s:arrays){
            if(!s.equals(str)){
                tempArr[i] = s;
                i++;
            }
        }
        return tempArr;
    }

    //GPS和先锋卫士融资限制
    public String checkFinanceInfo(String gpsFeeFlag, String xfwsFlag, String[] financeInfos){
        boolean gpsInfo = Arrays.asList(financeInfos).contains("a6");
        boolean xfwsInfo = Arrays.asList(financeInfos).contains("a16");
        String message = "";
        //二者都可融，必须且只能融一项
        if(gpsInfo && xfwsInfo){
            if("1".equals(gpsFeeFlag) && "1".equals(xfwsFlag)){
                message = "先锋卫士和GPS只能融一个";
            }
            if("0".equals(gpsFeeFlag) && "0".equals(xfwsFlag)){
                message = "先锋卫士和GPS必须融一个";
            }
        }
        //先锋卫士不可融GPS可融时GPS必须融
        if(gpsInfo && !xfwsInfo){
            if ("0".equals(gpsFeeFlag)){
                message = "GPS费用必须融资";
            }
        }
        //GPS不可融先锋卫士可融时先锋卫士必须融
        if(!gpsInfo && xfwsInfo){
            if("0".equals(xfwsFlag)){
                message = "先锋卫士金额必须融资";
            }
        }
        return message;
    }



}
