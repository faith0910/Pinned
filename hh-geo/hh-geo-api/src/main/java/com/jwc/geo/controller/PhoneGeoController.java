package com.jwc.geo.controller;


import com.jwc.geo.exception.RetCode;
import com.jwc.geo.response.Result;
import com.jwc.geo.service.PhoneGeoInfoService;
import com.jwc.geo.utils.RegexUtils;
import com.jwc.geo.vo.GeoInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api("手机号归属地查询")
@RestController
@RequestMapping("geo/phone")
public class PhoneGeoController {
    @Autowired
    private PhoneGeoInfoService phoneGeoInfoService;

    @ApiOperation("查询手机号归属地")
    @GetMapping
    @SuppressWarnings("unchecked")
    public Result<GeoInfoVO> geoInfo(@RequestParam String phone) {
        Result<GeoInfoVO> result = new Result<>();
        if (!RegexUtils.isMobile(phone)) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        GeoInfoVO data = phoneGeoInfoService.selectGeoInfoByPhone(phone);
        return result.suc(data);
    }
}
