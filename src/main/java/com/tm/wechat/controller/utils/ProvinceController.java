package com.tm.wechat.controller.utils;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/10/27.
 */
@RestController
@RequestMapping("/provinces")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    /**
     * 获取省份
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getProvinces(){
        return provinceService.getProvinces();
    }

    /**
     * 获取省份下属城市
     * @param provinceId
     * @return
     */
    @RequestMapping(value = "/{provinceId}/cities")
    public ResponseEntity<Message> getCities(@PathVariable Long provinceId){
        return provinceService.getCities(provinceId);
    }
}
