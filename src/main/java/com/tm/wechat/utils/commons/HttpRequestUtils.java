package com.tm.wechat.utils.commons;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by pengchao on 2017/7/27.
 */
public class HttpRequestUtils {

    public static final String GET_URL = "http://112.4.27.9/mall-back/if_user/store_list?storeId=32";
    public static final String POST_URL = "http://119.23.128.214:8080/carWeb/external/app/createPreCarImage.html";


    public static String TestPost(String param, String userName, String fromSource) throws IOException {

        URL url = new URL("http://119.23.128.214:8080/carWeb/external/app/createPreCarImage.html");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        out.write("content=" + param + "&userName=" + userName +"&fromSource=" + fromSource); // 向页面传递数据。post的关键所在！
        out.flush();
        out.close();
        // 一旦发送成功，用以下方法就可以得到服务器的回应：
        String sCurrentLine;
        String sTotalString;
        sCurrentLine = "";
        sTotalString = "";
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        // 传说中的三层包装阿！
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(
                l_urlStream));
        while ((sCurrentLine = l_reader.readLine()) != null) {
            sTotalString += sCurrentLine + "\r\n";

        }
        System.out.println(sTotalString);
        return sTotalString;

    }

//    public static void main(String[] args) throws IOException {
////        TestPost();
//    }
}