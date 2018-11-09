package com.tm.wechat;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * 继承ClassLoader是为了方便调用defineClass方法，因为该方法的定义为protected
 * */
public class Test extends ClassLoader {

    public static void main(String[] args) throws MalformedURLException {
        System.out.println(getGeo());
//        calcMobileCity("18055313782");
    }
    public static String GetImageStr()
    {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "C:\\Users\\pengchao\\Pictures\\Camera Roll\\9370446_143039165000_2.jpg";//待处理的图片
        InputStream in = null;
        imgFile.length();
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        System.out.println(encoder.encode(data));
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }
    public static boolean GenerateImage(String imgStr)
    {//对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            String imgFilePath = "d:\\222.jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public static String calcMobileCity(String mobileNumber) throws MalformedURLException {

        //获取拍拍网的API地址
        //        String urlString = "http://virtual.paipai.com/extinfo/GetMobileProductInfo?mobile="
        //                + mobileNumber + "&amount=10000&callname=getPhoneNumInfoExtCallback";
        //淘宝网的API地址
        String urlString = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel="
                + mobileNumber;

        StringBuffer sb = new StringBuffer();
        BufferedReader buffer;
        URL url = new URL(urlString);
        String province = "";
        try {
            //获取URL地址中的页面内容
            InputStream in = url.openStream();
            // 解决乱码问题
            buffer = new BufferedReader(new InputStreamReader(in, "gb2312"));
            String line = null;
            //一行一行的读取数据
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            buffer.close();
            System.out.println(sb.toString());
            //定义两种不同格式的字符串
            //   __GetZoneResult_ = {    mts:'1594578',    province:'黑龙江',    catName:'中国移动',    telString:'15945782060',    areaVid:'30496',    ispVid:'3236139',   carrier:'黑龙江移动'}
            String objectStr = "{\"mts\":\"1594578\",\"province\":\"黑龙江\",\"catName\":\"中国移动\",\"telString\":\"15945782060\",\"areaVid\":\"30496\",\"ispVid\":\"3236139\",\"carrier\":\"黑龙江移动\"}";
            //1、使用JSONObject
            JSONObject jsonObject2 = JSONObject.fromObject(sb);
            String pro1 = jsonObject2.getString("province");
            System.out.println(pro1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //从JSONObject对象中读取城市名称
        return province/*jsonObject.getString("cityname")*/;
    }



    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();

    /**
     * 根据国家代码和手机号  判断手机号是否有效
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber, Integer countryCode){

        long phone = Long.parseLong(phoneNumber);

        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phone);

        return phoneNumberUtil.isValidNumber(pn);

    }

    /**
     * 根据国家代码和手机号  判断手机运营商
     * @date 2017-4-26 上午11:30:18
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static String getCarrier(String phoneNumber, Integer countryCode){

        long phone = Long.parseLong(phoneNumber);

        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }


    /**
     *
     * @Description: 根据国家代码和手机号  手机归属地
     * @date 2017-4-26 上午11:33:18
     * @return    参数
     */
    public static String getGeo(){

//        long phone = Long.parseLong(phoneNumber);
//
//        PhoneNumber pn = new PhoneNumber();
//        pn.setCountryCode(countryCode);
//        pn.setNationalNumber(phone);

        String string = "20180525101009";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(date);
        return dateString;

    }


}