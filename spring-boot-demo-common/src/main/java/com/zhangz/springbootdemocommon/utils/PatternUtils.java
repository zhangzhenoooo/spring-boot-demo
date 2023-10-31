package com.zhangz.springbootdemocommon.utils;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.regex.Pattern;

public class PatternUtils {
    /**
     * ^：匹配字符串的开始。
     * (?=.*[a-zA-Z])：匹配至少一个字母。
     * (?=.*\d)：匹配至少一个数字。
     * (?=.*[!@#$%^&*()\-_=+{};:,<.>])：匹配至少一个特殊字符。
     * .{8,}：匹配至少8个字符。
     * (?=.*[a-z])：匹配至少一个小写字母。
     * (?=.*[A-Z])：匹配至少一个大写字母。
     * $：匹配字符串的结束。
     */
 
    // 包含大小写字母、数字、特殊字符和符号，长度至少8个字符
    public static final String REGEX_PASSWORD1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{10,}$";
    // 包含字母、数字和特殊字符
    public static final String REGEX_PASSWORD2 = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$";
    // 包含大小写字母、数字和特殊字符，长度至少8个字符
    public static final String REGEX_PASSWORD3 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$";

    public static boolean matches(String regex, CharSequence input) {
        return Pattern.matches(regex, input);
    }
 
}
