package com.tm.wechat.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by HJYang on 2016/10/11.
 */

//@FeignClient(name = "addCustInfo", url = "http://116.228.224.58:8077/TMZLTEST/app")
@FeignClient(name = "addCustInfo", url = "${request.coreServerUrl}")
public interface AddInfoService {
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addCustInfo(
            @RequestParam(value = ".url", required = false) String url,//.url：getAliInfo
            @RequestParam(value = "operator", required = false) String operator,//用户名：admin
            @RequestParam(value = "timestamp", required = false) String timestamp,//时间戳：wechatProperties.getTimestamp()
            @RequestParam(value = "sign", required = false) String sign,//签名：wechatProperties.getSign()

            @RequestParam(value = "applyno", required = false) String applyno,//阿里申请单号
            @RequestParam(value = "name", required = false) String name,//姓名
            @RequestParam(value = "csrq", required = false) String csrq,//出生日期
            @RequestParam(value = "hklx", required = false) String hklx,//户口类别  1.本地 2.非本地 3.外省
            @RequestParam(value = "khxl", required = false) String khxl,//学历 1.本科以上 2.本科 3.大专 4.高中、中专、技校 5.高中以下

            @RequestParam(value = "gcmd", required = false) String gcmd,//购车目的 1.本人自用 2.商用 3.企业自用 4.指定第三方自用
            @RequestParam(value = "ywfc", required = false) String ywfc,//有无房产 1.有 2.没有
            @RequestParam(value = "sfyjsz", required = false) String sfyjsz,//是否有驾驶证 1.有 2.没有
            @RequestParam(value = "jszdah", required = false) String jszdah,//驾驶证档案号
            @RequestParam(value = "clsydsf", required = false) String clsydsf,//车辆使用地省份

            @RequestParam(value = "clsydcs", required = false) String clsydcs,//车辆使用地城市
            @RequestParam(value = "zjlx", required = false) String zjlx,//证件类型 0:身份证  1:临时身份证  2:护照  3:台胞证4:港澳居民通行证
            @RequestParam(value = "zjhm", required = false) String zjhm,//证件号码
            @RequestParam(value = "sjhm", required = false) String sjhm,//手机号码
            @RequestParam(value = "hyzk", required = false) String hyzk,//婚姻状况 0:已婚有子女1:已婚无子女2:未婚3:离异

            @RequestParam(value = "gzdwmc", required = false) String gzdwmc,//工作单位名称
            @RequestParam(value = "dwdz", required = false) String dwdz,//单位地址
            @RequestParam(value = "sjjzdz", required = false) String sjjzdz,//实际居住地地址
            @RequestParam(value = "hjszdz", required = false) String hjszdz,//户籍所在地地址
            @RequestParam(value = "poxm", required = false) String poxm,//配偶姓名

            @RequestParam(value = "zjlx_po", required = false) String zjlx_po,//配偶证件类型 0:身份证  1:临时身份证  2:护照  3:台胞证4:港澳居民通行证
            @RequestParam(value = "zjhm_po", required = false) String zjhm_po,//配偶证件号码
            @RequestParam(value = "gzdwmc_po", required = false) String gzdwmc_po,//配偶工作单位名称
            @RequestParam(value = "dwdz_po", required = false) String dwdz_po,//配偶单位地址
            @RequestParam(value = "jjlxrxm_1", required = false) String jjlxrxm_1,//紧急联系人1姓名

            @RequestParam(value = "jjlxrhm_1", required = false) String jjlxrhm_1,//紧急联系人1号码
            @RequestParam(value = "ysqrgx_1", required = false) String ysqrgx_1,//紧急联系人1与申请人关系 0:家人1:亲属
            @RequestParam(value = "jjlxrxm_2", required = false) String jjlxrxm_2,//紧急联系人2姓名
            @RequestParam(value = "jjlxrhm_2", required = false) String jjlxrhm_2,//紧急联系人2手机号码
            @RequestParam(value = "ysqrgx_2", required = false) String ysqrgx_2,//紧急联系人2与申请人关系 0:家人1:亲属2:朋友3:同事4:其他

            @RequestParam(value = "khxb", required = false) String khxb,//申请人性别
            @RequestParam(value = "dwzj", required = false) String dwzj,//申请人单位座机
            @RequestParam(value = "fcszsf", required = false) String fcszsf,//房产所在省份
            @RequestParam(value = "fcszcs", required = false) String fcszcs,//房产所在城市
            @RequestParam(value = "posjdh", required = false) String posjdh,//配偶手机电话
            @RequestParam(value = "podwdh", required = false) String podwdh,//配偶单位电话
            @RequestParam(value = "fcxxdz", required = false) String fcxxdz,//房产详细地址
            @RequestParam(value = "idcardphoto", required = false) String idcardphoto,//身份证照片
            @RequestParam(value = "driveLicencePhoto", required = false) String driveLicencePhoto,//驾驶证照片
            @RequestParam(value = "otherFiles", required = false) String otherFiles//其他文件
    );
}
