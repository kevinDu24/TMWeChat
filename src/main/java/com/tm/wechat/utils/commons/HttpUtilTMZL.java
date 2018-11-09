package com.tm.wechat.utils.commons;

import org.apache.commons.codec.binary.Base64;
import net.sf.json.JSONObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 读取先锋太盟gps_data数据
 * Created by qiaohao on 2017/4/14.
 */
@Component
public class HttpUtilTMZL<T> {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtilTMZL.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 调用贷前系统获取cookie
     *
     * @return
     * @throws Exception
     */
    public String getCookie() throws Exception{
        String result= null;
        // 贷前系统登陆url
        String loginUrl = UrlUtils.login;

        HttpPost httpPost =  new HttpPost(loginUrl);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(20000).build();
        httpPost.setConfig(requestConfig);
        // 获取当前客户端对象
        HttpClient httpClient = HttpClients.createDefault();
        // form-data形式传参

        String authCode = httpServletRequest.getHeader("Authorization");
        if(authCode == null || authCode.isEmpty()){
            return null;
        }
        String auth = new String(Base64.decodeBase64(authCode.split(" ")[1].replace("==","").getBytes("utf-8")), "utf-8");
        // 用户名
        String userName = auth.split(":")[0];
        // 密码
        String passWord = auth.split(":")[1];
        // 获取登登录验证码的url
        String getCodeUrl = UrlUtils.getuniqueCode;
        getCodeUrl = getCodeUrl + "?objectid=" + userName + "&_=" + String.valueOf(new Date().getTime());
        HttpGet httpGet =  new HttpGet(getCodeUrl);
        httpGet.setConfig(requestConfig);
        // 通过请求对象获取响应对象
        HttpResponse codeResponse = httpClient.execute(httpGet);
        String code = "1111";
        if (codeResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String resultCode = EntityUtils.toString(codeResponse.getEntity());
            if(resultCode != null && !resultCode.isEmpty()){
                code = resultCode;
            }
        }else{
            return null;
        }
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("biaozhi", "0"));
        formParams.add(new BasicNameValuePair("userName", userName));
        formParams.add(new BasicNameValuePair("pwd", passWord));
        formParams.add(new BasicNameValuePair("按钮", "登  录"));
        formParams.add(new BasicNameValuePair("code", code));
        HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
        httpPost.setEntity(entity);
        // 通过请求对象获取响应对象
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode()==302) {
            String loginResult = EntityUtils.toString(response.getEntity());
            if(loginResult.indexOf("操作错误") != -1){
                // 密码不正确，设定cookie为null
                return null;
            }
            Header[] cookies = response.getHeaders("Set-Cookie");
            if(cookies !=null && cookies.length > 0 && cookies[0] != null){
                String [] cookieVal = cookies[0].toString().split(";");
                if(cookieVal !=null && cookieVal.length > 0 && cookieVal[0] != null )  {
                    result = cookieVal[0].replace("Set-Cookie: ","");
                }
            }
        }else{
            result = CommonUtils.errorInfo;
        }
        return result;
    }

    /**
     * 在线申请二期调用主系统接口桥梁
     *
     * @param url
     * @param httpType
     * @param postType
     * @param paramMap
     * @param bodyMap
     * @return
     * @throws Exception
     */
    public String getCoreData(String url, RequestMethod httpType, String postType, Map<String, String> paramMap,
                         Map<String, String> bodyMap)throws Exception{
        // 获取cookie
        String cookie = getCookie();
        if(cookie == null){
            return CommonUtils.errorInfo;
        } else if(cookie.indexOf(CommonUtils.errorInfo) != -1){
            return CommonUtils.errorInfo;
        }
        String result = null;
        HttpResponse response = null;
        StringBuilder wholeUrl = new StringBuilder(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(20000).build();
        // 获取当前客户端对象
        HttpClient httpClient = HttpClients.createDefault();
        // 若是get请求拼接url
        if(RequestMethod.GET.equals(httpType)){
            if(paramMap != null){
                wholeUrl.append("?");
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    //拼接url
                    wholeUrl.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            HttpGet httpGet =  new HttpGet(wholeUrl.toString());
            httpGet.setConfig(requestConfig);
            // 设定cookie
            httpGet.setHeader("Cookie",cookie);
            // 通过请求对象获取响应对象
            response = httpClient.execute(httpGet);
        }else if(RequestMethod.POST.equals(httpType)){ // post请求，进行url拼接及post表单参数构成
            if(paramMap != null){
                wholeUrl.append("?");
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    //补齐url
                    wholeUrl.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            HttpPost httpPost =  new HttpPost(wholeUrl.toString());
            httpPost.setConfig(requestConfig);
            // post表单以form-data形式提交
            if("form-data".equals(postType)){
                List<NameValuePair> formParams = new ArrayList<>();
                if(bodyMap != null){
                    for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                        formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                    HttpEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
                    httpPost.setEntity(entity);
                }
            }else if("body".equals(postType)){ // // post表单以RequestBody形式提交
                JSONObject postData = new JSONObject();
                if(bodyMap != null){
                    for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                        postData.put(entry.getKey(), entry.getValue());
                    }
                    httpPost.setEntity(new StringEntity(postData.toString(), "UTF-8"));
                }
            }
            response = httpClient.execute(httpPost);
        }

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            result = EntityUtils.toString(response.getEntity());
        }else{
            result = CommonUtils.errorInfo;
        }
        if(result != null && (result.indexOf("Session过期") != -1 || result.indexOf("USER SESSION 失效") != -1)){
            //TODO 调用同步接口同步当前用户的密码
            result = CommonUtils.errorInfo;
        }
        return result;
    }

//    /**
//     * cookie过期才重新取
//     * @param simCode sim卡号
//     * @return gps_data json
//     */
//    public String getResult(String simCode) throws Exception{
//        //cookie为空去取
//        if(cookie == null)
//            cookie = getCookie();
//        String result = getGpsData(cookie,simCode);
//        //cookie过期去取
//        if(result != null && result.indexOf("Session过期") != -1){
//            cookie = getCookie();
//            result = getGpsData(cookie,simCode);
//        }
//        return result;
//    }

}
