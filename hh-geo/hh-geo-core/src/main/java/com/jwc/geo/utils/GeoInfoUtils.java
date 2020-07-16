package com.jwc.geo.utils;

import com.jwc.geo.consts.StatusEnum;
import com.jwc.geo.consts.TopGeoTypeEnum;
import com.jwc.geo.entity.GeoInfoEntity;
import com.jwc.geo.vo.GeoInfoVO;

public class GeoInfoUtils {

    public static GeoInfoVO genGeoInfoVO(GeoInfoEntity entity) {
        if (null == entity) {
            return null;
        }
        StatusEnum statu = isMunicipality(entity) ? StatusEnum.VALID : StatusEnum.INVALID;
        return genGeoInfoVO(entity, statu);
    }

    public static GeoInfoVO genGeoInfoVO(GeoInfoEntity entity, StatusEnum statu) {
        GeoInfoVO vo = new GeoInfoVO();
        BaseUtils.copyProperties(vo, entity);
        vo.setMunicipality(statu.getCode());
        return vo;
    }

    public static boolean isMunicipality(GeoInfoEntity entity) {
        return entity.getParentId() == TopGeoTypeEnum.MUNICIPALITY.getCode();
    }

}
