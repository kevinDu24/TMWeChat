package com.tm.wechat.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lijian on 2015/10/19.
 */
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.sendError(response.SC_UNAUTHORIZED,authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("conductss");
        super.afterPropertiesSet();
    }
}
