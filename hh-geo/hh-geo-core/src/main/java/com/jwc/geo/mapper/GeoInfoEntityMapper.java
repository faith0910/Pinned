package com.jwc.geo.mapper;

import com.jwc.geo.entity.GeoInfoEntity;
import com.jwc.geo.entity.GeoInfoEntityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GeoInfoEntityMapper {
    int countByExample(GeoInfoEntityExample example);

    int deleteByExample(GeoInfoEntityExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GeoInfoEntity record);

    int insertSelective(GeoInfoEntity record);

    List<GeoInfoEntity> selectByExample(GeoInfoEntityExample example);

    GeoInfoEntity selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GeoInfoEntity record, @Param("example") GeoInfoEntityExample example);

    int updateByExample(@Param("record") GeoInfoEntity record, @Param("example") GeoInfoEntityExample example);

    int updateByPrimaryKeySelective(GeoInfoEntity record);

    int updateByPrimaryKey(GeoInfoEntity record);
}