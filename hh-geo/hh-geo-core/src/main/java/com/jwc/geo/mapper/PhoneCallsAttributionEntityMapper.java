package com.jwc.geo.mapper;

import com.jwc.geo.entity.PhoneCallsAttributionEntity;
import com.jwc.geo.entity.PhoneCallsAttributionEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PhoneCallsAttributionEntityMapper {
    int countByExample(PhoneCallsAttributionEntityExample example);

    int deleteByExample(PhoneCallsAttributionEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PhoneCallsAttributionEntity record);

    int insertSelective(PhoneCallsAttributionEntity record);

    List<PhoneCallsAttributionEntity> selectByExample(PhoneCallsAttributionEntityExample example);

    PhoneCallsAttributionEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PhoneCallsAttributionEntity record, @Param("example") PhoneCallsAttributionEntityExample example);

    int updateByExample(@Param("record") PhoneCallsAttributionEntity record, @Param("example") PhoneCallsAttributionEntityExample example);

    int updateByPrimaryKeySelective(PhoneCallsAttributionEntity record);

    int updateByPrimaryKey(PhoneCallsAttributionEntity record);
}