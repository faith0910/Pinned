package com.jwc.geo.utils;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
    private static HanyuPinyinOutputFormat DEFAULT_FORMAT = new HanyuPinyinOutputFormat();
    static {
        // 汉语拼音格式输出类
        DEFAULT_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        DEFAULT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        DEFAULT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    private static List<String> convertToPinyin(String zhStr) {
        char[] chars = zhStr.toCharArray();
        int charNum = chars.length;
        List<String> ret = new ArrayList<>(charNum);
        for (int i = 0; i < charNum; i++) {
            if (chars[i] <= 128) {
                ret.add(String.valueOf(chars[i]));
                continue;
            }
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(chars[i], DEFAULT_FORMAT);
                // 转换失败则按元字符处理
                if (null == pinyins) {
                    ret.add(String.valueOf(chars[i]));
                    continue;
                }
                // 多音字取第一个
                ret.add(pinyins[0]);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
                // TODO
            }
        }
        return ret;
    }

    public static String getPinyinFirstChars(String zhStr) {
        StringBuilder builder = new StringBuilder();
        List<String> pinyin = convertToPinyin(zhStr);
        pinyin.forEach(e -> builder.append(e.charAt(0)));
        return builder.toString().toUpperCase();
    }

    public static String getPinyin(String zhStr) {
        StringBuilder builder = new StringBuilder();
        List<String> pinyin = convertToPinyin(zhStr);
        pinyin.forEach(e -> builder.append(e));
        return builder.toString();
    }
}
