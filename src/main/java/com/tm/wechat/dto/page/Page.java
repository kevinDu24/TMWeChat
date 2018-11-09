package com.tm.wechat.dto.page;

import lombok.Data;

import java.util.List;

/**
 * Created by pengchao on 2015/11/26.
 */
@Data
public class Page {
    private int number;
    private int size;
    private int numberOfElements;
    private List content;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;
    private int totalPages;
    private long totalElements;

    public Page(){}

    public Page(List content, Long totalElements){
        this.content = content;
        this.totalElements = totalElements;
    }

    public Page(org.springframework.data.domain.Page page){
        this.number = page.getNumber();
        this.size = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public Page(Integer totalElements, List content, Integer page, Integer size){
        this.totalElements = totalElements;
        this.content = content;
        this.number = page;
        this.size = content.size();
        this.numberOfElements = size;
        this.isFirst = page == 0;
        this.hasPrevious = page > 0 && totalElements != 0;
        this.totalPages = (int)Math.ceil((double)totalElements / size);
        this.isLast = page + 1 == totalPages;
        this.hasNext = !this.isLast;
    }
}
