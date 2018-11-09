package com.tm.wechat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by LEO on 2015/9/15.
 */
@Configuration
@EnableScheduling
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public WechatProperties wechatProperties(){
        return new WechatProperties();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public FileUploadProperties fileUploadProperties(){
        return new FileUploadProperties();
    }

    @Bean
    public VersionProperties versionProperties(){
        return new VersionProperties();
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public AccountProperties accountProperties(){
        return new AccountProperties();
    }

    @Bean
    public MessageProperties messageProperties(){ return new MessageProperties(); }

//    @Bean
//    public GXWServerInfo gXWServerInfo(){
//        return new GXWServerInfo();
//    }
//
//    @Bean
//    public GXWResultInfo gXWResultInfo(){
//        return new GXWResultInfo();
//    }

    @Bean
    public PhoneSearchProperties phoneSearchProperties(){
        return new PhoneSearchProperties();
    }

    @Bean
    public FaceIdProperties faceIdProperties(){return  new FaceIdProperties();}

    @Bean
    public WzProperties wzProperties(){return new WzProperties();}

    @Bean
    public MailProperties mailProperties(){return  new MailProperties();}

    @Autowired
    private WechatProperties wechatProperties;

    @Bean
    public WxMpService wxMpService(){
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(wechatProperties.getAppid());
        config.setSecret(wechatProperties.getAppsecret());
        config.setToken(wechatProperties.getToken());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        configurer.setUseSuffixPatternMatch(false);
    }
}
