package com.tm.wechat.dto.industry;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LEO on 16/11/2.
 */
@Data
public class IndustryDto implements Serializable{

    private static final long serialVersionUID = -3350489711763011715L;
    private String name;
    private List<ChildDto> children;
}
