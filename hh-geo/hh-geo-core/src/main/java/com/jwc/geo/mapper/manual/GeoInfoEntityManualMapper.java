package com.jwc.geo.mapper.manual;

import org.apache.ibatis.annotations.Param;

public interface GeoInfoEntityManualMapper {
    int updateChildInfoByParentId(@Param("parentId") int parentId, @Param("level") int level);
}