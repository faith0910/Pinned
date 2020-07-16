package com.jwc.geo.service;

import com.google.common.collect.Lists;
import com.jwc.geo.model.IPLocationModel;
import com.jwc.geo.utils.BaseUtils;
import com.jwc.geo.utils.RegexUtils;
import com.jwc.geo.vo.IPLocationVo;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IPLocationService {
    private final static Logger logger = LoggerFactory.getLogger(IPLocationService.class);
    private final static String ZH_CN = "zh-CN";
    private final static String DB_PATH = "/Users/guanghui/Documents/GeoData/geoip2/GeoLite2-City.mmdb";
    private static DatabaseReader dbReader;

    @PostConstruct
    private void init() {
        try {
//            InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(DB_PATH);
//            dbReader = new DatabaseReader.Builder(inStream).build();
        } catch (Exception e) {
            logger.error("加载GeoLite数据库出现异常", e);
        }
    }

    public IPLocationVo getLocation(String ip) {
        try {
            if (null == dbReader) {
                return null;
            }
            IPLocationVo vo = new IPLocationVo();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);

            // country
            Country country = response.getCountry();
            vo.setCountry(country.getNames().get(ZH_CN));
            // subdivision（alias to province）
            Subdivision subdivision = response.getMostSpecificSubdivision();
            vo.setProvince(subdivision.getNames().get(ZH_CN));
            // city
            City city = response.getCity();
            vo.setCity(city.getNames().get(ZH_CN));
            // lat && lng
            Location location = response.getLocation();
            vo.setLat(String.valueOf(location.getLatitude()));
            vo.setLng(String.valueOf(location.getLongitude()));
            return vo;
        } catch (IOException e) {
            logger.error("解析ip geo信息出现异常", e);
        } catch (GeoIp2Exception e) {
            logger.error("解析ip geo信息出现异常", e);
        }
        return null;
    }

    public List<IPLocationVo> batchLocation(IPLocationModel model) {
        if (BaseUtils.listEmpty(model.getIps())) {
            return Collections.emptyList();
        }
        // 过滤掉非法IP
        List<String> ips = model.getIps().stream().filter(e -> RegexUtils.isIP(e)).collect(Collectors.toList());
        List<IPLocationVo> ret = Lists.newArrayList();
        // 逐个遍历getLocation函数
        ips.stream().forEach(ip -> {
            IPLocationVo vo = getLocation(ip);
            if (null != vo) {
                ret.add(vo);
            }
        });
        return ret;
    }
}
