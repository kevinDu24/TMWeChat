package com.tm.wechat.service.sale;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by PengChao on 16/9/1.
 */
@FeignClient(name = "sale", url = "${request.coreServerUrl}")
public interface SaleInterface {
    @RequestMapping(value = "/lytmtjapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryAreaLevel(@RequestParam("userId") String userId, @RequestParam(".url") String url);

    @RequestMapping(value = "/lytmtjapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryStatisticsByDate(@RequestParam(".url")String url,
                                 @RequestParam("userId") String userId,
                                 @RequestParam("type") int type,
                               @RequestParam("userLevel") String userLevel,
                                 @RequestParam("beginTime") String beginTime,
                               @RequestParam("endTime") String endTime,
                                 @RequestParam("queryAll") String queryAll);

    @RequestMapping(value = "/lytmtjapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String queryStatisticsByYearOrMonth(@RequestParam(".url")String url, @RequestParam("userId") String userId, @RequestParam("type") int type,
                                 @RequestParam("userLevel") String userLevel, @RequestParam("year") int year,
                                 @RequestParam("month") int month);

    /**
     * 查询GPS硬件价格
     * @param operator
     * @param sign
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getGpsPrice(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "BACPID") String typeId,
            @RequestParam(value = "BACPMC") String productCase,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);

    /**
     * 查询经销商
     * @param type
     * @param operator
     * @param sign
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getDealers(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "type") Integer type,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);

    /**
     * 查询销售抵押城市
     * @param operator
     * @param sign
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getPledgeCities(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);

    /**
     * 查询还款借记卡开户行
     * @param operator
     * @param sign
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getBanks(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String insuranceCalculate(@RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp,
                              @RequestParam(value = "baclpl") String baclpl,
                              @RequestParam(value = "badycs") String badycs,
                              @RequestParam(value = "bazwsl") String bazwsl,
                              @RequestParam(value = "baszzr") String baszzr,
                              @RequestParam(value = "baclzd") String baclzd,
                              @RequestParam(value = "ryzrxsj") String ryzrxsj,
                              @RequestParam(value = "ryzrxsjxe") String ryzrxsjxe,
                              @RequestParam(value = "ryzrxck") String ryzrxck,
                              @RequestParam(value = "ryzrxckxe") String ryzrxckxe,
                              @RequestParam(value = "babjmp") String babjmp,
                              @RequestParam(value = "bacshh") String bacshh,
                              @RequestParam(value = "badqxe") String badqxe,
                              @RequestParam(value = "cshhxe") String cshhxe,
                              @RequestParam(value = "cshhbjmp") String cshhbjmp,
                              @RequestParam(value = "blddbs") String blddbs,
                              @RequestParam(value = "babxcd") String babxcd,
                              @RequestParam(value = "bazdzx") String bazdzx,
                              @RequestParam(value = "bayyfs") String bayyfs,
                              @RequestParam(value = "bacldw") String bacldw,
                              @RequestParam(value = "bacxlx") String bacxlx,
                              @RequestParam(value = "sfraxb") String sfraxb);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String apply(@RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp,
                 @RequestParam(value = "jbxx_xm") String jbxx_xm,
                              @RequestParam(value = "jbxx_xb") String jbxx_xb,
                              @RequestParam(value = "jbxx_csrq") String jbxx_csrq,
                              @RequestParam(value = "jbxx_zjlx") String jbxx_zjlx,
                              @RequestParam(value = "jbxx_zjhm") String jbxx_zjhm,
                              @RequestParam(value = "jbxx_sjhm") String jbxx_sjhm,
                              @RequestParam(value = "jbxx_zzdh") String jbxx_zzdh,
                              @RequestParam(value = "jbxx_hyzk") String jbxx_hyzk,
                              @RequestParam(value = "jbxx_hklb") String jbxx_hklb,
                              @RequestParam(value = "jbxx_xl") String jbxx_xl,
                              @RequestParam(value = "zyxx_gzdw") String zyxx_gzdw,
                              @RequestParam(value = "zyxx_qyxz") String zyxx_qyxz,
                              @RequestParam(value = "zyxx_sshy") String zyxx_sshy,
                              @RequestParam(value = "zyxx_zw") String zyxx_zw,
                              @RequestParam(value = "zyxx_zc") String zyxx_zc,
                              @RequestParam(value = "zyxx_zznx") String zyxx_zznx,
                              @RequestParam(value = "zyxx_dwszcs") String zyxx_dwszcs,
                              @RequestParam(value = "zyxx_dwdh") String zyxx_dwdh,
                              @RequestParam(value = "zyxx_dwdz") String zyxx_dwdz,
                              @RequestParam(value = "zyxx_shnx") String zyxx_shnx,
                              @RequestParam(value = "dzxx_xjzdrznx") String dzxx_xjzdrznx,
                 @RequestParam(value = "dzxx_xjzzk") String dzxx_xjzzk,
                              @RequestParam(value = "dzxx_xjzcs") String dzxx_xjzcs,
                              @RequestParam(value = "dzxx_xjzdz") String dzxx_xjzdz,
                              @RequestParam(value = "dzxx_fclx") String dzxx_fclx,
                              @RequestParam(value = "dzxx_fcszcs") String dzxx_fcszcs,
                              @RequestParam(value = "dzxx_fcqy") String dzxx_fcqy,
                              @RequestParam(value = "dzxx_fcdyqk") String dzxx_fcdyqk,
                              @RequestParam(value = "dzxx_hjszcs") String dzxx_hjszcs,
                              @RequestParam(value = "dzxx_hjszdz") String dzxx_hjszdz,
                              @RequestParam(value = "jjlxr_lxr1xm") String jjlxr_lxr1xm,
                              @RequestParam(value = "jjlxr_lxr1sj") String jjlxr_lxr1sj,
                              @RequestParam(value = "jjlxr_yczrgx1") String jjlxr_yczrgx1,
                              @RequestParam(value = "jjlxr_lxr2xm") String jjlxr_lxr2xm,
                              @RequestParam(value = "jjlxr_lxr2sj") String jjlxr_lxr2sj,
                              @RequestParam(value = "jjlxr_yczrgx2") String jjlxr_yczrgx2,
                              @RequestParam(value = "po_xm") String po_xm,
                              @RequestParam(value = "po_sjhm") String po_sjhm,
                              @RequestParam(value = "po_zjlx") String po_zjlx,
                              @RequestParam(value = "po_zjhm") String po_zjhm,
                              @RequestParam(value = "po_gzdw") String po_gzdw,
                              @RequestParam(value = "po_zw") String po_zw,
                 @RequestParam(value = "po_dwdh") String po_dwdh,
                              @RequestParam(value = "po_dwdz") String po_dwdz,
                              @RequestParam(value = "po_shnx") String po_shnx,
                              @RequestParam(value = "dbr_yczrgx") String dbr_yczrgx,
                              @RequestParam(value = "dbr_xm") String dbr_xm,
                              @RequestParam(value = "dbr_sjhm") String dbr_sjhm,
                              @RequestParam(value = "dbr_zjlx") String dbr_zjlx,
                              @RequestParam(value = "dbr_zjhm") String dbr_zjhm,
                              @RequestParam(value = "dbr_hyzk") String dbr_hyzk,
                              @RequestParam(value = "dbr_hklb") String dbr_hklb,
                              @RequestParam(value = "dbr_dwmc") String dbr_dwmc,
                              @RequestParam(value = "dbr_dwdh") String dbr_dwdh,
                              @RequestParam(value = "gsr_yczrgx") String gsr_yczrgx,
                              @RequestParam(value = "gsr_xm") String gsr_xm,
                              @RequestParam(value = "gsr_sjhm") String gsr_sjhm,
                              @RequestParam(value = "gsr_zjlx") String gsr_zjlx,
                              @RequestParam(value = "gsr_zjhm") String gsr_zjhm,
                              @RequestParam(value = "gsr_hyzk") String gsr_hyzk,
                              @RequestParam(value = "gsr_hklb") String gsr_hklb,
                              @RequestParam(value = "gsr_dwmc") String gsr_dwmc,
                              @RequestParam(value = "gsr_dwdh") String gsr_dwdh,
                 @RequestParam(value = "rzxx_cpdlid") String rzxx_cpdlid,
                              @RequestParam(value = "rzxx_cpxlid") String rzxx_cpxlid,
                              @RequestParam(value = "rzxx_cpfaid") String rzxx_cpfaid,
                              @RequestParam(value = "rzxx_zzsid") String rzxx_zzsid,
                              @RequestParam(value = "rzxx_ppid") String rzxx_ppid,
                              @RequestParam(value = "rzxx_cxid") String rzxx_cxid,
                              @RequestParam(value = "rzxx_clzdj") String rzxx_clzdj,
                              @RequestParam(value = "rzxx_baclpl") String rzxx_baclpl,
                              @RequestParam(value = "rzxx_bazwsl") String rzxx_bazwsl,
                              @RequestParam(value = "xxjl_xm") String xxjl_xm,
                              @RequestParam(value = "fp_sczlxm") String fp_sczlxm,
                              @RequestParam(value = "rzxx_clxsj") String rzxx_clxsj,
                              @RequestParam(value = "rzxx_gpsyjfy") String rzxx_gpsyjfy,
                              @RequestParam(value = "rzxx_rzqx") String rzxx_rzqx,
                              @RequestParam(value = "rzxx_sfyy") String rzxx_sfyy,
                              @RequestParam(value = "rzxx_sfryb") String rzxx_sfryb,
                              @RequestParam(value = "rzxx_sfrbx") String rzxx_sfrbx,
                              @RequestParam(value = "rzxx_sfraxb") String rzxx_sfraxb,
                              @RequestParam(value = "rzxx_axbjg") String rzxx_axbjg,
                              @RequestParam(value = "rzxx_gzs") String rzxx_gzs,
                              @RequestParam(value = "rzxx_yb") String rzxx_yb,
                              @RequestParam(value = "rzxx_jqx") String rzxx_jqx,
                              @RequestParam(value = "rzxx_ccs") String rzxx_ccs,
                              @RequestParam(value = "rzxx_syx") String rzxx_syx,
                              @RequestParam(value = "rzxx_sfbl") String rzxx_sfbl,
                 @RequestParam(value = "rzxx_sfje") String rzxx_sfje,
                              @RequestParam(value = "rzxx_wfbl") String rzxx_wfbl,
                              @RequestParam(value = "rzxx_wfje") String rzxx_wfje,
                              @RequestParam(value = "rzxx_rzje") String rzxx_rzje,
                              @RequestParam(value = "rzxx_sxfsffq") String rzxx_sxfsffq,
                              @RequestParam(value = "rzxx_sxffl") String rzxx_sxffl,
                              @RequestParam(value = "rzxx_sxf") String rzxx_sxf,
                              @RequestParam(value = "rzxx_bzjl") String rzxx_bzjl,
                              @RequestParam(value = "rzxx_xsjlid") String rzxx_xsjlid,
                              @RequestParam(value = "rzxx_sczlid") String rzxx_sczlid,
                              @RequestParam(value = "rzxx_hkjjkkhh") String rzxx_hkjjkkhh,
                              @RequestParam(value = "rzxx_hkjjkzh") String rzxx_hkjjkzh,
                              @RequestParam(value = "rzxx_hkjjkhm") String rzxx_hkjjkhm,
                              @RequestParam(value = "rzxx_zxsfhz") String rzxx_zxsfhz,
                              @RequestParam(value = "rzxx_sqbz") String rzxx_sqbz,
                              @RequestParam(value = "fj_sfzlj") String fj_sfzlj,
                              @RequestParam(value = "fj_jsz") String fj_jsz,
                              @RequestParam(value = "fj_jhz") String fj_jhz,
                              @RequestParam(value = "fj_hkb") String fj_hkb,
                              @RequestParam(value = "fj_srzm") String fj_srzm,
                              @RequestParam(value = "fj_jgzkyhls") String fj_jgzkyhls,
                 @RequestParam(value = "fj_fclzl") String fj_fclzl,
                              @RequestParam(value = "fj_sqb") String fj_sqb,
                              @RequestParam(value = "fj_dbrsfz") String fj_dbrsfz,
                 @RequestParam(value = "jbxx_gcyt") String jbxx_gcyt,
                                @RequestParam(value = "jbxx_jszyw") String jbxx_jszyw,
                                @RequestParam(value = "jbxx_jszxm") String jbxx_jszxm,
                                @RequestParam(value = "jbxx_jszhm") String jbxx_jszhm,
                                @RequestParam(value = "jbxx_jsdah") String jbxx_jsdah,
                                @RequestParam(value = "CLSYSF") String CLSYSF,
                                @RequestParam(value = "jbxx_ywfc") String jbxx_ywfc,
                                @RequestParam(value = "jbxx_clsycs") String jbxx_clsycs,
                                @RequestParam(value = "jbxx_sjycr") String jbxx_sjycr,
                                 @RequestParam(value = "jbxx_sjycrsj") String jbxx_sjycrsj,
                                 @RequestParam(value = "jjlxr_lxr3xm") String jjlxr_lxr3xm,
                                 @RequestParam(value = "jjlxr_lxr3sj") String jjlxr_lxr3sj,
                                 @RequestParam(value = "jjlxr_yczrgx3") String jjlxr_yczrgx3,
                                 @RequestParam(value = "jjlxr_lxrdz1") String jjlxr_lxrdz1,
                                 @RequestParam(value = "jjlxr_lxrdz2") String jjlxr_lxrdz2,
                                 @RequestParam(value = "dbr_xb") String dbr_xb,
                                 @RequestParam(value = "dbr_zw") String dbr_zw,
                                 @RequestParam(value = "dbr_xjudnx") String dbr_xjudnx,
                                 @RequestParam(value = "dbr_xjzzk") String dbr_xjzzk,
                                 @RequestParam(value = "BAZZNX") String BAZZNX,
                                 @RequestParam(value = "BAZCBX") String BAZCBX,
                                 @RequestParam(value = "dbr_xjzdz") String dbr_xjzdz,
                                 @RequestParam(value = "gsr_xb") String gsr_xb,
                                 @RequestParam(value = "gsr_zw") String gsr_zw,
                                 @RequestParam(value = "gsr_xjudnx") String gsr_xjudnx,
                                 @RequestParam(value = "gsr_xjzzk") String gsr_xjzzk,
                                 @RequestParam(value = "gsr_xjzdz") String gsr_xjzdz,
                                 @RequestParam(value = "rzxx_hzldycs") String rzxx_hzldycs,
                                 @RequestParam(value = "zfsqfs") String zfsqfs,
                                 @RequestParam(value = "baybcp") String baybcp,
                                 @RequestParam(value = "baybfy") String baybfy,
                                 @RequestParam(value = "rzxx_dszzrxxe") String rzxx_dszzrxxe,
                                 @RequestParam(value = "rzxx_csrysj") String rzxx_csrysj,
                                 @RequestParam(value = "rzxx_csryck") String rzxx_csryck,
                                 @RequestParam(value = "ryzrxckxe") String ryzrxckxe,
                                 @RequestParam(value = "rzxx_csryck") String rzxx_bjmp,
                                 @RequestParam(value = "rzxx_cshhx") String rzxx_cshhx,
                                 @RequestParam(value = "cshhxe") String cshhxe,
                                 @RequestParam(value = "rzxx_csbjmp") String rzxx_csbjmp,
                                 @RequestParam(value = "rzxx_bldd") String rzxx_bldd,
                                 @RequestParam(value = "rzxx_zdzxx") String rzxx_zdzxx,
                                 @RequestParam(value = "babxcd") String babxcd,
                                 @RequestParam(value = "rzxx_bzj") String rzxx_bzj,
                                 @RequestParam(value = "rzxx_tzze") String rzxx_tzze,
                                 @RequestParam(value = "fj_dbrhkb") String fj_dbrhkb,
                                 @RequestParam(value = "fj_dbrfclzl") String fj_dbrfclzl,
                                 @RequestParam(value = "fj_gsrsfz") String fj_gsrsfz,
                                 @RequestParam(value = "fj_gsrjsz") String fj_gsrjsz,
                                 @RequestParam(value = "fj_gsryhls") String fj_gsryhls,
                                 @RequestParam(value = "fj_gsrfclzl") String fj_gsrfclzl,
                                 @RequestParam(value = "dbr_xjzcs") String dbr_xjzcs,
                                 @RequestParam(value = "gsr_xjzcs") String gsr_xjzcs,
                                 @RequestParam(value = "bacxlx") String bacxlx,
                                 @RequestParam(value = "ryzrxsjxe") String ryzrxsjxe,
                                 @RequestParam(value = "BABZDW") String BABZDW,
                                 @RequestParam(value = "xfwsje") String xfwsje,
                                 @RequestParam(value = "babzdwje") String babzdwje
                 );



    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getWarranty(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getWarrantyPlan(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp,
            @RequestParam(value = "BAYBCP") String id);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getChildLevel(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp,
            @RequestParam(value = "levelId") String levelId);

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getPurchaseTax(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "BACPID") String productId,
            @RequestParam(value = "BACPMC") String productName,
            @RequestParam(value = "BACLJG") String sellingPrice,
            @RequestParam(value = "BASFRZ") String isFinance,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp
    );


    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAccidentProtection(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "BABZDW") String grade,
            @RequestParam(value = "BARZQX") String period,
            @RequestParam(value = "BABZFY") String deposit,
            @RequestParam(value = "BASFJE") String downPay,
            @RequestParam(value = "BACLJG") String sellingPrice,
            @RequestParam(value = "BASFBZ") String isFinance,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp
    );

    @RequestMapping(value = "/lywxapi.htm!", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getXfws(
            @RequestParam(value = ".url") String url,
            @RequestParam(value = "SFXFWS") String isFinance,
            @RequestParam(value = "BARZQX") String period,
            @RequestParam(value = "BACLJG") String sellingPrice,
            @RequestParam(value = "operator")  String operator,
            @RequestParam(value = "sign") String sign,
            @RequestParam(value = "timestamp") String timestamp
    );
}
