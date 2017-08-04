package com.heraldg.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class StringUtil {

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String)
            return obj == null || "".equals(obj.toString().trim());
        else if (obj instanceof List)
            return obj == null || ((List) obj).isEmpty();
        else if (obj instanceof Map)
            return obj == null || ((Map) obj).isEmpty();
        else if (obj instanceof Object[])
            return obj == null || Array.getLength(obj) == 0;
        else
            return obj == null;
    }

    public static boolean isEmptys(Object obj1, Object obj2, OpertionType type) {
        if(type == OpertionType.OP_AND)
            return isEmpty(obj1) && isEmpty(obj2);
        else
            return isEmpty(obj1) || isEmpty(obj2);
    }

    public static boolean isNotEmpty(Object s) {
        System.out.println("isNotEmpty : " + !isEmpty(s));
        return !isEmpty(s);
    }
    
    public static boolean isNotEmptys(Object obj1, Object obj2, OpertionType type) {
        if(type == OpertionType.OP_AND)
            return isNotEmpty(obj1) && isNotEmpty(obj2);
        else
            return isEmpty(obj1) || isEmpty(obj2);
    }
    
    public static boolean isNotEmptys(Object obj1, Object obj2) {
            return isEmpty(obj1) || isEmpty(obj2);
    }

    public enum OpertionType {
        OP_AND, OP_OR;
    }

    public static String subStringBytes(String str, int byteLength) {

        int retLength = 0;
        int tempSize = 0;
        int asc;
        if(str == null || "".equals(str) || "null".equals(str)){
            str = "";
        }

        int length = str.length();

        for (int i = 1; i <= length; i++) {
            asc = (int) str.charAt(i - 1);
            if (asc > 127) {
                if (byteLength >= tempSize + 2) {
                    tempSize += 2;
                    retLength++;
                } else {
                    return str.substring(0, retLength) + "...";
                }
            } else {
                if (byteLength > tempSize) {
                    tempSize++;
                    retLength++;
                }
            }
        }

        return str.substring(0, retLength);
    }
}
