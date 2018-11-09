package com.tm.wechat.service;

import com.tm.wechat.dto.gps.ActivateDeviceDto;
import com.tm.wechat.dto.gps.DismantleDeviceDto;
import com.tm.wechat.dto.message.Message;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/9/14.
 */
@FeignClient(name = "GPS", url = "${request.adminServerUrl}")
public interface GpsInterface {

    @RequestMapping(value = "/sendCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getSmsCode(@RequestParam(value = "phoneNum") String phoneNum);

    @RequestMapping(value = "/gps/verifyCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> verifySmsCode(@RequestParam(value = "phoneNum") String phoneNum, @RequestParam(value = "code") String code);

    @RequestMapping(value = "/gps/activateDevice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> activateDevice(@RequestBody ActivateDeviceDto activateDeviceDto);

    @RequestMapping(value = "/gps/devices/simCards/{simCardNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getActiveDeviceBySimCard(@RequestParam(value = "num") String num, @PathVariable(value = "simCardNum") String simCardNum);

    @RequestMapping(value = "/gps/applyTasks/installPersons/{phoneNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getActiveDeviceByPhoneNum(@PathVariable(value = "phoneNum") String phoneNum);

    @RequestMapping(value = "/gps/cancel/applyTasks/{applyNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getRecords(@PathVariable(value = "applyNum") String applyNum);

    @RequestMapping(value = "/gps/deviceImg/{path}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    byte[] getDeviceImg(@PathVariable(value = "path") String path);

    @RequestMapping(value = "/gps/applyTasks/complete/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> completeApplyTask(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "/gpsdismantles/wechat/createGpsDismantle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> dismantleDevice(@RequestBody DismantleDeviceDto dismantleDeviceDto);

    @RequestMapping(value = "/gpsdismantles/wechat/getWechatGpsDismantle", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getDismantleDevices(@RequestParam(value = "size") Integer size, @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                                @RequestParam(value = "dismantleStatus") Integer dismantleStatus, @RequestParam(value = "dismantlePersonName") String dismantlePersonName,
                                                @RequestParam(value = "submitPersonPhone") String submitPersonPhone, @RequestParam(value = "vin") String vin,
                                                @RequestParam(value = "customerName") String customerName);

    @RequestMapping(value = "/gpsdismantles/wechat/getApplyTask", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> getApplyTasks(@RequestParam(value = "applyNum") String applyNum, @RequestParam(value = "vin") String vin);

    @RequestMapping(value = "/gps/applyTasks/reset/{simCardNum}/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> changeDevice(@PathVariable(value = "simCardNum") String simCardNum, @PathVariable(value = "id") String id, @RequestParam(value = "resetReason") String resetReason);

    @RequestMapping(value = "/gps/devices/reactive/simCards/{simCardNum}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Message> reactivate(@PathVariable(value = "simCardNum") String simCardNum);
}
