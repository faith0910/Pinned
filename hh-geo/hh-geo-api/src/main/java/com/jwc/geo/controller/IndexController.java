package com.jwc.geo.controller;

import com.jwc.geo.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("辅助性接口")
@RestController
@RequestMapping
@SuppressWarnings("unchecked")
public class IndexController {

    @ApiOperation("测试接口")
    @GetMapping("test/hello")
    public Result<String> hello() {
        Result<String> result = new Result<>();
        return result.suc("hello world!");
    }

    @ApiOperation("监控用接口")
    @GetMapping("monitor")
    public Result<String> monitor() {
        Result<String> response = new Result<>();
        return response.suc("It's runing!");
    }
}
