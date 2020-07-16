package com.jwc.geo.consts;

public enum TopGeoTypeEnum {
    MUNICIPALITY(-2, "直辖市/港澳台"),

    PROVINCE(-1, "普通省份/海外/其它"),

    ;

    private final int code;
    private final String desc;

    private TopGeoTypeEnum(int code, String desc) {
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
