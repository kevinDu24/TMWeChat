package com.tm.wechat.service.car;

import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.JsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by LEO on 16/10/28.
 */
@Service
public class CarService {
    @Autowired
    private CarInterface carInterface;

    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private WechatProperties wechatProperties;

    /**
     * 获取车辆列表
     * @param type
     * @param financeProductId
     * @param conditionId
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getCars(Integer type, String financeProductId, String conditionId, String userName){
        String result = carInterface.getCars("queryVehicleInfoList", userName, wechatProperties.getTimestamp(),
                wechatProperties.getSign(), type, financeProductId, conditionId);
//        return jsonDecoder.jsonDecoder("queryVehicleInfoList", result);
        return jsonDecoder.singlePropertyDecoder(result, "vehicleInfoList");
    }

    /**
     * 查询车辆详情
     * @param carId
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getCarDetail(String carId, String userName){
        String result = carInterface.getCarDetail("queryVehicleInfo", userName, wechatProperties.getTimestamp(),
                wechatProperties.getSign(), carId);
//        return jsonDecoder.jsonDecoder("queryVehicleInfo", result);
        return jsonDecoder.singlePropertyDecoder(result, "vehicleInfo");
    }

    /**
     * 获取融资车型
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getFinanceCar(String userName){
        String result = carInterface.getFinanceCar("getQueryCpcx", userName, wechatProperties.getTimestamp(), wechatProperties.getSign());
//        return jsonDecoder.jsonDecoder("getQueryCpcx", result);
        return jsonDecoder.singlePropertyDecoder(result, "queryCpcxList");
    }
}
