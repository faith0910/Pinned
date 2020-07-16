package com.jwc.geo.controller;

import com.jwc.geo.consts.GeoLevelEnum;
import com.jwc.geo.response.Result;
import com.jwc.geo.service.GeoInfoService;
import com.jwc.geo.vo.GeoInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Deprecated // 新老数据表合并之后就没有必要再维护此部分接口
@Api("V2地理GEO信息接口")
@RestController
@RequestMapping("v2/geo")
@SuppressWarnings("unchecked")
public class NewGeoController{

    @Resource
    private GeoInfoService geoInfoService;

    @ApiOperation("列出所有省级行政单位，包括省、自治区、直辖市、港澳台")
    @GetMapping("provinces")
    public Result<List<GeoInfoVO>> provinces() {
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> provinces = geoInfoService.getGeoInfoByLevel(GeoLevelEnum.FIRST.getLevel());
        return result.suc(provinces);
    }

    @ApiOperation("获取指定pid下所有的子节点GEO列表")
    @GetMapping("subs")
    public Result<List<GeoInfoVO>> subs(@RequestParam int pid) {
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> subs = geoInfoService.getChildGeoInfo(pid);
        return result.suc(subs);
    }

    @ApiOperation("获取指定pid下所有的子节点GEO列表，level参数对直辖市/港澳台有效，如果level=3时返回子节点列表")
    @GetMapping("level/subs")
    public Result<List<GeoInfoVO>> levelSubs(@RequestParam int pid, @RequestParam int level) {
        Result<List<GeoInfoVO>> result = new Result<>();
        List<GeoInfoVO> subs = geoInfoService.getChildGeoInfo(pid, level);
        return result.suc(subs);
    }
}
