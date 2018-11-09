package com.tm.wechat.controller.information;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.InformationInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/8.
 */
@RequestMapping("/informations")
@RestController
public class InformationController {
    @Autowired
    private InformationInterface informationInterface;

    /**
     * 分页查询最新新闻列表
     * @param type
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getInfos(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam,
                                            Integer type, Integer page, Integer size){
        return informationInterface.getInfos(headerParam, type, page, size);
    }

    /**
     * 查询某条新闻
     * @param infoId
     * @return
     */
    @RequestMapping(value = "/{infoId}",method = RequestMethod.GET)
    public String getInfoDetails(@PathVariable Long infoId){
        return informationInterface.getInfoDetail(infoId);
    }

    /**
     * 获取banner
     * @param size
     * @return
     */
    @RequestMapping(value = "/banners", method = RequestMethod.GET)
    public ResponseEntity<Message> getBanners(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam, Integer size){
        return informationInterface.getBanners(headerParam, size);
    }
}
