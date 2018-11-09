package com.tm.wechat.service.financeProduct.yc;

import com.tm.wechat.dao.yc.CarBrandRepository;
import com.tm.wechat.domain.yc.CarBrand;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by HJYang on 2016/12/14.
 */
@Service
public class FinanceKindService {
    @Autowired
    private CarBrandRepository carBrandRepository;

    /**
     * 获取融资方案(非核心服务器)
     * @return
     */
    public ResponseEntity<Message> getFinancePlans(){
        List<CarBrand> carBrands = carBrandRepository.findAll();
        carBrands.forEach(carBrand -> {
            carBrand.getFinanceKinds().forEach(financeKind -> {
                financeKind.setCarBrand(null);
                financeKind.setRate(null);
                financeKind.setMaxFinance(null);
                financeKind.setMinFinance(null);
            });
        });
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, carBrands), HttpStatus.OK);
    }
}
