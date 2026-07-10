package com.xuena.supplier.interfaces.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class TrendSeriesVO {
    private String name;
    private String type;
    private List<Double> data;
    private String dimension;
}
