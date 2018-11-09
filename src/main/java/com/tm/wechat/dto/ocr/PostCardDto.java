package com.tm.wechat.dto.ocr;

import lombok.Data;

/**
 * Created by pengchao on 2018/3/5.
 */
@Data
public class PostCardDto {
    
    private int app_id; //腾讯AI应用id
    
    private int time_stamp; //时间戳
    
    private String nonce_str; //随机数
    
    private String image; //image的base64字符串

    private String sign; //签名

}
