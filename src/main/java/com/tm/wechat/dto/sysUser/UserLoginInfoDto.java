package com.tm.wechat.dto.sysUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by pengchao on 2018/6/20.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginInfoDto  implements Serializable{

    private String deviceToken;

    private String uniqueMark;

    private String userName;

    public UserLoginInfoDto(String deviceToken, String uniqueMark, String userName) {
        this.deviceToken = deviceToken;
        this.uniqueMark = uniqueMark;
        this.userName = userName;
    }

    public UserLoginInfoDto() {
    }
}
