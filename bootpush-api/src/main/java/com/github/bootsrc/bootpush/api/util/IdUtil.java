package com.github.bootsrc.bootpush.api.util;

import java.util.UUID;

public class IdUtil {
    public static String newUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        String str = newUUID();
        System.out.println(str);
        System.out.println("str.length=" + str.length());
    }
}
