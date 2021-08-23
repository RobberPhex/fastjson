package com.alibaba.fastjson.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ModuleUtil {
    private static Map<Class, Object> mKHMap = new HashMap<Class, Object>();
    private static boolean hasJavaSql = false;

    static {
        try {
            Class.forName("java.sql.Time");
            hasJavaSql = true;
        } catch (Throwable e) {
            hasJavaSql = false;
        }
        if (hasJavaSql) {
            mKHMap.put(TypeUtils.class, new com.alibaba.fastjson.withjavasql.TypeUtils());
        } else {
            mKHMap.put(TypeUtils.class, new TypeUtils());
        }
    }

    public static <T> T getObject(Class<T> className) {
        return (T) mKHMap.get(className);
    }

    public static void callWhenJavaSql(Runnable runnable) {
        if (hasJavaSql) {
            runnable.run();
        }
    }
}
