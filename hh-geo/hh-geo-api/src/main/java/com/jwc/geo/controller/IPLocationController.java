package com.jwc.geo.controller;

import com.jwc.geo.response.Result;
import com.jwc.geo.exception.RetCode;
import com.jwc.geo.model.IPLocationModel;
import com.jwc.geo.service.IPLocationService;
import com.jwc.geo.utils.RegexUtils;
import com.jwc.geo.vo.IPLocationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("IP归属地查询")

@RestController
@RequestMapping("ip")
public class IPLocationController {
    @Autowired
    private IPLocationService ipLocationService;

    @SuppressWarnings("unchecked")
    @ApiOperation("查询IP归属地")
    @GetMapping("location")
    public Result<IPLocationVo> location(@RequestParam String ip) {
        Result<IPLocationVo> result = new Result<>();
        if (!RegexUtils.isIP(ip)) {
            return result.fail(RetCode.PARAM_ERROR.getCode(),RetCode.PARAM_ERROR.getMsg());
        }
        IPLocationVo vo = ipLocationService.getLocation(ip);
        if (null == vo) {
            return result.fail(RetCode.SERVER_ERROR_IP2CITY.getCode(),RetCode.SERVER_ERROR_IP2CITY.getMsg());
        }
        return result.suc(vo);
    }

    @ApiOperation("批量查询IP归属地")
    @GetMapping("location/batch")
    public Result<List<IPLocationVo>> batchLocation(@RequestBody IPLocationModel model) {
        Result<IPLocationVo> result = new Result<>();
        List<IPLocationVo> data = ipLocationService.batchLocation(model);
        return result.suc(data);
    }
}
