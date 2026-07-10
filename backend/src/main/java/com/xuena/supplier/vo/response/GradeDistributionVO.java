package com.xuena.supplier.vo.response;

import lombok.Data;

@Data
public class GradeDistributionVO {
    private String yearMonth;
    private String monthLabel;
    private Integer gradeA;
    private Integer gradeB;
    private Integer gradeC;
    private Integer gradeD;
}
