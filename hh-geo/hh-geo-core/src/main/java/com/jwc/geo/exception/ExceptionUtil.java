package com.jwc.geo.exception;


public class ExceptionUtil {
    public static void error(RetCode code) {
        throw new ServiceException(code.getCode(), code.getMsg());
    }
}
