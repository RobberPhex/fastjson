package com.alibaba.json.bvt.parser;

import com.alibaba.fastjson.util.ModuleUtil;
import org.junit.Assert;

import com.alibaba.fastjson.util.TypeUtils;

import junit.framework.TestCase;

public class TypeUtilsTest_loadClass extends TestCase {

    public void test_loadClass() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertSame(Entity.class,
                typeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_loadClass$Entity",
                        Entity.class.getClassLoader()));

        Assert.assertSame(Entity.class,
                typeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_loadClass$Entity", null));
    }

    public void test_error() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertNull(typeUtils.loadClass("com.alibaba.json.bvt.parser.TypeUtilsTest_loadClass.Entity",
                Entity.class.getClassLoader()));
    }

    public static class Entity {

    }
}
