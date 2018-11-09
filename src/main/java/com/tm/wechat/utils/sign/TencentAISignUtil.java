package com.tm.wechat.utils.sign;


import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by pengchao on 2018/3/2.
 */
@Component
public class TencentAISignUtil {


    private static String string = "abcdefghijklmnopqrstuvwxyz";
    public static int tencentAppId = 1106678501;
    public static String tencentAppKey = "s3GR9SHlf5xJEkc3";
    public static String bcocrUrl = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_bcocr";
    public static String driverLicenseOcrUrl = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_driverlicenseocr";
    public static String facecosmetic = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facecosmetic";
    public static String facedecoration = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facedecoration";
    public static String facemerge = "https://api.ai.qq.com/fcgi-bin/ptu/ptu_facemerge";

    private static final Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();




    /**
     * SIGN签名生成算法-JAVA版本 通用。默认参数都为UTF-8适用
     * @return 签名
     * @throws IOException
     */
    public  String getSignature(Map<String, String> params) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            //sign参数 和 空值参数 不加入算法
            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
                baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
            }
        }
//        System.err.println("未拼接APPKEY的参数："+baseString.toString());
        if(baseString.length() > 0 ) {
            baseString.deleteCharAt(baseString.length()-1).append("&app_key="+tencentAppKey);
        }
//        System.err.println("拼接APPKEY后的参数："+baseString.toString());
        // 使用MD5对待签名串求签
        String sign = md5PasswordEncoder.encodePassword(baseString.toString(),null);
//        System.out.println(sign.toUpperCase());
        return sign.toUpperCase();
    }
    /**
     * SIGN签名生成算法-JAVA版本 针对于基本文本分析接口要求text为GBK的方法
     * @param  params 请求参数集，所有参数必须已转换为字符串类型
     * @return 签名
     * @throws IOException
     */
    public  String getSignatureforNLP(HashMap<String,String> params) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {

            //sign参数 和 空值参数 不加入算法
            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
                if(param.getKey().equals("text")){
                    baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"GBK")).append("&");
                }else{
                    baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");

                }
            }
        }
        if(baseString.length() > 0 ) {
            baseString.deleteCharAt(baseString.length()-1).append("&app_key="+ tencentAppKey);
        }
        // 使用MD5对待签名串求签
            String sign = md5PasswordEncoder.encodePassword(baseString.toString(),null);
            return sign;
    }
    /**
     * 获取拼接的参数
     * @param params
     * @return
     * @throws IOException
     */
    public static String getParams(HashMap<String,String> params) throws IOException {
        //  先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            //sign参数 和 空值参数 不加入算法
            baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
        }
        return baseString.toString();
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    public static String getRandomString(int length){
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len-1)));
        }
        return sb.toString();
    }

}
