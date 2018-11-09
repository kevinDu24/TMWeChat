package com.tm.wechat.controller.file;

import com.tm.wechat.dto.message.Message;
import com.tm.wechat.service.CfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by LEO on 16/9/29.
 */
@RestController
@RequestMapping(value = "/files")
public class FileController {
    @Autowired
    private CfsService cfsService;

    /**
     * 文件上传
     * @param type
     * @param file
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Message> uploadFile(String type, MultipartFile file){
        return cfsService.uploadFile("", type, file);
    }

    /**
     * 文件上传带名字
     * @param name
     * @param type
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadFileByName", method = RequestMethod.POST)
    public ResponseEntity<Message> uploadFileByName(String name, String type, MultipartFile file){
        return cfsService.uploadFile(name, type, file);
    }

    /**
     * 批量文件上传
     * @param type
     * @param files
     * @return
     */
    @RequestMapping(value = "/multifileUpload", method = RequestMethod.POST)
    public ResponseEntity<Message> multifile(String type,  MultipartFile[] files){
        return cfsService.multifile(type, files);
    }

    /**
     * 文件下载
     * @param response
     * @param type
     * @param fileName
     * @param loadDate
     * @return
     * @throws IOException
     */
    @RequestMapping(value="/download/{type}/{loadDate}/{fileName}", method = RequestMethod.GET)
    public byte[] downloadFile(HttpServletResponse response, @PathVariable("type") String type,@PathVariable("fileName") String fileName, @PathVariable("loadDate") String loadDate) throws IOException {
        return cfsService.downloadFile(response, fileName, loadDate, type);
    }
}
