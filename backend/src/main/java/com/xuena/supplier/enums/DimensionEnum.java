package com.xuena.supplier.enums;

public enum DimensionEnum {

    A("A", "品质考核", 25),
    B("B", "成本考核", 20),
    C("C", "交货考核", 20),
    D1("D1", "服务考核-包材", 20),
    D2("D2", "服务考核-业务支持", 15);

    private final String code;
    private final String name;
    private final Integer fullScore;

    DimensionEnum(String code, String name, Integer fullScore) {
        this.code = code;
        this.name = name;
        this.fullScore = fullScore;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getFullScore() {
        return fullScore;
    }

    public static DimensionEnum fromCode(String code) {
        for (DimensionEnum dimension : values()) {
            if (dimension.code.equals(code)) {
                return dimension;
            }
        }
        return null;
    }
}