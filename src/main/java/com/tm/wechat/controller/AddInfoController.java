package com.tm.wechat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.wechat.config.WechatProperties;
import com.tm.wechat.dto.addInfo.CustInfo;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import com.tm.wechat.dto.resu.Resul;
import com.tm.wechat.service.AddInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by HJYang on 2016/10/11.
 */
@RestController
@RequestMapping("/addInfo")
public class AddInfoController {

    @Autowired
    private AddInfoService addInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WechatProperties wechatProperties;

    @RequestMapping(value = "/addCustInfo", method = RequestMethod.GET)
    public ResponseEntity<Message> addCustInfo(CustInfo custInfo){
        String[] csrq = custInfo.getCsrq().split("-");
        String csrqS = "";
        for (int i = 0; i < csrq.length; i++) {
            csrqS = csrqS + csrq[i];
        }
        String res = addInfoService.addCustInfo("getAliInfo", "admin",
                wechatProperties.getTimestamp(),wechatProperties.getSign(),
                custInfo.getApplyno(),custInfo.getName(),
                csrqS,      custInfo.getHklx(),
                custInfo.getKhxl(),custInfo.getGcmd(),
                custInfo.getYwfc(),custInfo.getSfyjsz(),
                custInfo.getJszdah(),custInfo.getClsydsf(),
                custInfo.getClsydcs(),custInfo.getZjlx(),
                custInfo.getZjhm(),custInfo.getSjhm(),
                custInfo.getHyzk(),custInfo.getGzdwmc(),
                custInfo.getDwdz(),custInfo.getSjjzdz(),
                custInfo.getHjszdz(),custInfo.getPoxm(),
                custInfo.getZjlx_po(),custInfo.getZjhm_po(),
                custInfo.getGzdwmc_po(),custInfo.getDwdz_po(),
                custInfo.getJjlxrxm_1(),custInfo.getJjlxrhm_1(),
                custInfo.getYsqrgx_1(),custInfo.getJjlxrxm_2(),
                custInfo.getJjlxrhm_2(),custInfo.getYsqrgx_2(),
                custInfo.getKhxb(), custInfo.getDwzj(), custInfo.getFcszsf(),
                custInfo.getFcszcs(), custInfo.getPosjdh(), custInfo.getPodwdh(),
                custInfo.getFcxxdz(), custInfo.getIdCardFrontImg() + ";" + custInfo.getIdCardBehindImg(),
                custInfo.getDriveLicenceFront() + ";" + custInfo.getDriveLicenceBehind(), custInfo.getOtherFiles()
                );
        Resul resul = null;
        try {
            resul = objectMapper.readValue(res, Resul.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if("true".equals(resul.getResult().getIsSuccess())){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, resul.getResult().getResultMsg()), HttpStatus.OK);
        }
    }
}