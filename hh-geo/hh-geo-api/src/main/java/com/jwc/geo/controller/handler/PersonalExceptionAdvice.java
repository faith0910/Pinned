package com.jwc.geo.controller.handler;

import com.jwc.geo.exception.ServiceException;
import com.jwc.geo.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class PersonalExceptionAdvice {
    private final static Logger logger = LoggerFactory.getLogger(PersonalExceptionAdvice.class);

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<String> exception(Exception e) {
        logger.error("handleRuntimeException---", e);
        return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
    }

    // 业务异常按请求成功处理，返回相应的 code 和 msg，让调用方处理
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result BusinessException(ServiceException e) {
        logger.error("业务异常信息:{}", e.getMessage());
        return new Result<>().suc();
    }
}
