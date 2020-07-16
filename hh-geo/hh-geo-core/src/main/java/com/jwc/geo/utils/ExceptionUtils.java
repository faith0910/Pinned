package com.jwc.geo.utils;

import com.jwc.geo.exception.RetCode;
import com.jwc.geo.exception.ServiceException;

public class ExceptionUtils {
    public static void throwException(RetCode ret) {
        throw new ServiceException(ret.getCode(), ret.getMsg());
    }
}
