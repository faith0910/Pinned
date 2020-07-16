package com.jwc.geo.exception;

/**
 * @program: geo
 * @description: 自定义异常类
 * @author: Juwenchao
 * @create: 2020-07-07 10:33
 **/

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int code;
    private final String message;
    private Object data;

    public ServiceException(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ServiceException(RetCode code) {
        this.code = code.getCode();
        this.message = code.getMsg();
    }

    public ServiceException(RetCode code, Object data) {
        this.code = code.getCode();
        this.message = code.getMsg();
        this.data = data;
    }

    public ServiceException(int code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
