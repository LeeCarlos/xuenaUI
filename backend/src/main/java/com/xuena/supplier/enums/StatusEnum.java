package com.xuena.supplier.enums;

public enum StatusEnum {

    DRAFT("DRAFT", "草稿"),
    SUBMITTED("SUBMITTED", "已提交"),
    LOCKED("LOCKED", "已锁定"),
    PENDING("PENDING", "未开始"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    COMPLETED("COMPLETED", "已完成");

    private final String code;
    private final String description;

    StatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static StatusEnum fromCode(String code) {
        for (StatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}