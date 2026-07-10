package com.xuena.supplier.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class SummaryVO {

    private Integer totalSupplierCount;
    private Integer activeSupplierCount;
    private Integer assessmentCount;
    private Integer completedAssessmentCount;
    private Double avgScore;
    private List<DepartmentSummary> departmentSummaries;
    private List<CategorySummary> categorySummaries;

    @Data
    public static class DepartmentSummary {
        private String department;
        private Integer scoreCount;
        private Double avgScore;
    }

    @Data
    public static class CategorySummary {
        private String category;
        private Integer supplierCount;
        private Double avgScore;
        private Integer gradeACount;
        private Integer gradeDCount;
    }
}
