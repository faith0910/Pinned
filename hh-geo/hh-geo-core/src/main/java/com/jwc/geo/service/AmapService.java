package com.jwc.geo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwc.geo.consts.Amap;
import com.jwc.geo.consts.TopGeoTypeEnum;
import com.jwc.geo.utils.AmapUtils;
import com.jwc.geo.utils.HttpClientUtils;
import com.jwc.geo.vo.GeoInfoVO;
import com.jwc.geo.vo.GeoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: geo
 * @description: 封装高德相关业务
 * @author: Juwenchao
 * @create: 2020-07-07 13:08
 **/
@Service
public class AmapService {
    protected final static Logger logger = LoggerFactory.getLogger(AmapService.class);

    @Autowired
    private GeoInfoService geoInfoService;
    /**
     * 通过地址获取经纬度数据
     */
    public GeoVO geo(String address) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put(Amap.GeoParam.ADDRES, address);
        params.put(Amap.GeoParam.KEY, Amap.KEY);
        String jsonStr = HttpClientUtils.sendGetRequest(Amap.Url.GEO, params);
        if (!AmapUtils.returnSucess(jsonStr)) {
            logger.error("高德地图geo接口获取失败:{}", jsonStr);
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONArray geo = jsonObject.getJSONArray(Amap.RetKeys.GEOCODES);
        if (geo.isEmpty()) {
            logger.error("高德地图geo接口返回数据格式错误:{}", jsonStr);
            return null;
        }
        String location = geo.getJSONObject(0).getString(Amap.RetKeys.GEOCODES_LOCATION);
        return AmapUtils.genGeoVO(location);
    }

    /**
     * 通过经纬度获取地址位置位置信息
     */
    public GeoInfoVO reGeo(double lgt,double lat) {
        Map<String,String> params  = new HashMap<>();
        String location = AmapUtils.concatLgtLat(lgt, lat);
        params.put(Amap.ReGeoParam.LOCATION,location);
        params.put(Amap.ReGeoParam.KEY,Amap.KEY);
        String jsonStr = HttpClientUtils.sendGetRequest(Amap.Url.REGEO, params);
        if (!AmapUtils.returnSucess(jsonStr)){
            logger.error("高德地图regeo接口获取失败:{}", jsonStr);
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONObject geo = jsonObject.getJSONObject(Amap.RetKeys.REGEOCODE);
        JSONObject addressComponent = geo.getJSONObject(Amap.RetKeys.REGEOCODE_ADDRESSCOMPONENT);
        String province = addressComponent.getString(Amap.RetKeys.ADDRESSCOMPONENT_PROVINCE);
        String city = addressComponent.getString(Amap.RetKeys.ADDRESSCOMPONENT_CITY);
        String district = addressComponent.getString(Amap.RetKeys.ADDRESSCOMPONENT_DISTRICT);
        if (Objects.equals(city,Amap.EMPTY_CITY)){
            city = district;
        }
        return this.mapping2Geoinfo(province,city);


    }
    /**
     * 映射到 DB 中的 GEO 信息
     */
    private GeoInfoVO mapping2Geoinfo(String province, String city) {
        // 首先按直辖市/港澳台查询
       GeoInfoVO geoInfo =  geoInfoService.getGeoInfoByNamePid(TopGeoTypeEnum.MUNICIPALITY.getCode(),province);
       if (geoInfo!=null){
           return geoInfo;
       }
        // 再按一般省份查询
        geoInfo = geoInfoService.getGeoInfoByNamePid(TopGeoTypeEnum.PROVINCE.getCode(), province);
        if (geoInfo==null){
            return null;
        }
        // 如果存在于一般省份里面则继续查询城市信息
        return geoInfoService.getGeoInfoByNamePid(geoInfo.getId(),city);
    }


}
