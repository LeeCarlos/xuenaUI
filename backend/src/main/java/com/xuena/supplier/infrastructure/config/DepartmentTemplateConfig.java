package com.xuena.supplier.infrastructure.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DepartmentTemplateConfig {

    @Data
    @AllArgsConstructor
    public static class Dimension {
        private String code;
        private String name;
        private Integer maxScore;
    }

    private static final Map<String, List<Dimension>> DEPARTMENT_DIMENSIONS = new HashMap<>();
    private static final Map<String, String> DEPARTMENT_EXCEPTION_COLUMN = new HashMap<>();

    static {
        List<Dimension> planDimensions = new ArrayList<>();
        planDimensions.add(new Dimension("c1", "c1交货延迟批次（8）", 8));
        planDimensions.add(new Dimension("c2", "c2交货数量短缺（6）", 6));
        planDimensions.add(new Dimension("c3", "c3订单交付配合度（6）", 6));
        DEPARTMENT_DIMENSIONS.put("计划部", planDimensions);
        DEPARTMENT_EXCEPTION_COLUMN.put("计划部", "交货考核异常");

        List<Dimension> purchaseDimensions = new ArrayList<>();
        purchaseDimensions.add(new Dimension("b1", "b1价格水平（5）", 5));
        purchaseDimensions.add(new Dimension("b2", "b2供货周期（4）", 4));
        purchaseDimensions.add(new Dimension("b3", "b3付款条件（5）", 5));
        purchaseDimensions.add(new Dimension("b4", "b4报价准确（3）", 3));
        purchaseDimensions.add(new Dimension("b5", "b5成本支持（3）", 3));
        purchaseDimensions.add(new Dimension("d1", "d1产品开发配合(20)-成品供应商", 20));
        purchaseDimensions.add(new Dimension("d2", "d2业务支持(15)", 15));
        DEPARTMENT_DIMENSIONS.put("采购部", purchaseDimensions);
        DEPARTMENT_EXCEPTION_COLUMN.put("采购部", "成本考核异常原因");

        List<Dimension> qualityDimensions = new ArrayList<>();
        qualityDimensions.add(new Dimension("a1", "a1验货合格率（7）", 7));
        qualityDimensions.add(new Dimension("a2", "a2综合客诉率（8）", 8));
        qualityDimensions.add(new Dimension("a3", "a3新品开发质量（5）", 5));
        qualityDimensions.add(new Dimension("a4", "a4质量改善完成率（5）", 5));
        DEPARTMENT_DIMENSIONS.put("质量部", qualityDimensions);
        DEPARTMENT_EXCEPTION_COLUMN.put("质量部", "品质考核异常原因");

        List<Dimension> packagingDimensions = new ArrayList<>();
        packagingDimensions.add(new Dimension("d1", "d1产品开发配合(20)-包材供应商", 20));
        DEPARTMENT_DIMENSIONS.put("包开部", packagingDimensions);
        DEPARTMENT_EXCEPTION_COLUMN.put("包开部", "服务考核异常原因（d1包材供应商）");
    }

    public static List<Dimension> getDimensions(String department) {
        return DEPARTMENT_DIMENSIONS.getOrDefault(department, new ArrayList<>());
    }

    public static String getExceptionColumn(String department) {
        return DEPARTMENT_EXCEPTION_COLUMN.getOrDefault(department, "考核异常原因");
    }

    public static int getTotalColumns(String department) {
        return 3 + getDimensions(department).size() + 1;
    }
}