package com.tm.wechat.utils.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.PhoneSearchProperties;
import com.tm.wechat.dto.util.PhoneAuthDto;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取手机归属地
 * Created by qiaohao on 2017/4/14.
 */
@Component
public class PhoneAuthUtil<T> {

    private static final Logger logger = LoggerFactory.getLogger(PhoneAuthUtil.class);

    @Autowired
    private PhoneSearchProperties phoneSearchProperties;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 调用贷前系统获取cookie
     *
     * @return
     * @throws Exception
     */
    public String getPnoneInfo(String phoneNum) throws Exception{
        String result= null;
        // 贷前系统登陆url
        String loginUrl = "http://apis.juhe.cn/mobile/get?phone=phoneNumParam&key=apikey";
        loginUrl = loginUrl.replace("phoneNumParam",phoneNum);
        loginUrl = loginUrl.replace("apikey",phoneSearchProperties.getApikey());
        HttpGet httpGet =  new HttpGet(loginUrl);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(20000).build();
        httpGet.setConfig(requestConfig);
        // 获取当前客户端对象
        HttpClient httpClient = HttpClients.createDefault();
        // 通过请求对象获取响应对象
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity());
        }else{
            return "";
        }
        JSONObject jsonObject =  JSONObject.fromObject(result);
        if(!"200".equals(jsonObject.getString("resultcode"))){
            return "";
        }
        PhoneAuthDto phoneAuthDto = objectMapper.readValue(jsonObject.getString("result"), PhoneAuthDto.class);
        if(phoneAuthDto == null){
            return "";
        }else {
            return phoneAuthDto.getProvince() + phoneAuthDto.getCity();
        }
    }

}
