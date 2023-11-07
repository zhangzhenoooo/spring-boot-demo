package com.zhangz.springbootdemocommon.utils;

import java.util.UUID;

public class UUIDUtils {
    public static String randomUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }
}
