package com.jwc.geo.consts;

public final class Amap {
    public static final String KEY = "80e532cd0216bf975f7c366f21060ed6";
    public static final String EMPTY_CITY = "[]";

    public final class GeoParam {
        public static final String ADDRES = "address";
        public static final String KEY = "key";
    }

    public final class ReGeoParam {
        public static final String LOCATION = "location";
        public static final String KEY = "key";
        public static final String EXTENSIONS = "extensions";
    }

    public final class Url {
        public static final String GEO = "http://restapi.amap.com/v3/geocode/geo?";
        public static final String REGEO = "http://restapi.amap.com/v3/geocode/regeo?";
    }

    public final class RetCode {
        public static final int SUCCESS = 1;
        public static final int FAIL = 0;
    }

    /**
     * 返回参数，前缀代表层级关系 
     */
    public final class RetKeys {
        public static final String STATUS = "status";
        // regeo
        public static final String REGEOCODE = "regeocode";// 官网文档regeocodes与实际不符
        public static final String REGEOCODE_ADDRESSCOMPONENT = "addressComponent";
        public static final String ADDRESSCOMPONENT_PROVINCE = "province";
        public static final String ADDRESSCOMPONENT_CITY = "city";
        public static final String ADDRESSCOMPONENT_DISTRICT = "district";
        // geo
        public static final String GEOCODES = "geocodes";
        public static final String GEOCODES_LOCATION = "location";

    }
}
