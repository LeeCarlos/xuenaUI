package com.xuena.supplier.interfaces.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class CompareHeatmapVO {

    private List<String> rows;
    private List<String> columns;
    private List<List<Double>> values;
}
