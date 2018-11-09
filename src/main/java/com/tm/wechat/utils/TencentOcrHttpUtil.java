package com.tm.wechat.utils;

/**
 * Created by pengchao on 2018/3/6.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 腾讯OCR扫描http,POST请求模拟
 */
public class TencentOcrHttpUtil {

    public static String httpPost(String url, Map<String, String> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数，并将value，urlencoder
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(e.getValue().trim(),"UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                sb.append("&");
            }
        }
        String str = sb.substring(0, sb.length() - 1);
//        System.out.println("send_url:" + url);
//        System.out.println("send_data:" + str);
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(str);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            //一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
