package com.jwc.geo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    private final static Pattern PHONE_PATTERN = Pattern.compile("^[1][1,3,4,5,6,7,8,9][0-9]{9}$");
    private final static Pattern IP_PATTERN = Pattern
            .compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])(\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)){3}$");

    /**
     * 是否是手机号
     */
    public static boolean isMobile(String phone) {
        if (StrUtils.isEmpty(phone)) {
            return false;
        }
        Matcher m = PHONE_PATTERN.matcher(phone);
        return m.matches();
    }

    public static boolean isIP(String ip) {
        Matcher m = IP_PATTERN.matcher(ip);
        return m.matches();
    }
}
