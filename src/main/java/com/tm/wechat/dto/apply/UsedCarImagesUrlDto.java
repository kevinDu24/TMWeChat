package com.tm.wechat.dto.apply;

import lombok.Data;

/**
 * Created by pengchao on 2017/09/19.
 */
@Data
public class UsedCarImagesUrlDto {

    private String number;

    private String imageUrl;

    private String uniqueMark;

    private String imageClass;

    public UsedCarImagesUrlDto(Object[] objs){
        this.number = objs[0] == null ? "": objs[0].toString();
        this.imageUrl= objs[1] == null ? "": objs[1].toString();
        this.uniqueMark = objs[1] == null ? "": objs[2].toString();
        this.imageClass = objs[3] == null ? "": objs[3].toString();
    }

}
