package com.tm.wechat.controller.utils;

import com.tm.wechat.service.MapInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by LEO on 16/11/2.
 */
@RestController
@RequestMapping("/maps")
public class MapController {

    @Autowired
    private MapInterface mapInterface;

    @RequestMapping(method = RequestMethod.GET)
    public String getLocation(String latitude, String longitude){
        return mapInterface.getLocation(latitude + "," + longitude, "OMKBZ-7CIW2-OIOU5-CRF5K-WDBAH-QXFGC", "1");
    }
}
