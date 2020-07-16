package com.jwc.geo.controller.handler;


import com.alibaba.fastjson.JSON;
import com.jwc.geo.response.ErrorResult;
import com.jwc.geo.response.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @program:demo
 * @description:
 * @author:Juwenchao
 * @date:2020-05-26 13:35:39
 */
public class ResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof ErrorResult) {
            ErrorResult errorResult = (ErrorResult) o;
            return Result.fail(errorResult.getStatus(), errorResult.getMessage());
        } else if (o instanceof String) {
            return JSON.toJSONString(Result.suc(o));
        }
        return Result.suc(o);
    }
}
