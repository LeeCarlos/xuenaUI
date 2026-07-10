package com.xuena.supplier.interfaces.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    private int code;
    private String message;
    private T data;

    private ResultVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<>(200, "success", null);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(200, "success", data);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(200, message, data);
    }

    public static <T> ResultVO<T> error(int code, String message) {
        return new ResultVO<>(code, message, null);
    }

    public static <T> ResultVO<T> error(String message) {
        return new ResultVO<>(500, message, null);
    }
}