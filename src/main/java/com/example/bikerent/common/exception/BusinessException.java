package com.example.bikerent.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * 用于抛出业务层自定义错误
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }
}
