package com.tm.wechat.service.car.yc;

import com.tm.wechat.dao.yc.FinanceKindRepository;
import com.tm.wechat.domain.yc.FinanceKind;
import com.tm.wechat.dto.calculator.*;
import com.tm.wechat.dto.calculator.yc.CalculateReqDto;
import com.tm.wechat.dto.calculator.yc.CalculatorRespDto;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.dto.message.MessageType;
import org.apache.poi.ss.formula.functions.FinanceLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CalculatorYCService {

    @Autowired
    private FinanceKindRepository financeKindRepository;

//    @Autowired
//    private Gson gson;

//    @Autowired
//    private CarBrandRepository carBrandRepository;

    /**
     * 亚驰计算器
     * @param calculateReqDto
     * @param userName
     * @return
     */
    public ResponseEntity<Message> calculator(CalculateReqDto calculateReqDto, String userName){
        FinanceKind financeKind = financeKindRepository.findOne(calculateReqDto.getFinanceKindId());
        if(financeKind.getMinPeriod() > calculateReqDto.getFinancePeriod()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限过短"), HttpStatus.OK);
        }
        if(financeKind.getMaxPeriod() < calculateReqDto.getFinancePeriod()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限过长"), HttpStatus.OK);
        }
        // 明白融产品
//        if(null != financeKind.getDepositRate()){
//            return clearFinanceCalculator(financeKind, calculateReqDto, userName);
//        }
        // 包牌融产品
        if(financeKind.getDownPay() > 1){
            return allIncludeFinanceCalculator(financeKind, calculateReqDto, userName);
        }
        //普通融资产品
        return commonCalculator(financeKind, calculateReqDto, userName);
    }

    /**
     * 普通融资产品计算器
     * @param financeKind
     * @param calculateReqDto
     * @param userName
     * @return
     */
    public ResponseEntity<Message> commonCalculator(FinanceKind financeKind, CalculateReqDto calculateReqDto, String userName){
        if(financeKind.getDownPay() > calculateReqDto.getDownPay()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付比例过低"), HttpStatus.OK);
        }
        if(financeKind.getDownPay() > 1 || calculateReqDto.getDownPay() < 0){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付比例错误"), HttpStatus.OK);
        }
        Double financeAmount = calculateReqDto.getCarPrice() + calculateReqDto.getGpsPrice() + calculateReqDto.getOtherPrice() - calculateReqDto.getDownPay() * calculateReqDto.getCarPrice();
        if(financeKind.getMaxFinance().doubleValue() > 0 && financeKind.getMaxFinance().compareTo(financeAmount) == -1){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "超过融资上限"), HttpStatus.OK);
        }
        String rate = financeKind.getRate();
//        if(financeKind.getRate().split(",").length > 1){
//            //自贸融明白产品,手续费不分期
//            return FTAFinanceCalculator(financeKind, calculateReqDto, userName);
//        }
        Double monthPay = Math.abs(FinanceLib.pmt(Double.parseDouble(rate) / 12, calculateReqDto.getFinancePeriod(), financeAmount, 0, false));
        Double dayPay = monthPay / 30;
        Double downPay = calculateReqDto.getCarPrice() * calculateReqDto.getDownPay();
        Double totalInterest = monthPay * calculateReqDto.getFinancePeriod() - financeAmount;
//        Double totalInterest = financeAmount * (Double.parseDouble(financeProduct.getRate()) / 12) * calculatorRecDto.getFinancePeriod();
        Double yearInterest = totalInterest / (calculateReqDto.getFinancePeriod() / 12);
        Double yearEarnings = financeAmount * 0.08;
        CalculatorRespDto calculatorRespDto = new CalculatorRespDto(financeAmount, monthPay, dayPay, downPay, null,
                totalInterest, yearInterest, yearEarnings);
//        saveCalculatorRecord(financeProduct, calculateReqDto, calculatorDto, userName);
        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorRespDto), HttpStatus.OK);
    }


    /**
     * 包牌融产品计算器
     * @param financeKind
     * @param calculateReqDto
     * @param userName
     * @return
     */
    public ResponseEntity<Message> allIncludeFinanceCalculator(FinanceKind financeKind, CalculateReqDto calculateReqDto, String userName){
        if(financeKind.getDownPay() > calculateReqDto.getDownPay()){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付过低"), HttpStatus.OK);
        }
        // 购置税
        Double financeAmount = calculateReqDto.getCarPrice()+ calculateReqDto.getGpsPrice() + calculateReqDto.getOtherPrice() + ((calculateReqDto.getCarPrice() / 1.17 * 0.1) / 2) - calculateReqDto.getDownPay();
        if(financeKind.getMaxFinance().doubleValue() > 0 && financeKind.getMaxFinance().compareTo(financeAmount) == -1){
            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "超过融资上限"), HttpStatus.OK);
        }
        Double monthPay = Math.abs(FinanceLib.pmt(Double.parseDouble(financeKind.getRate()) / 12, calculateReqDto.getFinancePeriod(), financeAmount, 0, false));
        Double dayPay = monthPay / 30;
        Double downPay = calculateReqDto.getDownPay();
        Double totalInterest = monthPay * calculateReqDto.getFinancePeriod() - financeAmount;
//        Double totalInterest = financeAmount * (Double.parseDouble(financeProduct.getRate()) / 12) * calculatorRecDto.getFinancePeriod();
        Double yearInterest = totalInterest / (calculateReqDto.getFinancePeriod() / 12);
        Double yearEarnings = financeAmount * 0.08;
        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, null,
                totalInterest, yearInterest, yearEarnings);
//        saveCalculatorRecord(financeKind, calculateReqDto, calculatorDto, userName);
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

    //    /**
//     * 自贸融明白计算器(手续费一次性收取)
//     * @param financeProduct
//     * @param calculatorRecDto
//     * @return
//     */
//    public ResponseEntity<Message> FTAFinanceCalculator(FinanceKind financeKind, CalculateReqDto calculateReqDto, String userName){
//        if(calculatorRecDto.getFinancePeriod().intValue() % 12 != 0){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限应为12的整数倍"), HttpStatus.OK);
//        }
//        String rate = getMutativeRate(calculatorRecDto.getFinancePeriod(), financeProduct.getRate().split(";")[0]);
//        Double financeAmount = calculatorRecDto.getSellingPrice()+ calculatorRecDto.getGpsPrice() + calculatorRecDto.getOtherPrice() - calculatorRecDto.getDownPay() * calculatorRecDto.getSellingPrice();
//        Double monthPay = financeAmount / calculatorRecDto.getFinancePeriod();
//        Double dayPay = monthPay / 30;
//        Double downPay = calculatorRecDto.getSellingPrice() * calculatorRecDto.getDownPay();
//        Double totalInterest = financeAmount * Double.parseDouble(rate);
//        Double yearInterest = totalInterest / (calculatorRecDto.getFinancePeriod() / 12);
//        Double yearEarnings = financeAmount * 0.08;
//        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, null,
//                totalInterest, yearInterest, yearEarnings);
//        saveCalculatorRecord(financeProduct, calculatorRecDto, calculatorDto, userName);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorDto), HttpStatus.OK);
//    }

//    /**
//     * 明白融产品计算器
//     * @param financeKind
//     * @param calculatorRecDto
//     * @param userName
//     * @return
//     */
//    public ResponseEntity<Message> clearFinanceCalculator(FinanceKind financeKind, CalculatorRecDto calculatorRecDto, String userName){
//        if(calculatorRecDto.getFinancePeriod().intValue() % 12 != 0){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "融资期限应为12的整数倍"), HttpStatus.OK);
//        }
//        if(calculatorRecDto.getDownPay() > 1 || calculatorRecDto.getDownPay() < 0){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "首付比例错误"), HttpStatus.OK);
//        }
//        Double financeAmount = calculatorRecDto.getSellingPrice()+ calculatorRecDto.getGpsPrice() + calculatorRecDto.getOtherPrice() - calculatorRecDto.getDownPay() * calculatorRecDto.getSellingPrice();
//        if(financeProduct.getMaxFinance().doubleValue() > 0 && financeProduct.getMaxFinance().compareTo(financeAmount) == -1){
//            return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_ERROR, "超过融资上限"), HttpStatus.OK);
//        }
//        String rate = null;
//        if(financeProduct.getRate().split(",").length == 2){
//            // 新车轻卡明白融0保证金产品
//            rate = getMutativeRate(calculatorRecDto.getFinancePeriod(), financeProduct.getRate());
//            if(calculatorRecDto.getFinancePeriod().equals(36)){
//                // 36期可加一半购置税
//                financeAmount += ((calculatorRecDto.getSellingPrice() / 1.17 * 0.1) / 2);
//            }
//        }else{
//            // 其他明白融产品
//            String rates = calculatorRecDto.getPayMode().equals(0) ? financeProduct.getRate().split(";")[0] : financeProduct.getRate().split(";")[1];
//            rate = getMutativeRate(calculatorRecDto.getFinancePeriod(), rates);
//        }
//        Double monthPay = Math.abs(financeAmount / calculatorRecDto.getFinancePeriod());
//        if(calculatorRecDto.getPayMode().equals(1)){
//            // 分期收取手续费,算入月供中
//            monthPay += (financeAmount * Double.parseDouble(rate)) / calculatorRecDto.getFinancePeriod();
//        }
//        Double dayPay = monthPay / 30;
//        Double downPay = calculatorRecDto.getSellingPrice() * calculatorRecDto.getDownPay();
//        Double deposit = calculatorRecDto.getSellingPrice() * financeProduct.getDepositRate();
//        Double totalInterest = financeAmount * Double.parseDouble(rate);
//        Double yearInterest = totalInterest / (calculatorRecDto.getFinancePeriod() / 12);
//        Double yearEarnings = financeAmount * 0.08;
//        CalculatorDto calculatorDto = new CalculatorDto(financeAmount, monthPay, dayPay, downPay, deposit,
//                totalInterest, yearInterest, yearEarnings);
//
//        saveCalculatorRecord(financeProduct, calculatorRecDto, calculatorDto, userName);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, calculatorDto), HttpStatus.OK);
//    }

//    /**
//     * 保存计算记录
//     * @param financeProduct
//     * @param calculatorRecDto
//     * @param calculatorDto
//     */
//    public void saveCalculatorRecord(FinanceProduct financeProduct, CalculatorRecDto calculatorRecDto, CalculatorDto calculatorDto, String userName){
//        CarType carType = carTypeRepository.findOne(calculatorRecDto.getCarTypeId());
//        calculatorRecDto.setCarTypeName(carType.getName());
//        calculatorRecDto.setFinanceProductName(financeProduct.getName());
//        CalculatorRecord calculatorRecord = new CalculatorRecord(gson.toJson(calculatorRecDto), gson.toJson(calculatorDto), userName);
//        calculatorRecordRepository.save(calculatorRecord);
//    }

//    /**
//     * 获取计算历史
//     * @param startDateStr
//     * @param endDateStr
//     * @param page
//     * @param size
//     * @return
//     */
//    public ResponseEntity<Message> getCalculatorRecords(String startDateStr, String endDateStr, Integer page, Integer size, String userName){
//        page = page - 1;
//        Page<CalculatorRecord> calculatorRecords = null;
//        if(StringUtils.isEmpty(startDateStr) || StringUtils.isEmpty(endDateStr)){
//            calculatorRecords = calculatorRecordRepository.findByOperatorOrderByTimeDesc(userName, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "time")));
//        }else{
//            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//            Date startDate = null;
//            Date endDate = null;
//            try {
//                startDate = dateFormat.parse(startDateStr);
//                endDate = dateFormat.parse(endDateStr);
//                endDate.setDate(endDate.getDate() + 1);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            calculatorRecords = calculatorRecordRepository.findByOperatorAndTimeBetweenOrderByTimeDesc(userName, startDate, endDate, new PageRequest(page, size));
//        }
//
//        List<CalculatorRecordDto> calculatorRecordDtos = Lists.newArrayList();
//        calculatorRecords.getContent().forEach(calculatorRecord -> {
//            Map condition = gson.fromJson(calculatorRecord.getCondition(), Map.class);
//            Map record = gson.fromJson(calculatorRecord.getRecord(), Map.class);
//            CalculatorRecordDto calculatorRecordDto = new CalculatorRecordDto(calculatorRecord.getId(),
//                    condition, record, calculatorRecord.getTime());
//            calculatorRecordDtos.add(calculatorRecordDto);
//        });
//        com.tm.wechat.dto.page.Page page1 = new com.tm.wechat.dto.page.Page(calculatorRecords);
//        page1.setContent(calculatorRecordDtos);
//        return new ResponseEntity<Message>(new Message(MessageType.MSG_TYPE_SUCCESS, page1), HttpStatus.OK);
//    }
}
