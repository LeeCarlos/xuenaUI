package com.xuena.supplier.interfaces.vo.response;

import lombok.Data;

@Data
public class GradeDistributionVO {
    private String yearMonth;
    private String monthLabel;
    private Integer totalCount;
    private Integer gradeA;
    private String gradeAPercent;
    private Integer gradeB;
    private String gradeBPercent;
    private Integer gradeC;
    private String gradeCPercent;
    private Integer gradeD;
    private String gradeDPercent;
}
