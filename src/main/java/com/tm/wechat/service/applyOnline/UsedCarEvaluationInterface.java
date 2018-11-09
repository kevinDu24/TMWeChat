package com.tm.wechat.service.applyOnline;

import com.tm.wechat.dto.apply.UsedCarEvaluationParamDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/28.
 */
@FeignClient(name = "usedCarEvaluationInterface", url = "${request.usedCarEvaluationUrl}")
public interface UsedCarEvaluationInterface {

    @RequestMapping(value = "taimeng/createPreCarBillId.html", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCarBillId(@RequestParam(value = "userName") String userName,
                        @RequestParam(value = "fromSource") String fromSource);


    @RequestMapping(value = "app/createPreCarImage.html", method = RequestMethod.POST)
    @ResponseBody
    String submitEvaluation(@RequestParam(value = "userName") String userName,
                            @RequestParam(value = "fromSource") String fromSource,
                            @RequestParam(value = "content") String content);

}
