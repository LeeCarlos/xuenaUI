package com.xuena.supplier.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class CompareRadarVO {

    private List<String> indicators;
    private List<SupplierRadarData> suppliers;

    @Data
    public static class SupplierRadarData {
        private String name;
        private List<Double> values;
    }
}
