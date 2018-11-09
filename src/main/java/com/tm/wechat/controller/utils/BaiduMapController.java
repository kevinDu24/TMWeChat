package com.tm.wechat.controller.utils;

import com.tm.wechat.service.BaiduMapInterface;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/9/18.
 */
@RestController
@RequestMapping("/baiduMap")
public class BaiduMapController {

    @Autowired
    private BaiduMapInterface baiduMapInterface;

    /**
     * 百度地图坐标转换
     * @param latitude
     * @param longitude
     * @return
     */
    @RequestMapping(value = "/convert", method = RequestMethod.GET)
    public String getBaiduLocation(String latitude, String longitude){
        return baiduMapInterface.getLocation("0", "4", longitude, latitude);
    }

    /**
     * 百度地图位置获取
     * @param location
     * @return
     */
    @RequestMapping(value = "/geoCoder", method = RequestMethod.GET)
    public String geoCoder(String location){
        return baiduMapInterface.geoCoder("btsVVWf0TM1zUBEbzFz6QqWF", location, "json", "1");
    }
}
