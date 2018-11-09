//package com.tm.wechat.controller;
//
//import com.tm.wechat.config.FileUploadProperties;
//import com.tm.wechat.dao.RedisRepository;
//import com.tm.wechat.dto.gps.ActivateDeviceDto;
//import com.tm.wechat.dto.gps.DismantleDeviceDto;
//import com.tm.wechat.dto.message.Message;
//import com.tm.wechat.service.GpsInterface;
//import me.chanjar.weixin.common.exception.WxErrorException;
//import me.chanjar.weixin.mp.api.WxMpService;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
///**
// * Created by LEO on 16/9/14.
// */
//@RestController
//@RequestMapping(value = "/gps")
//public class GpsController {
//
//    @Autowired
//    private GpsInterface gpsInterface;
//
//    @Autowired
//    private RedisRepository redisRepository;
//
//    @Autowired
//    private WxMpService wxMpService;
//
//    @Autowired
//    private FileUploadProperties fileUploadProperties;
//
//    /**
//     * 获取短信验证码
//     * @param phoneNum
//     * @return
//     */
//    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
//    public ResponseEntity<Message> getSmsCode(String phoneNum){
//        return gpsInterface.getSmsCode(phoneNum);
//    }
//
//    /**
//     * 校验短信验证码
//     * @param phoneNum
//     * @param code
//     * @return
//     */
//    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
//    public ResponseEntity<Message> verifySmsCode(String phoneNum, String code){
//        return gpsInterface.verifySmsCode(phoneNum, code);
//    }
//
//    /**
//     * 激活设备
//     * @param activateDeviceDto
//     * @return
//     */
//    @RequestMapping(value = "/devices", method = RequestMethod.POST)
//    public ResponseEntity<Message> activateDevice(@RequestBody ActivateDeviceDto activateDeviceDto){
//        String accessToken = (String) redisRepository.get("wxAccessToken");
//        if(accessToken == null){
//            try {
//                accessToken = wxMpService.getAccessToken();
//                redisRepository.save("wxAccessToken", accessToken, 7200);
//            } catch (WxErrorException e) {
//                e.printStackTrace();
//            }
//        }
//        activateDeviceDto.setAccess_token(accessToken);
//        return gpsInterface.activateDevice(activateDeviceDto);
//    }
//
//    /**
//     * 设备激活状态查询
//     * @param simCardNum
//     * @return
//     */
//    @RequestMapping(value = "/devices/simCards/{simCardNum}", method = RequestMethod.GET)
//    public ResponseEntity<Message> getActiveDeviceBySimCard(String num, @PathVariable String simCardNum){
//        return gpsInterface.getActiveDeviceBySimCard(num, simCardNum);
//    }
//
//    /**
//     * 设备激活状态查询
//     * @param phoneNum
//     * @return
//     */
//    @RequestMapping(value = "/applyTasks/installPersons/{phoneNum}", method = RequestMethod.GET)
//    public ResponseEntity<Message> getActiveDeviceByApplyNum(@PathVariable String phoneNum){
//        return gpsInterface.getActiveDeviceByPhoneNum(phoneNum);
//    }
//
//    /**
//     * 获取历史记录
//     * @param applyNum
//     * @return
//     */
//    @RequestMapping(value = "/canel/applyTasks/{applyNum}", method = RequestMethod.GET)
//    public ResponseEntity<Message> getRecords(@PathVariable String applyNum){
//        return gpsInterface.getRecords(applyNum);
//    }
//
//    /**
//     * 获取图片
//     * @param path
//     * @return
//     */
//    @RequestMapping(value = "/deviceImg/{path}", method = RequestMethod.GET)
//    public byte[] getDeviceImg(@PathVariable String path){
//        path = fileUploadProperties.getDeviceImgPath() + path;
//        Resource imgRes = new FileSystemResource(path);
//        try {
//            return IOUtils.toByteArray(imgRes.getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 完成安装
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "/applyTasks/complete/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Message> completeApplyTask(@PathVariable Long id){
//        return gpsInterface.completeApplyTask(id);
//    }
//
//    /**
//     * 设备拆除
//     * @param dismantleDeviceDto
//     * @return
//     */
//    @RequestMapping(value = "/dismantle", method = RequestMethod.POST)
//    public ResponseEntity<Message> dismantleDevice(@RequestBody DismantleDeviceDto dismantleDeviceDto){
//        return gpsInterface.dismantleDevice(dismantleDeviceDto);
//    }
//
//    /**
//     * 设备拆除列表
//     * @param page
//     * @param size
//     * @return
//     */
//    @RequestMapping(value = "/dismantle", method = RequestMethod.GET)
//    public ResponseEntity<Message> getDismantleDevices(Integer page, Integer size, String startDate, String endDate,
//                                                       Integer dismantleStatus, String vin, String customerName, String phoneNum){
//        return gpsInterface.getDismantleDevices(size, page, startDate, endDate, dismantleStatus, "", phoneNum, vin, customerName);
//    }
//
//    /**
//     * 通过订单号和车架号后六位查询任务列表
//     * @param applyNum
//     * @param vin
//     * @return
//     */
//    @RequestMapping(value = "/applyTasks", method = RequestMethod.GET)
//    public ResponseEntity<Message> getApplyTasks(String applyNum, String vin){
//        return gpsInterface.getApplyTasks(applyNum, vin);
//    }
//
//    /**
//     * 更换设备
//     * @param id
//     * @return
//     */
//    @RequestMapping(value = "/applyTasks/reset/{simCardNum}/{id}", method = RequestMethod.PUT)
//    public ResponseEntity<Message> changeDevice(@PathVariable String simCardNum, @PathVariable String id, String resetReason){
//        return gpsInterface.changeDevice(simCardNum, id, resetReason);
//    }
//
//    /**
//     * 重新激活
//     * @param simCardNum
//     * @return
//     */
//    @RequestMapping(value = "/devices/reactive/simCards/{simCardNum}", method = RequestMethod.GET)
//    public ResponseEntity<Message> reactivate(@PathVariable String simCardNum){
//        return gpsInterface.reactivate(simCardNum);
//    }
//}
