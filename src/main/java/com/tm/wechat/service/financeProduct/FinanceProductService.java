package com.tm.wechat.service.financeProduct;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dao.CarTypeRepository;
import com.tm.wechat.domain.CarType;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.service.JsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by LEO on 16/9/13.
 */
@Service
public class FinanceProductService {

    @Autowired
    private CarTypeRepository carTypeRepository;

    @Autowired
    private FinanceProductInterface financeProductInterface;

    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WechatProperties wechatProperties;


    /**
     * 获取融资方案(非核心服务器)
     * @return
     */
    public ResponseEntity<Message> getFinancePlans(){
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "功能维护中..."), HttpStatus.OK);
        List<CarType> carTypes = carTypeRepository.findAll();
        carTypes.forEach(carType -> {
            carType.getFinanceProducts().forEach(financeProduct -> {
                financeProduct.setCarType(null);
                financeProduct.setRate(null);
                financeProduct.setMaxFinance(null);
            });
        });
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, carTypes), HttpStatus.OK);
    }

    /**
     * 获取融资产品(核心服务器)
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getFinancePlans(String carType, String userName){
        String result = financeProductInterface.getFinancePlans("getQueryCpdl", userName, wechatProperties.getTimestamp(),
                wechatProperties.getSign(), carType);
//        return jsonDecoder.jsonDecoder("queryProductPlanSizeList", result);
        return jsonDecoder.singlePropertyDecoder(result, "bam068");
    }

    /**
     * 获取融资产品方案
     * @param carType
     * @param typeId
     * @param userName
     * @return
     * @throws IOException
     */
    public ResponseEntity<Message> getProductCase(String carType,  String typeId, String userName)throws IOException {
        String result = financeProductInterface.getProductCase("getQueryCpfa", userName, wechatProperties.getTimestamp(),
                wechatProperties.getSign(), carType, typeId);
//        CallBackDto productCaseCallBackDto = objectMapper.readValue(result, CallBackDto.class);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, productCaseCallBackDto.getBam054()), HttpStatus.OK);
        return jsonDecoder.singlePropertyDecoder(result, "bam054");
    }

    /**
     * 获取融资产品详情
     * @param financeProductId
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getFinanceProduct(String financeProductId, String userName){
        String result = financeProductInterface.getFinanceProduct("queryProductPlanInfo", userName,
                wechatProperties.getTimestamp(), wechatProperties.getSign(), financeProductId);
//        return jsonDecoder.jsonDecoder("queryProductPlanInfo", result);
        return jsonDecoder.singlePropertyDecoder(result, "productPlanInfo");
    }
}
