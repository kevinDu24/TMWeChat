package com.tm.wechat.config;

import com.tm.wechat.security.CustomUserDetailsService;
import com.tm.wechat.security.RestAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by LEO on 2015/9/12.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }

    @Bean
    UserDetailsService customUserDetailsService(){
        return new CustomUserDetailsService(); //注册CustomUserService的bean；
    }

    @Bean
    RestAuthenticationEntryPoint restAuthenticationEntryPoint(){

        return new RestAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, a) -> response.setStatus(HttpServletResponse.SC_NO_CONTENT))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/tpl/**", "/json/**", "/l10n/**", "/assets/**", "/fonts/**","/libs/**","/index.html", "/baidu_verify_58AcEkTqcA.html", "/addCustInfo.html" ,"/", "/img/**",
                "/weChats/push", "/weChats/urlSignature", "/gps/**", "/baiduMap/convert", "/baiduMap/geoCoder","/addInfo/addCustInfo",
                "/financeProducts","/calculators/web", "/maps", "/sms/**", "/contracts/applyNum","/hpl.html", "/files/**", "/apply/**",
                "/gXWServlet/**","/gXWResult/**","/gxb/processGxbData","/gxb/**",
                "/sysUsers/sendRandomCode","/userInfo/**","/tencent/postCardOcr",
                "/sysUsers/coreSystemSendCode","/sysUsers/addressInputSendCode","/statistics/**"
        );

    }
}
