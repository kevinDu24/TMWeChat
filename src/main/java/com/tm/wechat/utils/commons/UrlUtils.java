package com.tm.wechat.utils.commons;

/**
 * Created by pengchao on 2017/5/16.
 */
public class UrlUtils {
    // 登录
    public static String login = "http://happyleasing.cn/TMZL/loginuser.action";
    // 获取登陆验证码
    public static String getuniqueCode = "http://happyleasing.cn/TMZL/getuniqueCode.action";
    // 获取车型大类
    public static String getCarType = "http://happyleasing.cn/TMZL/hqlhyh.action";
    // 获取细分产品和产品描述
    public static String getSpecificType = "http://happyleasing.cn/TMZL/getcpxlBycx.action";
    // 获取产品类型
    public static String getProductType = "http://happyleasing.cn/TMZL/getcpdlBycx.action";
    // 获取制造商
    public static String getProductor = "http://happyleasing.cn/TMZL/listjsonbam003.action?operexl=json&page.search=true&nd=时间&page.pageSize=1000&page.curPageNo=1&page.orderBy=&page.order=asc&filters=%7bgs_BACLMC:制造商;gs_BAZTDM:6003000;gs_BACLJB:1;gs_CLLX:CG;gs_ZMC:XXLR;gs_BACPCX:车型;gs_BACPMC:产品%7d&queryfilters=%7bgs_BACLMC:制造商;gs_BAZTDM:6003000;gs_BACLJB:1;gs_CLLX:CG;gs_ZMC:XXLR;gs_BACPCX:车型;gs_BACPMC:产品%7d";
    // 获取车辆品牌
    public static String getBrand = "http://happyleasing.cn/TMZL/listjsonbam003.action?operexl=json&page.search=false&nd=时间&page.pageSize=1000&page.curPageNo=1&page.orderBy=&page.order=asc&filters=%7bgs_CLSJMC:制造商;gs_BAZTDM:6003000;gs_BACLJB:2;gs_CLLX:CG;gs_ZMC:XXLR;gs_BACPCX:车型;gs_BACPMC:产品%7d";
    // 获取车辆款式
    public static String getCarStyle = "http://happyleasing.cn/TMZL/listjsonbam003.action?operexl=json&page.search=true&nd=时间&page.pageSize=1000&page.curPageNo=1&page.orderBy=&page.order=asc&filters=%7bgs_BACLMC:检索条件;gs_CLSJMC:款式;gs_BAZTDM:6003000;gs_BACLJB:4;gs_CLLX:CG;gs_ZMC:XXLR;gs_BACPCX:车型;gs_BACPMC:产品%7d&queryfilters=%7bgs_BACLMC:检索条件;gs_CLSJMC:款式;gs_BAZTDM:6003000;gs_BACLJB:4;gs_CLLX:CG;gs_ZMC:XXLR;gs_BACPCX:车型;gs_BACPMC:产品%7d";
    // 获取经销商
    public static String getSeller = "http://happyleasing.cn/TMZL/listjsonbam061_c.action?operexl=json&page.search=true&nd=时间&page.pageSize=1000&page.curPageNo=1&page.orderBy=&page.order=asc&filters=%7bgs_BADMMC:渠道名称;gs_JPCPID:产品%7d&queryfilters=%7bgs_BADMMC:渠道名称;gs_JPCPID:产品%7d";
    // 获取回租抵押城市和抵押公司
    public static String getLeaseBackCity = "http://happyleasing.cn/TMZL/listjsonbam066_p.action?operexl=json&page.search=false&nd=时间&page.pageSize=1000&page.curPageNo=1&page.orderBy=&page.order=asc&filters=%7bgs_JPCPID:产品%7d";
    // 保险试算
    public static String getInsuranceTrial = "http://happyleasing.cn/TMZL/operinsurancecost_bx.action?BASQLX=个人&BADYCS=上海市&BXZWSL=座位数&BXDSXE=100.0&BACLCL=0&BACLZD=车辆指导价&SJZZXZ=false&SJZZXE=1&CKZZXZ=false&CKZZXE=1&RYZZBJ=false&CSHHXZ=false&BXQCXZ=true&HHXZXE=2000.0&HHXZBJ=false&BLDDPS=false&BLXZCD=国产&BXZDZX=false&BAYYFS=非营运&BACLDW=2吨以下&BACXLX=车型&BASFWY=否&BACLPL=排量&_=时间";
    // 获取所属行业
    public static String getIndustry= "http://happyleasing.cn/TMZL/listjsonbam002_sshy.action?operexl=json&page.search=false&nd=时间&page.pageSize=1000&page.curPageNo=1&page.orderBy=&page.order=asc&filters=%7bgs_BASJDM:行业;gs_BASJMC:0%7d";

}
