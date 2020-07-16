package com.jwc.geo.consts;

public enum StatusEnum {
    INVALID(0, "无效"),

    VALID(1, "有效"),

    ;
    private final int code;
    private final String desc;

    private StatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
