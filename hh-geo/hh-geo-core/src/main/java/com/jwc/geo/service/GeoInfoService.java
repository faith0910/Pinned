package com.jwc.geo.service;

import com.jwc.geo.consts.GeoLevelEnum;
import com.jwc.geo.consts.GeoVersionEnum;
import com.jwc.geo.consts.StatusEnum;
import com.jwc.geo.consts.TopGeoTypeEnum;
import com.jwc.geo.entity.GeoInfoEntity;
import com.jwc.geo.entity.GeoInfoEntityExample;
import com.jwc.geo.exception.ExceptionUtil;
import com.jwc.geo.exception.RetCode;
import com.jwc.geo.mapper.GeoInfoEntityMapper;
import com.jwc.geo.mapper.manual.GeoInfoEntityManualMapper;
import com.jwc.geo.model.GeoAddModel;
import com.jwc.geo.model.GeoDelModel;
import com.jwc.geo.model.GeoUpdateModel;
import com.jwc.geo.utils.BaseUtils;
import com.jwc.geo.utils.GeoInfoUtils;
import com.jwc.geo.utils.PinyinUtil;
import com.jwc.geo.vo.GeoInfoVO;
import com.jwc.geo.vo.GeoTextVO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeoInfoService {
    protected final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Resource
    private GeoInfoEntityMapper geoInfoEntityMapper;
    @Resource
    private GeoInfoEntityManualMapper geoInfoEntityManualMapper;

    /**
     * 按层级获取地理信息,按sort降序排列
     */
    public List<GeoInfoVO> getGeoInfoByLevel(int level) {
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andLevelEqualTo(level).andVersionEqualTo(GeoVersionEnum.NEW.getValue());
        List<GeoInfoEntity> entities = geoInfoEntityMapper.selectByExample(example);
        List<GeoInfoVO> ret = new ArrayList<>(entities.size());
        entities.forEach(e -> ret.add(GeoInfoUtils.genGeoInfoVO(e)));
        return ret;
    }

    /**
     * 按层级获取地理信息
     */
    public List<GeoTextVO> getGeoTextByLevel(int level) {
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andLevelEqualTo(level).andVersionEqualTo(GeoVersionEnum.NEW.getValue());
        List<GeoInfoEntity> entities = geoInfoEntityMapper.selectByExample(example);
        if (BaseUtils.listEmpty(entities)) {
            return Collections.emptyList();
        }
        List<GeoTextVO> ret = new ArrayList<>(entities.size());
        entities.forEach(e -> {
            GeoTextVO vo = new GeoTextVO();
            vo.setId(e.getId());
            vo.setName(e.getName());
            ret.add(vo);
        });
        return ret;
    }

    /**
     * 获取所有城市级别的数据 北京和石家庄、南京属于统一级别
     */
    public List<GeoInfoVO> getAllCity() {
        // 先获取直辖市、港澳台
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(TopGeoTypeEnum.MUNICIPALITY.getCode());
        List<GeoInfoEntity> municipalities = geoInfoEntityMapper.selectByExample(example);
        List<Integer> ids = municipalities.stream().map(e -> e.getId()).collect(Collectors.toList());
        // 获取所有二级城市（包括直辖市/港澳台下面的区/县）
        example = new GeoInfoEntityExample();
        example.createCriteria().andLevelEqualTo(GeoLevelEnum.SECOND.getLevel()).andVersionEqualTo(GeoVersionEnum.NEW.getValue());
        List<GeoInfoEntity> cities = geoInfoEntityMapper.selectByExample(example);
        // 过滤掉直辖市/港澳台下面的区/县
        cities = cities.stream().filter(e -> !ids.contains(e.getParentId())).collect(Collectors.toList());
        // 结果等于 直辖市/港澳台 + 地级市
        List<GeoInfoVO> ret = new ArrayList<>();
        municipalities.stream().forEach(e -> ret.add(GeoInfoUtils.genGeoInfoVO(e, StatusEnum.VALID)));
        cities.stream().forEach(e -> ret.add(GeoInfoUtils.genGeoInfoVO(e, StatusEnum.INVALID)));
        return ret;
    }

    public GeoInfoVO getProvinceCity(String province, String city) {
        // 先按直辖市/港澳台查询
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(TopGeoTypeEnum.MUNICIPALITY.getCode()).andNameEqualTo(province);
        List<GeoInfoEntity> geos = geoInfoEntityMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(geos)) {
            return GeoInfoUtils.genGeoInfoVO(geos.get(0));
        }
        // 再按普通省份/海外查询
        example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(TopGeoTypeEnum.PROVINCE.getCode()).andNameEqualTo(province);
        geos = geoInfoEntityMapper.selectByExample(example);
        // 如果省份不存在则返回空
        if (CollectionUtils.isEmpty(geos)) {
            return null;
        }
        // 省份存在则去该省份下查找对应的city
        return getGeoInfoByNamePid(geos.get(0).getId(), city);
    }

    public List<GeoInfoVO> getChildGeoInfo(int pid) {
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(pid).andVersionEqualTo(GeoVersionEnum.NEW.getValue());
        List<GeoInfoEntity> entities = geoInfoEntityMapper.selectByExample(example);
        if (BaseUtils.listEmpty(entities)) {
            return Collections.emptyList();
        }
        List<GeoInfoVO> ret = new ArrayList<>(entities.size());
        entities.forEach(e -> ret.add(GeoInfoUtils.genGeoInfoVO(e)));
        return ret;
    }

    /**
     * 获得子级别地理信息
     */
    public List<GeoInfoVO> getChildGeoInfo(int pid, int level) {
        // 直辖市/港澳台的二级child信息返回直辖市/港澳台信息本身
        if (level == GeoLevelEnum.SECOND.getLevel()) {
            GeoInfoEntity entity = geoInfoEntityMapper.selectByPrimaryKey(pid);
            if (GeoInfoUtils.isMunicipality(entity)) {
                return Arrays.asList(GeoInfoUtils.genGeoInfoVO(entity));
            }
        }
        // 非直辖市/港澳台的二级child信息直接返回
        return getChildGeoInfo(pid);
    }

    /**
     * 根据id获取地理信息
     */
    public GeoInfoVO getGeoInfoById(int id) {
        GeoInfoEntity entity = geoInfoEntityMapper.selectByPrimaryKey(id);
        return GeoInfoUtils.genGeoInfoVO(entity);
    }

    /**
     * 根据id批量获取地理信息
     */
    public Map<Integer, GeoInfoVO> getGeoInfoByIds(List<Integer> ids) {
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andIdIn(ids);
        List<GeoInfoEntity> entities = geoInfoEntityMapper.selectByExample(example);
        if (BaseUtils.listEmpty(entities)) {
            return Collections.emptyMap();
        }
        // 转换成 map
        Map<Integer, GeoInfoVO> retMap = new HashMap<>(entities.size());
        entities.forEach(e -> {
            retMap.put(e.getId(), GeoInfoUtils.genGeoInfoVO(e));
        });
        return retMap;
    }

    /**
     * 根据名称获取地理信息
     */
    public List<GeoInfoVO> getGeoInfoByName(String name) {
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andNameEqualTo(name);
        List<GeoInfoEntity> entities = geoInfoEntityMapper.selectByExample(example);
        List<GeoInfoVO> ret = new ArrayList<>(entities.size());
        entities.forEach(e -> ret.add(GeoInfoUtils.genGeoInfoVO(e)));
        return ret;
    }

    /**
     * 通过pid和name获取地理位置信息
     */
    public GeoInfoVO getGeoInfoByNamePid(int pid, String name) {
        // 依据 parentId 获取所有 entity
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(pid);
        List<GeoInfoEntity> entities = this.geoInfoEntityMapper.selectByExample(example);
        // 然后过滤掉 name 不符合规则的 entity
        entities = entities.stream().filter(e -> e.getName().contains(name)).collect(Collectors.toList());
        // 仅返回第一个
        if (BaseUtils.listNotEmpty(entities)) {
            return GeoInfoUtils.genGeoInfoVO(entities.get(0));
        }
        return null;
    }

    public void addGeoInfo(GeoAddModel model) {
        // 首先判定要添加的 geo 不存在
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(model.getParentId()).andNameEqualTo(model.getName());
        List<GeoInfoEntity> geos = geoInfoEntityMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(geos)) {
            ExceptionUtil.error(RetCode.PARAM_ERROR_GEO_EXISTSED);
        }
        GeoInfoEntity newGeo = genAddGeoInfoEntity(model);
        geoInfoEntityMapper.insert(newGeo);
    }

    public void delGeoInfo(GeoDelModel model) {
        // 验证 geo 是否存在
        GeoInfoEntity geo = geoInfoEntityMapper.selectByPrimaryKey(model.getId());
        if (null == geo) {
            ExceptionUtil.error(RetCode.PARAM_ERROR_GEO_NOT_EXISTS);
        }
        // 验证有没有sub geo信息，如果有则不允许
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(model.getId());
        List<GeoInfoEntity> subGeos = geoInfoEntityMapper.selectByExample(example);
        if (BaseUtils.listNotEmpty(subGeos)) {
            ExceptionUtil.error(RetCode.PARAM_ERROR_GEO_HAS_SUB);
        }
        // 执行删除操作
        geoInfoEntityMapper.deleteByPrimaryKey(model.getId());
    }

    public void updateGeoInfo(GeoUpdateModel model) {
        // 判定 geo 信息存在
        GeoInfoEntity geo = geoInfoEntityMapper.selectByPrimaryKey(model.getId());
        if (null == geo) {
            ExceptionUtil.error(RetCode.PARAM_ERROR_GEO_NOT_EXISTS);
        }
        // 生成待修改的 geo entity 依据 id 进行修改
        GeoInfoEntity newGeo = genUpdateGeoInfoEntity(model);
        geoInfoEntityMapper.updateByPrimaryKey(newGeo);

        // 如果有 child 配置信息，则递归更新 child 信息
        GeoInfoEntityExample example = new GeoInfoEntityExample();
        example.createCriteria().andParentIdEqualTo(model.getId());
        List<GeoInfoEntity> childs = geoInfoEntityMapper.selectByExample(example);
        if (!childs.isEmpty()) {
            updateChildInfo(newGeo, childs);
        }
    }

    private boolean isTopLevel(final int parentId) {
        for (TopGeoTypeEnum type : TopGeoTypeEnum.values()) {
            if (type.getCode() == parentId) {
                return true;
            }
        }
        return false;
    }

    private GeoInfoEntity genAddGeoInfoEntity(GeoAddModel model) {
        // 如果是顶层 geo则按 level=1处理
        if (isTopLevel(model.getParentId())) {
            return genAddGeoInfoEntity(model, GeoLevelEnum.FIRST.getLevel());
        }
        // 判定 parent 存在
        GeoInfoEntity parent = geoInfoEntityMapper.selectByPrimaryKey(model.getParentId());
        if (null == parent) {
            ExceptionUtil.error(RetCode.PARAM_ERROR_GEO_NOT_EXISTS);
        }
        // 非顶层 geo 的 level 按 parent geo 的 level+1 处理
        return genAddGeoInfoEntity(model, parent.getLevel() + 1);
    }

    private GeoInfoEntity genAddGeoInfoEntity(GeoAddModel model, int level) {
        GeoInfoEntity entity = new GeoInfoEntity();
        BaseUtils.copyProperties(entity, model);
        entity.setLevel(level);
        // 设置拼音相关
        String pinyin = PinyinUtil.getPinyinFirstChars(model.getName());
        entity.setPinyin(pinyin);
        entity.setInitial(String.valueOf(pinyin.charAt(0)));
        // 设置version
        entity.setVersion(GeoVersionEnum.NEW.getValue());
        entity.setCreator(model.getOperator());
        entity.setModifier(model.getOperator());
        return entity;
    }

    private GeoInfoEntity genUpdateGeoInfoEntity(GeoUpdateModel model) {
        // 如果是非顶层 geo 配置则需要验证 parent 信息是否存在
        if (isTopLevel(model.getParentId())) {
            return genUpdateGeoInfoEntity(model, GeoLevelEnum.FIRST.getLevel());
        }
        GeoInfoEntity parent = geoInfoEntityMapper.selectByPrimaryKey(model.getParentId());
        if (null == parent) {
            ExceptionUtil.error(RetCode.PARAM_ERROR_GEO_NOT_EXISTS);
        }
        return genUpdateGeoInfoEntity(model, parent.getLevel() + 1);
    }

    private GeoInfoEntity genUpdateGeoInfoEntity(GeoUpdateModel model, int level) {
        GeoInfoEntity entity = new GeoInfoEntity();
        BaseUtils.copyProperties(entity, model);
        entity.setLevel(level);
        entity.setInitial(String.valueOf(model.getPinyin().charAt(0)));
        entity.setVersion(GeoVersionEnum.NEW.getValue());
        entity.setModifier(model.getOperator());
        return entity;
    }

    // 目前仅更新level，canDeliver、selfSupport、hot后续再定
    private void updateChildInfo(GeoInfoEntity geo, List<GeoInfoEntity> childs) {
        geoInfoEntityManualMapper.updateChildInfoByParentId(geo.getId(), geo.getLevel() + 1);
        // 更新内存中的信息，目前仅更新 level 信息
        childs.stream().forEach(e -> e.setLevel(geo.getLevel() + 1));
        // 为后面支持canDeliver、selfSupport、hot，遍历更新（很少触发）
        for (GeoInfoEntity child : childs) {
            GeoInfoEntityExample example = new GeoInfoEntityExample();
            example.createCriteria().andParentIdEqualTo(child.getId());
            List<GeoInfoEntity> tmpChilds = geoInfoEntityMapper.selectByExample(example);
            if (tmpChilds.isEmpty()) {
                continue;
            }
            updateChildInfo(child, tmpChilds);
        }
    }
}
