package com.xuena.supplier.interfaces.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class TrendDataVO {
    private List<String> xAxis;
    private List<String> xAxisLabels;
    private List<String> categories;
    private String aggregateType;
    private List<TrendSeriesVO> series;
}
