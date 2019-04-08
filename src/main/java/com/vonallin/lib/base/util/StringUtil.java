package com.vonallin.lib.base.util;

import android.text.TextUtils;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//①②③④⑤⑥⑦⑧⑨
public class StringUtil {
    /**
     * 判断字串是否为空
     *
     * @param str
     * @return
     * @see TextUtils#isEmpty(CharSequence)
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 存在空字符串
     *
     * @param arrStr
     * @return
     */
    public static boolean isEmpty(CharSequence... arrStr) {
        for (CharSequence str : arrStr) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 不存在空字符串
     *
     * @param arrStr
     * @return
     */
    public static boolean isNotEmpty(CharSequence... arrStr) {
        return !isEmpty(arrStr);
    }


    public static boolean equals(String previous, String behind) {
        return previous != null && behind != null && previous.equals(behind);
    }

    public static boolean notEquals(String a, String b) {
        return !equals(a, b);
    }

    public static String getHttpUrl(String url, boolean handle) {
        if (url == null) return null;

        if (url.startsWith("https") && handle) {
            String realUrl = url.substring(5);
            return "http" + realUrl;
        }

        return url;
    }

    public static String build(String url, Map<String, String> params) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url);
        try {
            if (params != null) {
                buffer.append("?");
                for (Map.Entry<String, String> e : params.entrySet()) {
                    buffer.append(e.getKey());
                    buffer.append("=");
                    buffer.append(java.net.URLEncoder.encode(e.getValue(), "UTF-8"));
                    buffer.append("&");
                }
                buffer.setLength(buffer.length() - 1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return buffer.toString();
    }

    public static boolean isEmail(String s) {
        if (StringUtil.isEmpty(s) || !s.contains("@") || s.length() > 50) {
            return false;
        }
        if (s.length() > 50) {//正则表达式的：{2,50}，会直接卡死，java的缺陷？？使用笨方法
            return false;
        }
        String check = "^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(s);
        return matcher.matches();
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }

    public static String ensureNullToEmpty(String txt) {
        if (txt == null) {
            return "";
        }
        return txt;
    }

    /**
     * 判断给定的字符是否是null 或空 或全为不可见字符。
     *
     * @param value 输入字符串。
     * @return 若给定字符串为空或全为不可见字符则返回true.
     */
    public static boolean emptyOrUnvisible(String value) {
        int strLen = value == null ? 0 : value.length();
        boolean result = strLen == 0;
        if (!result) {
            result = Character.isWhitespace(value.charAt(--strLen));
            while (result && strLen > 0) {
                result = Character.isWhitespace(value.charAt(--strLen));
            }
        }
        return result;
    }

    public static String[] splitByChar(String input, char c) {
        int strlen = input == null ? 0 : input.length(), arraylen = 0, point;
        for (int i = 0; i < strlen; i++) {
            if ((point = input.indexOf(c, i)) == -1) {
                break;
            } else {
                arraylen++;
                i = point;
            }
        }
        String[] result;
        if (arraylen > 0) {
            result = new String[arraylen + 1];
            arraylen = 0;
            for (int i = 0; i < strlen; i++) {
                if ((point = input.indexOf(c, i)) == -1) {
                    result[arraylen++] = input.substring(i, strlen);
                    break;
                } else {
                    if (i == point) {
                        result[arraylen++] = "";
                    } else {
                        result[arraylen++] = input.substring(i, point);
                    }
                    i = point;
                }
            }
            if (arraylen < result.length) {
                result[result.length - 1] = "";
            }
        } else {
            result = new String[]{input == null ? "" : input};
        }
        return result;
    }

    public static boolean isValid(String regExpress, String input) {
        Pattern pattern = Pattern.compile(regExpress);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
