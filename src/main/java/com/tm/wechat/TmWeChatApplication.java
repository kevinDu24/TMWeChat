package com.tm.wechat;

import com.tm.wechat.utils.esign.SignHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
@EnableJpaAuditing
public class TmWeChatApplication {


// 下面是现在已经没有使用的代码 -- By ChengQiChuan 2018/10/16 9:21
//	@Bean
//	public GXWServerInfo getGxw(){
//		return new GXWServerInfo();
//	}
//
//	@Bean
//	public GXWResultInfo getGxwR(){
//		return new GXWResultInfo();
//	}
//
//	/**
//	 * spring boot 快速支持 servlet
//	 * @return
//	 */
//	@Bean
//	public ServletRegistrationBean myServlet1(){
//
//		return new ServletRegistrationBean(getGxw(),"/gXWServlet/*");
//	}
//
//	@Bean
//	public ServletRegistrationBean myServlet2(){
//
//		return new ServletRegistrationBean(getGxwR(),"/gXWResult/*");
//	}

	public static void main(String[] args) {
		//e签宝环境初始化
		SignHelper.initProject();
		SpringApplication.run(TmWeChatApplication.class, args);
	}
}
