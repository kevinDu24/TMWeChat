package com.tm.wechat.service.sale;

import com.google.common.collect.Maps;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.sale.ApplyRecDto;
import com.tm.wechat.dto.sale.InsuranceCalculateRecDto;
import com.tm.wechat.service.JsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by pengchao on 2016/10/28.
 */
@Service
public class SaleService {
    @Autowired
    private JsonDecoder jsonDecoder;

    @Autowired
    private SaleInterface saleInterface;

    @Autowired
    private SaleYCInterface saleYCInterface;

    @Autowired
    private WechatProperties wechatProperties;
    /**
     * 查询GPS硬件价格
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getGpsPrice(String userName, String typeId, String productCase){
        String result = saleInterface.getGpsPrice("queryGpsJg", typeId, productCase, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
//        return jsonDecoder.jsonDecoder("queryGpsJg", result);
        return jsonDecoder.singlePropertyDecoder(result, "gpsList");
    }

    /**
     * 查询经销商
     * @param userName
     * @param type
     * @return
     */
    public ResponseEntity<Message> getDealers(Integer type,String userName){
        String result = saleInterface.getDealers("queryFpScList", type, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
//        return jsonDecoder.jsonDecoder("queryFpScList", result);
        return jsonDecoder.singlePropertyDecoder(result, "fpScList");
    }

    /**
     * 获取回租抵押城市
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getPledgeCities(String userName){
        String result = saleInterface.getPledgeCities("querySpdycs", userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
//        return jsonDecoder.jsonDecoder("querySpdycs", result);
        return jsonDecoder.singlePropertyDecoder(result, "spdycsList");
    }

    /**
     * 获取还款借记卡开户行
     * @param username
     * @return
     */
    public ResponseEntity<Message> getBanks(String username){
        String result = saleInterface.getBanks("queryRepaymentBankList", username, wechatProperties.getSign(), wechatProperties.getTimestamp());
//        return jsonDecoder.jsonDecoder("queryRepaymentBankList", result);
        return jsonDecoder.singlePropertyDecoder(result, "repaymentBankList");
    }

    /**
     * 销售保险测算
     * @param insuranceCalculateRecDto
     * @param userName
     * @return
     */
    public ResponseEntity<Message> insuranceCalculate(InsuranceCalculateRecDto insuranceCalculateRecDto, String userName){
        String result = saleInterface.insuranceCalculate("queryInsuranceCalculation", userName, wechatProperties.getSign(), wechatProperties.getTimestamp(),
                insuranceCalculateRecDto.getBaclpl(), insuranceCalculateRecDto.getBadycs(),
                insuranceCalculateRecDto.getBazwsl(), insuranceCalculateRecDto.getBaszzr(),
                insuranceCalculateRecDto.getBaclzd(), insuranceCalculateRecDto.getRyzrxsj(),
                insuranceCalculateRecDto.getRyzrxsjxe(), insuranceCalculateRecDto.getRyzrxck(),
                insuranceCalculateRecDto.getRyzrxckxe(), insuranceCalculateRecDto.getBabjmp(),
                insuranceCalculateRecDto.getBacshh(), insuranceCalculateRecDto.getBadqxe(),
                insuranceCalculateRecDto.getCshhxe(), insuranceCalculateRecDto.getCshhbjmp(),
                insuranceCalculateRecDto.getBlddbs(), insuranceCalculateRecDto.getBabxcd(),
                insuranceCalculateRecDto.getBazdzx(), insuranceCalculateRecDto.getBayyfs(),
                insuranceCalculateRecDto.getBacldw(), insuranceCalculateRecDto.getBacxlx(),
                insuranceCalculateRecDto.getSfraxb());
//        return jsonDecoder.jsonDecoder("queryInsuranceCalculation", result);
        return jsonDecoder.singlePropertyDecoder(result, "insuranceInfo");
    }

    /**
     * 销售申请提交
     * @param applyRecDto
     * @param userName
     * @return
     */
    public ResponseEntity<Message> apply(ApplyRecDto applyRecDto, String userName){
        String result = saleInterface.apply("saveXsInfo", userName, wechatProperties.getSign(), wechatProperties.getTimestamp(),
                applyRecDto.getJbxx_xm(), applyRecDto.getJbxx_xb(),
                applyRecDto.getJbxx_csrq(), applyRecDto.getJbxx_zjlx(),
                applyRecDto.getJbxx_zjhm(), applyRecDto.getJbxx_sjhm(),
                applyRecDto.getJbxx_zzdh(), applyRecDto.getJbxx_hyzk(),
                applyRecDto.getJbxx_hklb(), applyRecDto.getJbxx_xl(),
                applyRecDto.getZyxx_gzdw(), applyRecDto.getZyxx_qyxz(),
                applyRecDto.getZyxx_sshy(), applyRecDto.getZyxx_zw(),
                applyRecDto.getZyxx_zc(), applyRecDto.getZyxx_zznx(),
                applyRecDto.getZyxx_dwszcs(), applyRecDto.getZyxx_dwdh(),
                applyRecDto.getZyxx_dwdz(), applyRecDto.getZyxx_shnx(),
                applyRecDto.getDzxx_xjzdrznx(), applyRecDto.getDzxx_xjzzk(),
                applyRecDto.getDzxx_xjzcs(), applyRecDto.getDzxx_xjzdz(),
                applyRecDto.getDzxx_fclx(), applyRecDto.getDzxx_fcszcs(),
                applyRecDto.getDzxx_fcqy(), applyRecDto.getDzxx_fcdyqk(),
                applyRecDto.getDzxx_hjszcs(), applyRecDto.getDzxx_hjszdz(),
                applyRecDto.getJjlxr_lxr1xm(), applyRecDto.getJjlxr_lxr1sj(),
                applyRecDto.getJjlxr_yczrgx1(), applyRecDto.getJjlxr_lxr2xm(),
                applyRecDto.getJjlxr_lxr2sj(), applyRecDto.getJjlxr_yczrgx2(),
                applyRecDto.getPo_xm(), applyRecDto.getPo_sjhm(),
                applyRecDto.getPo_zjlx(), applyRecDto.getPo_zjhm(),
                applyRecDto.getPo_gzdw(), applyRecDto.getPo_zw(),
                applyRecDto.getPo_dwdh(), applyRecDto.getPo_dwdz(),
                applyRecDto.getPo_shnx(), applyRecDto.getDbr_yczrgx(),
                applyRecDto.getDbr_xm(), applyRecDto.getDbr_sjhm(),
                applyRecDto.getDbr_zjlx(), applyRecDto.getDbr_zjhm(),
                applyRecDto.getDbr_hyzk(), applyRecDto.getDbr_hklb(),
                applyRecDto.getDbr_dwmc(), applyRecDto.getDbr_dwdh(),
                applyRecDto.getGsr_yczrgx(), applyRecDto.getGsr_xm(),
                applyRecDto.getGsr_sjhm(), applyRecDto.getGsr_zjlx(),
                applyRecDto.getGsr_zjhm(), applyRecDto.getGsr_hyzk(),
                applyRecDto.getGsr_hklb(), applyRecDto.getGsr_dwmc(),
                applyRecDto.getGsr_dwdh(), applyRecDto.getRzxx_cpdlid(),
                applyRecDto.getRzxx_cpxlid(), applyRecDto.getRzxx_cpfaid(),
                applyRecDto.getRzxx_zzsid(), applyRecDto.getRzxx_ppid(),
                applyRecDto.getRzxx_cxid(), applyRecDto.getRzxx_clzdj(),
                applyRecDto.getRzxx_baclpl(),applyRecDto.getRzxx_bazwsl(),
                applyRecDto.getXxjl_xm(),applyRecDto.getFp_sczlxm(),
                applyRecDto.getRzxx_clxsj(), applyRecDto.getRzxx_gpsyjfy(),
                applyRecDto.getRzxx_rzqx(), applyRecDto.getRzxx_sfyy(),
                applyRecDto.getRzxx_sfryb(), applyRecDto.getRzxx_sfrbx(),
                applyRecDto.getRzxx_sfraxb(), applyRecDto.getRzxx_axbjg(),
                applyRecDto.getRzxx_gzs(), applyRecDto.getRzxx_yb(),
                applyRecDto.getRzxx_jqx(), applyRecDto.getRzxx_ccs(),
                applyRecDto.getRzxx_syx(), applyRecDto.getRzxx_sfbl(),
                applyRecDto.getRzxx_sfje(), applyRecDto.getRzxx_wfbl(),
                applyRecDto.getRzxx_wfje(), applyRecDto.getRzxx_rzje(),
                applyRecDto.getRzxx_sxfsffq(), applyRecDto.getRzxx_sxffl(),
                applyRecDto.getRzxx_sxf(), applyRecDto.getRzxx_bzjl(),
                applyRecDto.getRzxx_xsjlid(), applyRecDto.getRzxx_sczlid(),
                applyRecDto.getRzxx_hkjjkkhh(), applyRecDto.getRzxx_hkjjkzh(),
                applyRecDto.getRzxx_hkjjkhm(), applyRecDto.getRzxx_zxsfhz(),
                applyRecDto.getRzxx_sqbz(), applyRecDto.getFj_sfzlj(),
                applyRecDto.getFj_jsz(), applyRecDto.getFj_jhz(),
                applyRecDto.getFj_hkb(),applyRecDto.getFj_srzm(),
                applyRecDto.getFj_jgzkyhls(), applyRecDto.getFj_fclzl(),
                applyRecDto.getFj_sqb(), applyRecDto.getFj_dbrsfz(),
                applyRecDto.getJbxx_gcyt(), applyRecDto.getJbxx_jszyw(),
                applyRecDto.getJbxx_jszxm(), applyRecDto.getJbxx_jszhm(),
                applyRecDto.getJbxx_jsdah(), applyRecDto.getClsysf(),
                applyRecDto.getJbxx_ywfc(), applyRecDto.getJbxx_clsycs(),
                applyRecDto.getJbxx_sjycr(), applyRecDto.getJbxx_sjycrsj(),
                applyRecDto.getJjlxr_lxr3xm(), applyRecDto.getJjlxr_lxr3sj(),
                applyRecDto.getJjlxr_yczrgx3(), applyRecDto.getJjlxr_lxrdz1(),
                applyRecDto.getJjlxr_lxrdz2(), applyRecDto.getDbr_xb(),
                applyRecDto.getDbr_zw(), applyRecDto.getDbr_xjudnx(),
                applyRecDto.getDbr_xjzzk(), applyRecDto.getBazznx(),
                applyRecDto.getBazcbx(), applyRecDto.getDbr_xjzdz(),
                applyRecDto.getGsr_xb(), applyRecDto.getGsr_zw(),
                applyRecDto.getGsr_xjudnx(), applyRecDto.getGsr_xjzzk(),
                applyRecDto.getGsr_xjzdz(), applyRecDto.getRzxx_hzldycs(),
                applyRecDto.getZfsqfs(), applyRecDto.getBaybcp(),
                applyRecDto.getBaybfy(), applyRecDto.getRzxx_dszzrxxe(),
                applyRecDto.getRzxx_csrysj(), applyRecDto.getRzxx_csryck(),
                applyRecDto.getRyzrxckxe(), applyRecDto.getRzxx_bjmp(),
                applyRecDto.getRzxx_cshhx(), applyRecDto.getCshhxe(),
                applyRecDto.getRzxx_csbjmp(), applyRecDto.getRzxx_bldd(),
                applyRecDto.getRzxx_zdzxx(), applyRecDto.getBabxcd(),
                applyRecDto.getRzxx_bzj(), applyRecDto.getRzxx_tzze(),
                applyRecDto.getFj_dbrhkb(), applyRecDto.getFj_dbrfclzl(),
                applyRecDto.getFj_gsrsfz(), applyRecDto.getFj_gsrjsz(),
                applyRecDto.getFj_gsryhls(),applyRecDto.getFj_gsrfclzl(),
                applyRecDto.getDbr_xjzcs(), applyRecDto.getGsr_xjzcs(),
                applyRecDto.getBacxlx(), applyRecDto.getRyzrxsjxe(),
                applyRecDto.getBabzdw(), applyRecDto.getXfwsje(),
                applyRecDto.getBabzdwje()
                );
//        return jsonDecoder.jsonDecoder("saveXsInfo", result);
        return jsonDecoder.singlePropertyDecoder(result, "saveXsInfo");
    }

    public ResponseEntity<Message> getWarranty(String userName){
        String result = saleInterface.getWarranty("getQueryYbcp", userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
//        return jsonDecoder.jsonDecoder("getQueryYbcp", result);
        return jsonDecoder.singlePropertyDecoder(result, "getQueryYbcpList");
    }

    public ResponseEntity<Message> getWarrantyPlan(String id, String userName){
        String result = saleInterface.getWarrantyPlan("getQueryYbfy", userName, wechatProperties.getSign(), wechatProperties.getTimestamp(), id);
//        return jsonDecoder.jsonDecoder("getYbfyList", result);
        return jsonDecoder.singlePropertyDecoder(result, "getYbfyList");
    }

    /**
     * 获取购置税
     * @param productId
     * @param productName
     * @param sellingPrice
     * @param isFinance
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getPurchaseTax(String productId, String productName, String sellingPrice, String isFinance, String userName){
        String result = saleInterface.getPurchaseTax("queryGzs", productId, productName, sellingPrice, isFinance, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
        return jsonDecoder.singlePropertyDecoder(result, "price");
    }

    /**
     * 获取人身意外保障服务
     * @param grade
     * @param period
     * @param deposit
     * @param downPay
     * @param sellingPrice
     * @param isFinance
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getAccidentProtection(String grade, String period, String deposit, String downPay, String sellingPrice, String isFinance, String userName){
        String result = saleInterface.getAccidentProtection("queryYwfw", grade, period, deposit, downPay, sellingPrice,isFinance, userName,wechatProperties.getSign(), wechatProperties.getTimestamp());
        return jsonDecoder.singlePropertyDecoder(result, "price");
    }

    /**
     *获取先锋卫士
     * @param isFinance
     * @param period
     * @param sellingPrice
     * @param userName
     * @return
     */
    public ResponseEntity<Message> getXfws(String isFinance, String period, String sellingPrice, String userName){
        String result = saleInterface.getXfws("queryXfws", isFinance, period, sellingPrice, userName, wechatProperties.getSign(), wechatProperties.getTimestamp());
        return jsonDecoder.singlePropertyDecoder(result, "price");
    }

    public ResponseEntity<Message> getChildLevel(String headerParam, String levelId, String userName){
        String result;
        if("taimeng".equals(JsonPath.from(headerParam).get("systemflag"))){
            result = saleInterface.getChildLevel("getXiaJiQuYu", userName, wechatProperties.getSign(), wechatProperties.getTimestamp(), levelId);
        }else {
            result = saleYCInterface.getChildLevel("getXiaJiQuYu", userName, wechatProperties.getSign(), wechatProperties.getTimestamp(), levelId);
        }
        Map map = Maps.newHashMap();
        map.put("areaList", "getNextAreaReList.getNextAreaList");
        map.put("areaName", "getNextAreaList.areaName");
        map.put("levelId", "getNextAreaList.levelId");
        map.put("sjjgid", "getNextAreaList.sjjgid");
        map.put("qyjb", "getNextAreaList.qyjb");
        return jsonDecoder.multiPropertiesDecoder(result, map);
    }
}
