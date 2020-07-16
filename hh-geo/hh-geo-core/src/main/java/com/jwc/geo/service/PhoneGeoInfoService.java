package com.jwc.geo.service;

import com.jwc.geo.entity.PhoneCallsAttributionEntity;
import com.jwc.geo.entity.PhoneCallsAttributionEntityExample;
import com.jwc.geo.mapper.PhoneCallsAttributionEntityMapper;
import com.jwc.geo.vo.GeoInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class PhoneGeoInfoService {
    @Resource
    private PhoneCallsAttributionEntityMapper phoneCallsAttributionEntityMapper;
    @Resource
    private GeoInfoService geoInfoService;

    private final static int PHONE_NUMBER_BEGIN_IDX = 0; // inclusive
    private final static int PHONE_NUMBER_END_IDX = 7;// exclusive

    /**
     * 根据手机号查询手机号码归属城市
     * 
     * @param phone
     * @return
     */
    public GeoInfoVO selectGeoInfoByPhone(String phone) {
        String number = genPhoneNumber(phone);
        PhoneCallsAttributionEntityExample example = new PhoneCallsAttributionEntityExample();
        example.createCriteria().andNumberEqualTo(number);
        List<PhoneCallsAttributionEntity> entity = this.phoneCallsAttributionEntityMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(entity)) {
            return null;
        }
        return this.geoInfoService.getGeoInfoById(entity.get(0).getCityId());
    }

    private String genPhoneNumber(String phone) {
        return phone.substring(PHONE_NUMBER_BEGIN_IDX, PHONE_NUMBER_END_IDX);
    }
}
