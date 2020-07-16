package com.jwc.geo.exception;

public enum RetCode {
    PARAM_ERROR(1, "参数错误"),

    PARAM_ERROR_GEO_HAS_SUB(101, "拥有子geo配置的geo不允许删除"),

    PARAM_ERROR_GEO_NOT_EXISTS(102, "数据不存在"),

    PARAM_ERROR_GEO_EXISTSED(103, "数据已经存在"),

    SERVER_ERROR(2, "依赖服务异常"),

    SERVER_ERROR_IP2CITY(201, "IP解析服务模块异常"),

    SUCCESS(-1,"成功")
    ;

    private final int code;
    private final String msg;

    private RetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
