package com.tm.wechat.dto.addInfo;

import lombok.Data;


/**
 * Created by HJYang on 2016/10/11.
 */

@Data
public class CustInfo {

    private String applyno;//阿里申请单号
    private String name;//姓名
    private String csrq;//出生日期
    private String hklx;//户口类别  1.本地 2.非本地 3.外省
    private String khxl;//学历 1.本科以上 2.本科 3.大专 4.高中、中专、技校 5.高中以下

    private String gcmd;//购车目的 1.本人自用 2.商用 3.企业自用 4.指定第三方自用
    private String ywfc;//有无房产 1.有 2.没有
    private String sfyjsz;//是否有驾驶证 1.有 2.没有
    private String jszdah;//驾驶证档案号
    private String clsydsf;//车辆使用地省份

    private String clsydcs;//车辆使用地城市
    private String zjlx;//证件类型 0:身份证  1:临时身份证  2:护照  3:台胞证4:港澳居民通行证
    private String zjhm;//证件号码
    private String sjhm;//手机号码
    private String hyzk;//婚姻状况 0:已婚有子女1:已婚无子女2:未婚3:离异

    private String gzdwmc;//工作单位名称
    private String dwdz;//单位地址
    private String sjjzdz;//实际居住地地址
    private String hjszdz;//户籍所在地地址
    private String poxm;//配偶姓名

    private String zjlx_po;//配偶证件类型 0:身份证  1:临时身份证  2:护照  3:台胞证4:港澳居民通行证
    private String zjhm_po;//配偶证件号码
    private String gzdwmc_po;//配偶工作单位名称
    private String dwdz_po;//配偶单位地址
    private String jjlxrxm_1;//紧急联系人1姓名 private String jjlxrhm_1;//紧急联系人1号码

    private String jjlxrhm_1;//紧急联系人2手机号码
    private String ysqrgx_1;//紧急联系人1与申请人关系 0:家人1:亲属
    private String jjlxrxm_2;//紧急联系人2姓名
    private String jjlxrhm_2;//紧急联系人2手机号码
    private String ysqrgx_2;//紧急联系人2与申请人关系 0:家人1:亲属2:朋友3:同事4:其他


    private String khxb;//申请人性别
    private String dwzj;//申请人单位座机
    private String fcszsf;//房产所在省份
    private String fcszcs;//房产所在城市
    private String posjdh;//配偶手机电话
    private String podwdh;//配偶单位电话
    private String fcxxdz;//房产详细地址
    private String idCardFrontImg;//身份证照片正面
    private String idCardBehindImg;//身份证照片反面
    private String driveLicenceFront;//驾驶证照片正面
    private String driveLicenceBehind;//驾驶证照片反面
    private String otherFiles; // 其他文件
}
