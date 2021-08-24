package com.alibaba.fastjson.util;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleUtil {
    private static Map<Class, Object> moduleObjectMap = new ConcurrentHashMap<Class, Object>();
    private static boolean hasJavaSql = false;

    static {
        try {
            Class.forName("java.sql.Time");
            hasJavaSql = true;
        } catch (Throwable e) {
            hasJavaSql = false;
        }
        if (hasJavaSql) {
            moduleObjectMap.put(TypeUtils.class, new com.alibaba.fastjson.withjavasql.TypeUtils());
        } else {
            moduleObjectMap.put(TypeUtils.class, new TypeUtils());
        }
    }

    public static <T> T getObject(Class<T> className) {
        return (T) moduleObjectMap.get(className);
    }

    public static <T> T callWhenHasJavaSql(Callable<T> callable) {
        if (hasJavaSql) {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
