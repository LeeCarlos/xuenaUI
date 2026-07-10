package com.xuena.supplier.interfaces.vo.response;

import lombok.Data;

@Data
public class DimensionAvgVO {
    private String yearMonth;
    private String monthLabel;
    private Double dimensionA;
    private Double dimensionB;
    private Double dimensionC;
    private Double dimensionD;
}
