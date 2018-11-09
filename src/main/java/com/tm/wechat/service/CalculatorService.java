package com.tm.wechat.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.jayway.restassured.path.json.JsonPath;
import com.tm.wechat.dao.CalculatorRecordRepository;
import com.tm.wechat.dao.CarTypeRepository;
import com.tm.wechat.dao.FinanceProductRepository;
import com.tm.wechat.domain.CalculatorRecord;
import com.tm.wechat.domain.CarType;
import com.tm.wechat.domain.FinanceProduct;
import com.tm.wechat.dto.calculator.CalculatorDto;
import com.tm.wechat.dto.calculator.CalculatorRecDto;
import com.tm.wechat.dto.calculator.CalculatorRecordDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.apache.poi.ss.formula.functions.FinanceLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/9/13.
 */
@Service
public class CalculatorService {

    @Autowired
    private FinanceProductRepository financeProductRepository;

    @Autowired
    private CalculatorRecordRepository calculatorRecordRepository;

    @Autowired
    private Gson gson;

    @Autowired
    private CarTypeRepository carTypeRepository;

    /**
     * 太盟宝计算器
     * @param calculatorRecDto
     * @return
     */
    public ResponseEntity<Message> calculator(CalculatorRecDto calculatorRecDto, String userName, String headerParam){
        FinanceProduct financeProduct = financeProductRepository.findOne(calculatorRecDto.getFinanceProductId());
        if(financeProduct.getMinPeriod() > calculatorRecDto.getFinancePeriod()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限过短"), HttpStatus.OK);
        }
        if(financeProduct.getMaxPeriod() < calculatorRecDto.getFinancePeriod()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限过长"), HttpStatus.OK);
        }
        // 明白融产品
        if(null != financeProduct.getDepositRate()){
            return clearFinanceCalculator(financeProduct, calculatorRecDto, userName, headerParam);
        }
        // 包牌融产品
        if(financeProduct.getDownPay() > 1){
            return allIncludeFinanceCalculator(financeProduct, calculatorRecDto, userName, headerParam);
        }
        // 普通融资产品
        return commonCalculator(financeProduct, calculatorRecDto, userName, headerParam);
    }

    /**
     * 普通融资产品计算器
     * @param financeProduct
     * @param calculatorRecDto
     * @return
     */
    public ResponseEntity<Message> commonCalculator(FinanceProduct financeProduct, CalculatorRecDto calculatorRecDto, String userName, String headerParam){
        if(financeProduct.getDownPay() > calculatorRecDto.getDownPay()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付比例过低"), HttpStatus.OK);
        }
        if(calculatorRecDto.getDownPay() > 1 || calculatorRecDto.getDownPay() < 0){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付比例错误"), HttpStatus.OK);
        }
        Double financeAmount = calculatorRecDto.getSellingPrice() + calculatorRecDto.getGpsPrice() + calculatorRecDto.getOtherPrice() - calculatorRecDto.getDownPay() * calculatorRecDto.getSellingPrice();
        if(financeProduct.getMaxFinance().doubleValue() > 0 && financeProduct.getMaxFinance().compareTo(financeAmount) == -1){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "超过融资上限"), HttpStatus.OK);
        }
        String rate = financeProduct.getRate();
        if(financeProduct.getRate().split(",").length > 1){
            // 自贸融明白产品,手续费不分期
            return FTAFinanceCalculator(financeProduct, calculatorRecDto, userName, headerParam);
        }
        Double monthPay = Math.abs(FinanceLib.pmt(Double.parseDouble(rate) / 12, calculatorRecDto.getFinancePeriod(), financeAmount, 0, false));
        Double dayPay = monthPay / 30;
        Double downPay = calculatorRecDto.getSellingPrice() * calculatorRecDto.getDownPay();
        Double totalInterest = monthPay * calculatorRecDto.getFinancePeriod() - financeAmount;
//        Double totalInterest = financeAmount * (Double.parseDouble(financeProduct.getRate()) / 12) * calculatorRecDto.getFinancePeriod();
        Double yearInterest = totalInterest / (calculatorRecDto.getFinancePeriod() / 12);
        Double yearEarnings = financeAmount * 0.08;
        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, null,
                totalInterest, yearInterest, yearEarnings);

        saveCalculatorRecord(financeProduct, calculatorRecDto, calculatorDto, userName, headerParam);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorDto), HttpStatus.OK);
    }

    /**
     * 自贸融明白计算器(手续费一次性收取)
     * @param financeProduct
     * @param calculatorRecDto
     * @return
     */
    public ResponseEntity<Message> FTAFinanceCalculator(FinanceProduct financeProduct, CalculatorRecDto calculatorRecDto,
                                                        String userName, String headerParam){
        if(calculatorRecDto.getFinancePeriod().intValue() % 12 != 0){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限应为12的整数倍"), HttpStatus.OK);
        }
        String rate = getMutativeRate(calculatorRecDto.getFinancePeriod(), financeProduct.getRate().split(";")[0]);
        Double financeAmount = calculatorRecDto.getSellingPrice()+ calculatorRecDto.getGpsPrice() + calculatorRecDto.getOtherPrice() - calculatorRecDto.getDownPay() * calculatorRecDto.getSellingPrice();
        Double monthPay = financeAmount / calculatorRecDto.getFinancePeriod();
        Double dayPay = monthPay / 30;
        Double downPay = calculatorRecDto.getSellingPrice() * calculatorRecDto.getDownPay();
        Double totalInterest = financeAmount * Double.parseDouble(rate);
        Double yearInterest = totalInterest / (calculatorRecDto.getFinancePeriod() / 12);
        Double yearEarnings = financeAmount * 0.08;
        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, null,
                totalInterest, yearInterest, yearEarnings);
        saveCalculatorRecord(financeProduct, calculatorRecDto, calculatorDto, userName, headerParam);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorDto), HttpStatus.OK);
    }

    /**
     * 明白融产品计算器
     * @param financeProduct
     * @param calculatorRecDto
     * @return
     */
    public ResponseEntity<Message> clearFinanceCalculator(FinanceProduct financeProduct, CalculatorRecDto calculatorRecDto,
                                                          String userName, String headerParam){
        if(calculatorRecDto.getFinancePeriod().intValue() % 12 != 0){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限应为12的整数倍"), HttpStatus.OK);
        }
        if(calculatorRecDto.getDownPay() > 1 || calculatorRecDto.getDownPay() < 0){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付比例错误"), HttpStatus.OK);
        }
        Double financeAmount = calculatorRecDto.getSellingPrice()+ calculatorRecDto.getGpsPrice() + calculatorRecDto.getOtherPrice() - calculatorRecDto.getDownPay() * calculatorRecDto.getSellingPrice();
        if(financeProduct.getMaxFinance().doubleValue() > 0 && financeProduct.getMaxFinance().compareTo(financeAmount) == -1){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "超过融资上限"), HttpStatus.OK);
        }
        String rate = null;
        if(financeProduct.getRate().split(",").length == 2){
            // 新车轻卡明白融0保证金产品
            rate = getMutativeRate(calculatorRecDto.getFinancePeriod(), financeProduct.getRate());
            if(calculatorRecDto.getFinancePeriod().equals(36)){
                // 36期可加一半购置税
                financeAmount += ((calculatorRecDto.getSellingPrice() / 1.17 * 0.1) / 2);
            }
        }else{
            // 其他明白融产品
            String rates = calculatorRecDto.getPayMode().equals(0) ? financeProduct.getRate().split(";")[0] : financeProduct.getRate().split(";")[1];
            rate = getMutativeRate(calculatorRecDto.getFinancePeriod(), rates);
        }
        Double monthPay = Math.abs(financeAmount / calculatorRecDto.getFinancePeriod());
        if(calculatorRecDto.getPayMode().equals(1)){
            // 分期收取手续费,算入月供中
            monthPay += (financeAmount * Double.parseDouble(rate)) / calculatorRecDto.getFinancePeriod();
        }
        Double dayPay = monthPay / 30;
        Double downPay = calculatorRecDto.getSellingPrice() * calculatorRecDto.getDownPay();
        Double deposit = calculatorRecDto.getSellingPrice() * financeProduct.getDepositRate();
        Double totalInterest = financeAmount * Double.parseDouble(rate);
        Double yearInterest = totalInterest / (calculatorRecDto.getFinancePeriod() / 12);
        Double yearEarnings = financeAmount * 0.08;
        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, deposit,
                totalInterest, yearInterest, yearEarnings);

        saveCalculatorRecord(financeProduct, calculatorRecDto, calculatorDto, userName, headerParam);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorDto), HttpStatus.OK);
    }

    /**
     * 获取利率(新车费率/二手车费率/LCV费率/0保证金费率)
     * @param financePeriod
     * @param temp
     * @return
     */
    public String getMutativeRate(Integer financePeriod, String temp){
        String rate = null;
        if(temp.split(",").length == 3){
            rate = temp.split(",")[(financePeriod / 12) - 1];
        }else if(temp.split(",").length == 2){
            rate = temp.split(",")[(financePeriod / 12) - 2];
        }
        return rate;
    }

    /**
     * 包牌融产品计算器
     * @param financeProduct
     * @param calculatorRecDto
     * @return
     */
    public ResponseEntity<Message> allIncludeFinanceCalculator(FinanceProduct financeProduct, CalculatorRecDto calculatorRecDto,
                                                               String userName, String headerParam){
        if(financeProduct.getDownPay() > calculatorRecDto.getDownPay()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付过低"), HttpStatus.OK);
        }
        // 购置税
        Double financeAmount = calculatorRecDto.getSellingPrice()+ calculatorRecDto.getGpsPrice() + calculatorRecDto.getOtherPrice() + ((calculatorRecDto.getSellingPrice() / 1.17 * 0.1) / 2) - calculatorRecDto.getDownPay();
        if(financeProduct.getMaxFinance().doubleValue() > 0 && financeProduct.getMaxFinance().compareTo(financeAmount) == -1){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "超过融资上限"), HttpStatus.OK);
        }
        Double monthPay = Math.abs(FinanceLib.pmt(Double.parseDouble(financeProduct.getRate()) / 12, calculatorRecDto.getFinancePeriod(), financeAmount, 0, false));
        Double dayPay = monthPay / 30;
        Double downPay = calculatorRecDto.getDownPay();
        Double totalInterest = monthPay * calculatorRecDto.getFinancePeriod() - financeAmount;
//        Double totalInterest = financeAmount * (Double.parseDouble(financeProduct.getRate()) / 12) * calculatorRecDto.getFinancePeriod();
        Double yearInterest = totalInterest / (calculatorRecDto.getFinancePeriod() / 12);
        Double yearEarnings = financeAmount * 0.08;
        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, null,
                totalInterest, yearInterest, yearEarnings);
        saveCalculatorRecord(financeProduct, calculatorRecDto, calculatorDto, userName, headerParam);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorDto), HttpStatus.OK);
    }

    /**
     * 保存计算记录
     * @param financeProduct
     * @param calculatorRecDto
     * @param calculatorDto
     */
    public void saveCalculatorRecord(FinanceProduct financeProduct, CalculatorRecDto calculatorRecDto, CalculatorDto calculatorDto,
                                     String userName, String headerParam){
        String customer = JsonPath.from(headerParam).get("systemflag");
        CarType carType = carTypeRepository.findOne(calculatorRecDto.getCarTypeId());
        calculatorRecDto.setCarTypeName(carType.getName());
        calculatorRecDto.setFinanceProductName(financeProduct.getName());
        CalculatorRecord calculatorRecord = new CalculatorRecord(gson.toJson(calculatorRecDto), gson.toJson(calculatorDto), userName, customer);
        calculatorRecordRepository.save(calculatorRecord);
    }

    /**
     * 获取计算历史
     * @param startDateStr
     * @param endDateStr
     * @param page
     * @param size
     * @return
     */
    public ResponseEntity<Message> getCalculatorRecords(String startDateStr, String endDateStr, Integer page, Integer size, String userName, String headerParam){
        page = page - 1;
        Page<CalculatorRecord> calculatorRecords = null;
        String customer = JsonPath.from(headerParam).get("systemflag");
        if(StringUtils.isEmpty(startDateStr) || StringUtils.isEmpty(endDateStr)){
            calculatorRecords = calculatorRecordRepository.findByOperatorAndCustomerOrderByTimeDesc(userName, customer, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "time")));
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dateFormat.parse(startDateStr);
                endDate = dateFormat.parse(endDateStr);
                endDate.setDate(endDate.getDate() + 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calculatorRecords = calculatorRecordRepository.findByOperatorAndCustomerAndTimeBetweenOrderByTimeDesc(userName, customer, startDate, endDate, new PageRequest(page, size));
        }

        List<CalculatorRecordDto> calculatorRecordDtos = Lists.newArrayList();
        calculatorRecords.getContent().forEach(calculatorRecord -> {
            Map condition = gson.fromJson(calculatorRecord.getCondition(), Map.class);
            Map record = gson.fromJson(calculatorRecord.getRecord(), Map.class);
            CalculatorRecordDto calculatorRecordDto = new CalculatorRecordDto(calculatorRecord.getId(),
                    condition, record, calculatorRecord.getTime());
            calculatorRecordDtos.add(calculatorRecordDto);
        });
        com.tm.wechat.dto.page.Page page1 = new com.tm.wechat.dto.page.Page(calculatorRecords);
        page1.setContent(calculatorRecordDtos);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, page1), HttpStatus.OK);
    }
}
