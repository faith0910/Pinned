package com.jwc.geo.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrUtils.class);
    private static final Comparator<Object> comp = Collator.getInstance(java.util.Locale.CHINESE);

    private StrUtils() {
    }

    public static int compare(String s1, String s2) {
        return comp.compare(s1, s2);
    }

    public static final String left(String src, int len) {
        return src == null ? null : src.substring(0, len);
    }

    public static final boolean safeEqual(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    public static final String minString(String a, String b) {
        return (a == null || (b != null && a.compareTo(b) < 0)) ? b : a;
    }

    public static final String replaceCharAt(String s, int index, char val) {
        if (isEmpty(s) || index < 0 || index >= s.length()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        sb.deleteCharAt(index);
        sb.insert(index, val);
        return sb.toString();
    }

    public static final boolean testCharAt(String s, int index, char val) {
        if (isEmpty(s) || index < 0 || index >= s.length()) {
            return false;
        }
        return s.charAt(index) == val;
    }

    public static final String concat(Object prefix, Object... objs) {
        StringBuilder sb = new StringBuilder();
        if (prefix != null) {
            sb.append(prefix);
        }
        for (Object o : objs) {
            if (o != null) {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public static final String concat(List<?> objs) {
        StringBuilder sb = new StringBuilder();
        for (Object o : objs) {
            if (o != null) {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public static final String concat(Set<?> objs) {
        StringBuilder sb = new StringBuilder();
        for (Object o : objs) {
            if (o != null) {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public static final String join(Object separator, Object... objs) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Object o : objs) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(separator);
            }
            if (o != null) {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public static final String join(Object separator, List<?> objs) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Object o : objs) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(separator);
            }
            if (o != null) {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public static final String join(Object separator, Set<?> objs) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Object o : objs) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(separator);
            }
            if (o != null) {
                sb.append(o);
            }
        }
        return sb.toString();
    }

    public static final String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static final boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static final boolean isAnyEmpty(String... strings) {
        for (String s : strings) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    public static final boolean isBlank(String s) {
        return s == null || s.isEmpty() || s.trim().isEmpty();
    }

    public static final boolean isAnyBlank(String... strings) {
        for (String s : strings) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isNotBlank(String s) {
        return s != null && !s.isEmpty() && !s.trim().isEmpty();
    }

    public static final String reverse(String s) {
        StringBuilder sb = new StringBuilder(s);
        return sb.reverse().toString();
    }

    public static final String toString(Object obj) {
        return null == obj ? "" : obj.toString();
    }

    public static final String[] split(String s, String separator) {
        if (isEmpty(s)) {
            return null;
        }
        String tmp = s.trim();
        return tmp.isEmpty() ? null : tmp.split(separator);
    }

    public static final int parseInt(String s, int defVal) {
        if (isBlank(s)) {
            return defVal;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            LOGGER.warn("{} parseInt failed: {}", s, e.getMessage());
        } catch (Exception e) {
            LOGGER.warn("{} parseInt error", s, e);
        }
        return defVal;
    }

    public static final double parseDouble(String s, double defVal) {
        if (isBlank(s)) {
            return defVal;
        }
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            LOGGER.warn("{} parseDouble failed: {}", s, e.getMessage());
        } catch (Exception e) {
            LOGGER.warn("{} parseDouble error", s, e);
        }
        return defVal;
    }

    public static final long parseLong(String s, long defVal) {
        if (isBlank(s)) {
            return defVal;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            LOGGER.warn("{} parseLong failed: {}", s, e.getMessage());
        } catch (Exception e) {
            LOGGER.warn("{} parseLong error", s, e);
        }
        return defVal;
    }

    public static final int ver2Num(String ver, int defVal) {
        if (isBlank(ver)) {
            return defVal;
        }
        String[] items = ver.split("\\.");
        if (items.length != 3) {
            return defVal;
        }
        int ret = 0;
        for (String i : items) {
            int num = parseInt(i, -1);
            if (num < 0) {
                return defVal;
            }
            ret = (ret << 8) + num;
        }
        return ret;
    }

    public static final String getValue(String s) {
        return getValue(s, "");
    }

    public static final String getValue(String s, String defaultStr) {
        if (isBlank(s)) {
            return defaultStr;
        }
        return s;
    }

    public static final void fillSet(Set<String> set, String... items) {
        if (BaseUtils.arrEmpty(items)) {
            return;
        }
        for (String i : items) {
            String tmp = i.trim();
            if (!tmp.isEmpty()) {
                set.add(tmp);
            }
        }
    }

    public static final boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c) && !isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
