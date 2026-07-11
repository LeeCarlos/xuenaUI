package com.xuena.supplier.interfaces.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    private List<T> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;

    public PageVO(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static <T> PageVO<T> of(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        return new PageVO<>(list, total, pageNum, pageSize);
    }
}