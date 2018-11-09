package com.tm.wechat.controller;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by LEO on 16/10/28.
 */
@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * 获取车辆列表
     * @param type 类型 1.查询厂商 2.查询品牌 3.查询车型
     * @param financeProductId 融资产品ID
     * @param conditionId 查询品牌需要输入厂商ID,查询车型需要品牌ID
     * @param user 当前登录用户
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getCars(@RequestParam Integer type, @RequestParam String financeProductId, @RequestParam String conditionId, Principal user){
        return carService.getCars(type, financeProductId, conditionId, user.getName());
    }

    /**
     * 查询车辆详情
     * @param carId 车辆ID
     * @param user 当前登录用户
     * @return
     */
    @RequestMapping(value = "/{carId}", method = RequestMethod.GET)
    public ResponseEntity<Message> getCarDetail(@PathVariable String carId, Principal user){
        return carService.getCarDetail(carId, user.getName());
    }

    /**
     * 获取融资车型
     * @param user
     * @return
     */
    @RequestMapping(value = "/finance/carTypes", method = RequestMethod.GET)
    public ResponseEntity<Message> getFinanceCar(Principal user){
        return carService.getFinanceCar(user.getName());
    }
}
