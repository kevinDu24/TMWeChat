package com.tm.wechat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.dto.baidu.ShortUrlDto;
import com.tm.wechat.dto.baidu.ShortUrlResultDto;
import com.tm.wechat.service.sign.ApplyContractService;
import com.tm.wechat.utils.commons.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by pengchao on 2018/7/20.
 */
@Service
public class BaiduShortUrlService {

    @Autowired
    BaiduShortUrlInterface baiduShortUrlInterface;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(ApplyContractService.class);

    public String getShortUrl(String url){
        ShortUrlDto shortUrlDto = new ShortUrlDto();
        shortUrlDto.setUrl(url);

        ShortUrlResultDto codeResult = new ShortUrlResultDto();
        try {
            String result = baiduShortUrlInterface.getShortUrl(shortUrlDto);
            if(result == null){
                return url;
            }
            logger.info("getShortUrl={}", result);
            codeResult = objectMapper.readValue(result, ShortUrlResultDto.class);
        } catch (IOException e) {
            e.printStackTrace();
            return url;
        }
        if("0".equals(codeResult.getCode()) && !CommonUtils.isNull(codeResult.getShortUrl())){
                return codeResult.getShortUrl();
        }else {
            return url;
        }
    }
}
