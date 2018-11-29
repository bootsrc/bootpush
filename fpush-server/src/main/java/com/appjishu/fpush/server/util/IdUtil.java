package com.appjishu.fpush.server.util;

import java.util.UUID;

public class IdUtil {
    /**
     * 保留id的最多4位尾数
     * @param id
     * @return
     */
    public static String id2TailStr(long id) {
        String idStr = id+"";
        int len = idStr.length();
        if (len > 4) {
            return idStr.substring(len-4);
        } else {
            return idStr;
        }
    }

//    public static void main(String[] args){
////        System.out.println(id2TailStr(100));
////        System.out.println(id2TailStr(469093324058263552L));
////
////        System.out.println(id2TailStr(4690933240582L));
////    	System.out.println(new12CharId());
//    	String str = newId();
//    	String part1 = str.substring(0, 20);
//    	String part2= str.substring(20);
//    	System.out.println("part1=" +part1 + "part2="+ part2);
//    }
    
    public static String newId() {
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    public static String new12CharId() {
    	return newId().substring(0, 12);
    }
}
