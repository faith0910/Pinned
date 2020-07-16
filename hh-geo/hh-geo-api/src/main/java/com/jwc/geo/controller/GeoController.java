package com.jwc.geo.controller;

import com.jwc.geo.consts.StatusEnum;
import com.jwc.geo.exception.RetCode;
import com.jwc.geo.model.GeoAddModel;
import com.jwc.geo.model.GeoBatchModel;
import com.jwc.geo.model.GeoDelModel;
import com.jwc.geo.model.GeoUpdateModel;
import com.jwc.geo.response.Result;
import com.jwc.geo.service.AmapService;
import com.jwc.geo.service.GeoInfoService;
import com.jwc.geo.utils.BaseUtils;
import com.jwc.geo.utils.StrUtils;
import com.jwc.geo.vo.GeoInfoVO;
import com.jwc.geo.vo.GeoTextVO;
import com.jwc.geo.vo.GeoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;



@Api("老版地理GEO接口")
@RestController
@RequestMapping("geo")
@SuppressWarnings("unchecked")
public class GeoController{

    private final static Logger logger = LoggerFactory.getLogger(GeoController.class);
    @Resource
    private GeoInfoService geoInfoService;
    @Resource
    private AmapService amapService;

    @ApiOperation("获取指定level的所有geo信息列表，不要使用1之外的值，数据量太大;使用的是新版数据源。")
    @GetMapping("level/info")
    public Result<List<GeoInfoVO>> geoLevelInfo(@RequestParam int level) {
        logger.info("请求geoLevelInfo:{}的接口！", level);
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> data = geoInfoService.getGeoInfoByLevel(level);
        return result.suc(data);
    }

    @ApiOperation("获取指定level的所有geo基本消息列表，不要使用1之外的值，数据量太大;使用的是新版数据源。")
    @GetMapping("level/text")
    public Result<List<GeoTextVO>> geoLevelText(@RequestParam int level) {
        logger.info("请求geoLevelText:{}的接口！", level);
        Result<List<GeoTextVO>> result = new Result<>();
        List<GeoTextVO> data = geoInfoService.getGeoTextByLevel(level);
        return result.suc(data);
    }

    @ApiOperation("获取指定pid的子节点信息列表;使用的是新版数据源。")
    @GetMapping("child/info")
    public Result<List<GeoInfoVO>> childInfo(@RequestParam int pid) {
        logger.info("请求childInfo:{}的接口！", pid);
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> data = geoInfoService.getChildGeoInfo(pid);
        return result.suc(data);
    }

    @ApiOperation("获取指定pid的子节点信息列表,level仅对直辖市/港澳台有效,level=3时返回其下属子节点信息列表;使用的是新版数据源。")
    @GetMapping("level/child/info")
    public Result<List<GeoInfoVO>> levelChildInfo(@RequestParam int pid, @RequestParam int level) {
        logger.info("请求levelChildInfo:{}, {}的接口！", pid, level);
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> data = geoInfoService.getChildGeoInfo(pid, level);
        return result.suc(data);
    }

    @ApiOperation("获取所有城市信息列表;使用的是新版数据源。")
    @GetMapping("city/info")
    public Result<List<GeoInfoVO>> allCity() {
        logger.info("请求allCity:的接口！");
        Result<List<GeoTextVO>> result = new Result<>();
        List<GeoInfoVO> data = geoInfoService.getAllCity();
        return result.suc(data);
    }

    @ApiOperation("获取指定省份所有城市信息列表")
    @GetMapping("province/city/info")
    public Result<GeoInfoVO> provinceCity(@RequestParam String province, @RequestParam String city) {
        logger.info("请求provinceCity:的接口！");
        Result<List<GeoTextVO>> result = new Result<>();
        GeoInfoVO data = geoInfoService.getProvinceCity(province, city);
        return result.suc(data);
    }

    @ApiOperation("获取指定id的GEO节点信息")
    @GetMapping("id/info")
    public Result<GeoInfoVO> idInfo(@RequestParam int id) {
        logger.info("请求idInfo:{}的接口！");
        Result<GeoInfoVO> result = new Result<>();
        GeoInfoVO data = geoInfoService.getGeoInfoById(id);
        return result.suc(data);
    }

    @ApiOperation("获取指定name的GEO节点信息。Warn：有重名的，不建议使用")
    @GetMapping("name/info")
    public Result<List<GeoInfoVO>> nameInfo(@RequestParam String name) {
        logger.info("请求nameInfo:{}的接口！");
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> data = geoInfoService.getGeoInfoByName(name);
        return result.suc(data);
    }

    @ApiOperation("获取指定pid下、特定name的geo节点信息")
    @GetMapping("pid/info")
    public Result<GeoInfoVO> pidInfo(@RequestParam int pid, @RequestParam String name) {
        logger.info("请求pidInfo:{}, {}的接口！", pid, name);
        Result<GeoInfoVO> result = new Result<>();
        if (StrUtils.isBlank(name)) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        GeoInfoVO data = geoInfoService.getGeoInfoByNamePid(pid, name);
        return result.suc(data);
    }

    @ApiOperation("根据id批量获取geo信息列表")
    @GetMapping("batch/info")
    public Result<Map<Integer, GeoInfoVO>> infos(@RequestBody GeoBatchModel model) {
        logger.info("请求infos:{}的接口！");
        Result<Map<Integer, GeoInfoVO>> result = new Result<>();
        if (BaseUtils.listEmpty(model.getIds())) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        Map<Integer, GeoInfoVO> data = geoInfoService.getGeoInfoByIds(model.getIds());
        return result.suc(data);
    }

    @ApiOperation("返回指定address的经纬度，可能返回空")
    @GetMapping("geo")
    public Result<GeoVO> geo(@RequestParam String address) {
        logger.info("请求geo:{}的接口！", address);
        Result<GeoVO> result = new Result<>();
        GeoVO data = amapService.geo(address);
        return result.suc(data);
    }

    @ApiOperation("根据经纬度返回GEO信息，可能返回空")
    @GetMapping("regeo")
    public Result<GeoInfoVO> regeo(@RequestParam double lgt, @RequestParam double lat) {
        logger.info("请求regeo:{}, {}的接口！", lgt, lat);
        Result<GeoInfoVO> result = new Result<>();
        GeoInfoVO data = amapService.reGeo(lgt, lat);
        return result.suc(data);
    }

    // @PostMapping("add")
    public Result<?> add(@RequestBody GeoAddModel model) {
        Result<?> result = new Result<>();
        if (invalidParam(model)) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        geoInfoService.addGeoInfo(model);
        return result.suc();
    }

    // @PostMapping("del")
    public Result<?> del(@RequestBody GeoDelModel model) {
        Result<?> result = new Result<>();
        if (StrUtils.isEmpty(model.getOperator()) || null == model.getId()) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        geoInfoService.delGeoInfo(model);
        return result.suc();
    }

    // @PostMapping("update")
    public Result<?> update(@RequestBody GeoUpdateModel model) {
        Result<?> result = new Result<>();
        if (invalidParam(model) || null == model.getId() || StrUtils.isEmpty(model.getPinyin())) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        geoInfoService.updateGeoInfo(model);
        return result.suc();
    }

    private boolean invalidParam(GeoAddModel model) {
        return StrUtils.isAnyEmpty(model.getName(), model.getOperator())
                || BaseUtils.isAnyNull(model.getParentId(), model.getSort())
                || invalidStatu(model.getCanDeliver(), model.getHot(), model.getSelfSupport());
    }

    private boolean invalidStatu(Integer... codes) {
        for (Integer code : codes) {
            if (null == code || (StatusEnum.INVALID.getCode() != code && StatusEnum.VALID.getCode() != code)) {
                return true;
            }
        }
        return false;
    }
}