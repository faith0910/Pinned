package com.jwc.geo.consts;

public enum GeoLevelEnum {
    UNKNOWN(0, "未知"),

    FIRST(1, "一级"),

    SECOND(2, "二级"),

    THIRD(3, "三级"),

    FORTH(4, "四级"),

    ;

    private final int level;
    private final String desc;

    private GeoLevelEnum(int level, String desc) {
        this.level = level;
        this.desc = desc;
    }

    public int getLevel() {
        return level;
    }

    public String getDesc() {
        return desc;
    }

    public static GeoLevelEnum valueOf(int level) {
        for (GeoLevelEnum geoLevel : GeoLevelEnum.values()) {
            if (geoLevel.getLevel() == level) {
                return geoLevel;
            }
        }
        return GeoLevelEnum.UNKNOWN;
    }
}
