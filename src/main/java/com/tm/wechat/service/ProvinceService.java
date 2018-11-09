package com.tm.wechat.service;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tm.wechat.dao.RedisRepository;
import com.tm.wechat.dto.area.City;
import com.tm.wechat.dto.area.Province;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEO on 16/10/27.
 */
@Service
public class ProvinceService {
    @Value("classpath:static/json/province.json")
    private Resource province;

    @Value("classpath:static/json/city.json")
    private Resource city;

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private Gson gson;

    /**
     * 获取省份列表
     * @return
     * @throws IOException
     */
    public ResponseEntity<Message> getProvinces() {
        List<Province> provinces = (List<Province>) redisRepository.get("provinces");
        if(null == provinces || provinces.isEmpty()){
            Type listType = new TypeToken<ArrayList<Province>>(){}.getType();
            try {
                provinces = gson.fromJson(IOUtils.toString(province.getInputStream()), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            redisRepository.save("provinces", provinces);
        }
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, provinces), HttpStatus.OK);
    }

    /**
     * 获取某省下的城市列表
     * @param provinceId 省份ID
     * @return
     */
    public ResponseEntity<Message> getCities(Long provinceId){
        List<City> allCities = (List<City>) redisRepository.get("cities");
        if(null == allCities || allCities.isEmpty()){
            Type listType = new TypeToken<ArrayList<City>>(){}.getType();
            try {
                allCities = gson.fromJson(IOUtils.toString(city.getInputStream()), listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            redisRepository.save("cities", allCities);
        }
        List<City> cities = Lists.newArrayList();
        allCities.forEach(city -> {
            if (city.getProID().equals(provinceId)){
                cities.add(city);
            }
        });
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, cities), HttpStatus.OK);
    }

}
