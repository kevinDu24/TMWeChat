package com.tm.wechat.controller.information;

import com.tm.wechat.config.AccountProperties;
import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.CompanyFileInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by LEO on 16/10/14.
 */
@RestController
@RequestMapping("/companyFiles")
public class CompanyFileController {
    @Autowired
    private CompanyFileInterface companyFileInterface;

    @Autowired
    private AccountProperties accountProperties;

    /**
     * 获取公司资料列表
     * @param type
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Message> getCompanyFiles(@RequestHeader(value="Header-Param", defaultValue="{\"systemflag\":\"taimeng\"}") String headerParam, Integer type, Integer page, Integer size){
        return companyFileInterface.getCompanyFiles(accountProperties.getAuth(), headerParam, type, page, size);
    }

    /**
     * 更新下载量
     * @param companyFileId
     */
    @RequestMapping(value = "/{companyFileId}/download", method = RequestMethod.PUT)
    public void download(@PathVariable Long companyFileId){
        companyFileInterface.download(accountProperties.getAuth(), companyFileId);
    }
}
