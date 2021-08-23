package com.alibaba.json.bvt.parser;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import com.alibaba.fastjson.util.ModuleUtil;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;

public class TypeUtilsTest2 extends TestCase {

    public void test_0() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertNull(typeUtils.cast("", Entity.class, null));
        Assert.assertNull(typeUtils.cast("", Type.class, null));
        Assert.assertNull(typeUtils.cast("", Byte.class, null));
        Assert.assertNull(typeUtils.cast("", Short.class, null));
        Assert.assertNull(typeUtils.cast("", Integer.class, null));
        Assert.assertNull(typeUtils.cast("", Long.class, null));
        Assert.assertNull(typeUtils.cast("", Float.class, null));
        Assert.assertNull(typeUtils.cast("", Double.class, null));
        Assert.assertNull(typeUtils.cast("", Character.class, null));
        Assert.assertNull(typeUtils.cast("", java.util.Date.class, null));
        Assert.assertNull(typeUtils.cast("", java.sql.Date.class, null));
        Assert.assertNull(typeUtils.cast("", java.sql.Timestamp.class, null));

        Assert.assertNull(typeUtils.castToChar(""));
        Assert.assertNull(typeUtils.castToChar(null));
        Assert.assertEquals('A', typeUtils.castToChar('A').charValue());
        Assert.assertEquals('A', typeUtils.castToChar("A").charValue());

        Assert.assertNull(typeUtils.castToBigDecimal(""));
        Assert.assertNull(typeUtils.castToBigInteger(""));
        Assert.assertNull(typeUtils.castToBoolean(""));
        Assert.assertNull(typeUtils.castToEnum("", Type.class, null));
        
        Assert.assertEquals(null, typeUtils.cast("", new TypeReference<Pair<Object, Object>>() {

        }.getType(), null));
    }

    public void test_1() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertEquals(null, typeUtils.cast("", new TypeReference<List<Object>>() {

        }.getType(), null));

    }

    public void test_error_2() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Exception error = null;
        try {
            Assert.assertEquals(null, typeUtils.cast("a", new TypeReference<List<Object>>() {

            }.getType(), null));
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_3() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Exception error = null;
        try {
            Assert.assertEquals(null, typeUtils.cast("a", new TypeReference<Pair<Object, Object>>() {

            }.getType(), null));
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_4() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Exception error = null;
        try {
            Assert.assertEquals(null, typeUtils.cast("a", ((ParameterizedType) new TypeReference<List<?>>() {

            }.getType()).getActualTypeArguments()[0], null));
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_0() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToChar("abc");
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_1() throws Exception {
        Exception error = null;
        try {
            TypeUtils.castToChar(true);
        } catch (JSONException e) {
            error = e;
        }
        Assert.assertNotNull(error);
    }

    public static class Entity {

    }

    public static class Pair<K, V> {

    }

    public static enum Type {
        A, B, C
    }

}
