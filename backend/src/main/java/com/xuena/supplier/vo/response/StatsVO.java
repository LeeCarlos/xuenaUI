package com.xuena.supplier.vo.response;

import lombok.Data;

@Data
public class StatsVO {
    private String yearMonth;
    private Double avgTotal;
    private Integer gradeACount;
    private Integer gradeDCount;
    private Double changeFromPrev;
}
