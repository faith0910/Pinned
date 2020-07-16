package com.jwc.geo.consts;

public enum GeoVersionEnum {
    OLD(0, "老版本"),

    NEW(1, "老版本"),

    ;

    private final int value;
    private final String desc;

    private GeoVersionEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}