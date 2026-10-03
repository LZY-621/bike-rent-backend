package com.example.bikerent.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果封装
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 状态码：200成功，其它失败 */
    private Integer code;

    /** 返回消息 */
    private String message;

    /** 返回数据 */
    private T data;

    public Result() {}

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}