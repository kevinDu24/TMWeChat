package com.tm.wechat.utils.commons;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by pengchao on 2017/5/16.
 */
public class CommonUtils {
    public static String[] CARTYPES = {"乘用车","微面","皮卡","轻客","轻卡", "微卡"};
    public static String[] PRODUCTTYPES = {"特惠融"};
    public static String[] SPECIFICTYPES = {"特惠融"};
    public static String errorInfo = "系统异常";
    private final static String DES = "DES";
    public  static String usedCarUsername = "taimeng";
    public  static String usedCarFromSource = "太盟";
    public  static String encryptKey = "taimeng1";
    public static SimpleDateFormat simpleDateFormat_8 =new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat simpleDateFormat_14 =new SimpleDateFormat("yyyyMMddHHmmss");
    public static String CHANNEL = "04";//提交渠道 04
    public static String idType = "01";//证件提交类型 01 身份证;
    public static String signChannel = "04"; //签署渠道
    public static String loginkey = "login_token_";
    public static String errorCode = "10086";
//    public static String CARTHREEHUNDRED_TOKEN= "f51daaef0e003ddfc4a7c7902601af87";//车300测试token
    public static String CARTHREEHUNDRED_TOKEN = "45aa2cdd5a60a832321e5ad4fceb3897";//车300生产token


    /**
     * URL解码
     *
     * @param param
     * @return
     */
    public static String urlDecoder(String param) {
        if (param == null) {
            return null;
        }
        try {
            return URLDecoder.decode(param, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 返回是否为空
     *
     * @param param
     * @return
     */
    public static boolean isNull(String param) {
        if (param == null)
            return true;
        else if (("").equals(param.trim()))
            return true;
        else if ("(null)".equals(param))
            return true;
        else
            return false;
    }

    public static boolean isNotNull(String param) {
       return  !isNull(param);
    }

    /**
     * 百分数转小数
     * @param percent
     * @return
     */
    public static float changePercentToPoint(String percent) {
        if (percent.contains("%")) {
            Float f = Float.valueOf(percent.replaceAll("%", ""));
            return f/100;
        }
        if(percent == null || "".equals(percent)){
            return 0.0f;
        }
        return Float.valueOf(percent);
    }

    /**
     * 百分数转数字
     * @param percent
     * @return
     */
    public static float changePercentToNumber(String percent) {
        if (percent.contains("%")) {
            Float f = Float.valueOf(percent.replaceAll("%", ""));
            return f;
        }
        if(percent == null || "".equals(percent)){
            return 0.0f;
        }
        return Float.valueOf(percent);
    }


    //根据身份证证号获取年龄
    public static int getAgeByIdCardNum(String idCardNum){
        int leh = idCardNum.length();
        String dates= "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        if (leh == 18) {
            dates = idCardNum.substring(6, 10);
            String year=df.format(new Date());
            int age=Integer.parseInt(year)-Integer.parseInt(dates);
            return age;
        }else{
            dates = idCardNum.substring(6, 8);
            StringBuilder sb = new StringBuilder("19");
            sb.append(dates);
            String year=df.format(new Date());
            int age = Integer.parseInt(year)-Integer.parseInt(sb.toString());
            return age;
        }
    }

    /**
     * 在线申请提交需要将出生日期转换为"xxxx年xx月xx日"
     * @param date
     * @return
     */
    public static String dateConvert(String date){
        String convertDate = "";
        if(date == null || "".equals(date.trim())){
            return convertDate;
        }
        if(date.length() == 8){
            convertDate = date.substring(0,4) + "年" + date.substring(4,6) +"月" + date.substring(6,8)+"日";
        }else {
            convertDate = date.substring(0,4) + "年" + date.substring(5,7) +"月" + date.substring(8,10)+"日";
        }
        return convertDate;
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

    /**
     * sql语句like查询值构造
     *
     * @param param
     * @return
     */
    public static String likePartten(String param) {
        return "%" + param + "%";
    }

    /**
     * 获取文件后缀名(包含.)
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String inPartten(Object[] param) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Object object : param) {
            stringBuffer.append("'" + object + "',");
        }
        if (stringBuffer.length() > 0) {
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    /**
     * 转string字符串
     *
     * @param param
     * @return
     */
    public static String getStr(Object param) {
        try {
            if (param != null)
                return param.toString();
            else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 转date
     *
     * @param param
     * @return
     */
    public static Date getDate(Object param) {
        try {
            if (param != null)
                return (Date)param;
            else
                return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
