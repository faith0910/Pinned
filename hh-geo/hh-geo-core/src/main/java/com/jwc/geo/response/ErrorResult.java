package com.jwc.geo.response;

import com.jwc.geo.exception.RetCode;

/**
 * @program:demo
 * @description:
 * @author:Juwenchao
 * @date:2020-05-26 13:31:58
 */


public class ErrorResult {

    /**
     * 异常状态码
     */
    private Integer status;

    /**
     * 用户看得见的异常，例如 用户名重复！！,
     */
    private String message;

    /**
     * 异常的名字
     */
    private String exception;

    /**
     * 异常堆栈信息
     */
    //private String errors;

    /**
     * 对异常提示语进行封装
     */
    public static ErrorResult fail(RetCode resultCode, Throwable e, String message) {
        ErrorResult result = ErrorResult.fail(resultCode, e);
        result.setMessage(message);
        return result;
    }

    /**
     * 对异常枚举进行封装
     */
    public static ErrorResult fail(RetCode resultCode, Throwable e) {

        ErrorResult result = new ErrorResult();
        result.setMessage(resultCode.getMsg());
        result.setStatus(resultCode.getCode());
        result.setException(e.getClass().getName());
        //result.setErrors(Throwables.getStackTraceAsString(e));
        return result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}

