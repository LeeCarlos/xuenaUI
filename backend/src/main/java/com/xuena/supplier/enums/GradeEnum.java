package com.xuena.supplier.enums;

public enum GradeEnum {

    A("A级", "优秀"),
    B("B级", "良好"),
    C("C级", "合格"),
    D("D级", "不合格");

    private final String code;
    private final String description;

    GradeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static GradeEnum fromCode(String code) {
        for (GradeEnum grade : values()) {
            if (grade.code.equals(code)) {
                return grade;
            }
        }
        return null;
    }
}