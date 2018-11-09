package com.tm.wechat.dto.init;

import com.tm.wechat.dto.message.Message;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by LEO on 16/11/2.
 */
@Data
public class InitDto {
    private List newsList;
    private List noticeList;
    private List bannerList;
    public InitDto(){}
    public InitDto(ResponseEntity<Message> news, ResponseEntity<Message> notice, ResponseEntity<Message> banner){
        this.newsList = (List) ((Map)news.getBody().getData()).get("content");
        this.noticeList = (List) ((Map)notice.getBody().getData()).get("content");
        this.bannerList = (List) ((Map)banner.getBody().getData()).get("content");
    }
}
