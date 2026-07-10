package com.xuena.supplier.domain.enums;

public enum DepartmentEnum {

    QUALITY("质量", "质量部"),
    PLANNING("计划", "计划部"),
    PACKAGING("包开", "包开部"),
    PROCUREMENT("采购", "采购部");

    private final String code;
    private final String name;

    DepartmentEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static DepartmentEnum fromCode(String code) {
        for (DepartmentEnum department : values()) {
            if (department.code.equals(code)) {
                return department;
            }
        }
        return null;
    }
}