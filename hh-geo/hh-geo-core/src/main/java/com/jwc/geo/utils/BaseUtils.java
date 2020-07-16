package com.jwc.geo.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class BaseUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(BaseUtils.class);

    public static boolean listEmpty(List<?> l) {
        return l == null || l.isEmpty();
    }

    public static boolean listNotEmpty(List<?> l) {
        return l != null && !l.isEmpty();
    }

    public static boolean mapEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static boolean mapNotEmpty(Map<?, ?> m) {
        return m != null && !m.isEmpty();
    }

    public static boolean setEmpty(Set<?> s) {
        return s == null || s.isEmpty();
    }

    public static boolean setNotEmpty(Set<?> s) {
        return s != null && !s.isEmpty();
    }

    public static boolean arrEmpty(Object[] arr) {
        return arr == null || arr.length < 1;
    }

    public static boolean arrNotEmpty(Object[] arr) {
        return arr != null && arr.length > 0;
    }

    public static boolean checkBoolean(Boolean b) {
        return b != null && b.booleanValue();
    }

    public static boolean checkLong(Long l, long v) {
        return l != null && l.longValue() == v;
    }

    public static boolean checkInteger(Integer i, int v) {
        return i != null && i.intValue() == v;
    }

    public static boolean checkDouble(Double i, double v) {
        return i != null && Double.doubleToLongBits(i) == Double.doubleToLongBits(v);
    }

    public static int value(Integer i) {
        return i == null ? 0 : i.intValue();
    }

    public static int value(Integer i, int def) {
        return i == null ? def : i.intValue();
    }

    public static long value(Long l) {
        return l == null ? 0L : l.longValue();
    }

    public static long value(Long l, long def) {
        return l == null ? def : l.longValue();
    }

    public static void copyProperties(final Object dest, final Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException e) {
            LOGGER.error("copyProperties出现异常", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("copyProperties出现异常", e);
        }
    }

    public static double value(Double d, double def) {
        return d == null ? def : d.doubleValue();
    }

    public static boolean isAnyNull(Object... objects) {
        if (arrEmpty(objects)) {
            return true;
        }
        for (Object obj : objects) {
            if (obj == null) {
                return true;
            }
        }
        return false;
    }

    public static void sleep(long millis, String name, Logger LOGGER) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            LOGGER.warn("{} 线程休眠 {}ms 异常", name, millis);
        }
    }

    public static Field[] getFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        if (null != fields && fields.length > 0) {
            fieldList.addAll(Arrays.asList(fields));
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (null != superClazz && Object.class != superClazz) {
            Field[] superFields = getFields(superClazz);
            if (null != superFields && superFields.length > 0) {
                fieldList.addAll(Arrays.asList(superFields));
            }
        }
        return fieldList.toArray(new Field[0]);
    }
}
