package com.jwc.geo.utils;

import com.alibaba.fastjson.JSONObject;
import com.jwc.geo.consts.Amap;
import com.jwc.geo.vo.GeoVO;

public class AmapUtils {
    private final static String SEPERATOR = ",";
    private final static int LOCATION_FIELD_NUM = 2;
    private final static int LOCATION_LGT_IDX = 0;
    private final static int LOCATION_LAT_IDX = 1;

    public static GeoVO genGeoVO(String location) {
        String[] segs = location.split(SEPERATOR);
        if (segs.length != LOCATION_FIELD_NUM) {
            return null;
        }
        double lgt = Double.parseDouble(segs[LOCATION_LGT_IDX]);
        double lat = Double.parseDouble(segs[LOCATION_LAT_IDX]);
        GeoVO vo = new GeoVO();
        vo.setLgt(lgt);
        vo.setLat(lat);
        return vo;
    }

    public static String concatLgtLat(double lgt, double lat) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(lgt)).append(SEPERATOR).append(String.valueOf(lat));
        return builder.toString();
    }

    public static boolean returnSucess(String res) {
        if (StrUtils.isBlank(res)) {
            return false;
        }

        JSONObject obj = JSONObject.parseObject(res);
        return null != obj && obj.containsKey(Amap.RetKeys.STATUS)
                && obj.getIntValue(Amap.RetKeys.STATUS) == Amap.RetCode.SUCCESS;
    }
}
