package com.tm.wechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LEO on 16/9/1.
 */
public class Utils {
    public static Boolean isCardId(String str){
        return str.matches("(\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
    }

    public static Boolean isNumber(String str){
        return str.matches("[0-9]+");
    }

    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //地球平均半径
    private static final double EARTH_RADIUS = 6378137;

    public static SimpleDateFormat yyyymmdd =new SimpleDateFormat("YYYYMMdd");

    public static SimpleDateFormat yyyymmddhhmmss =new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a/2),2)
                                + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)
                )
        );
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    //把经纬度转为度（°）
    private static double rad(double d){
        return d * Math.PI / 180.0;
    }



    /**
     * 将日期转换为字符串显示
     * @param time
     * @param simpleDateFormat
     * @return
     */
    public static String getStrDate(Date time, SimpleDateFormat simpleDateFormat){
        if (time == null) {
            return null;
        }
        try{
            return simpleDateFormat.format(time);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
