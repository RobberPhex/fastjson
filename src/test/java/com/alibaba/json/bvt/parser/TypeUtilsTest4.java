package com.alibaba.json.bvt.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.alibaba.fastjson.util.ModuleUtil;
import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.util.TypeUtils;

@SuppressWarnings("unchecked")
public class TypeUtilsTest4 extends TestCase {

    public void test_array() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertEquals(0, typeUtils.cast(new Entity[0], Object[].class, null).length);
    }

    public void test_ParameterizedType() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertEquals(Integer.valueOf(123),
                            ((ArrayList<Object>) typeUtils.cast(Collections.singleton(123),
                                                                new TypeReference<ArrayList<Object>>() {
                                                                }.getType(), null)).get(0));
    }

    public void test_ParameterizedType2() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Assert.assertEquals("123",
                            ((HashMap<Object, String>) typeUtils.cast(Collections.singletonMap("name", 123),
                                                                      new TypeReference<HashMap<Object, String>>() {
                                                                      }.getType(), null)).get("name"));
    }

    public void test_ParameterizedType_error() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Exception error = null;
        try {
            typeUtils.cast(Collections.singleton(123), new TypeReference<HashMap<Object, String>>() {
            }.getType(), null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Exception error = null;
        try {
            typeUtils.cast("xxx", Object[].class, null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_error_2() throws Exception {
        TypeUtils typeUtils = ModuleUtil.getObject(TypeUtils.class);
        Exception error = null;
        try {
            typeUtils.cast(123, new TypeReference<Object[]>() {
            }.getType(), null);
        } catch (Exception ex) {
            error = ex;
        }
        Assert.assertNotNull(error);
    }

    public void test_exception() throws Exception {
        JSONObject object = (JSONObject) JSON.toJSON(new RuntimeException());
        object.put("class", "xxx");
        Assert.assertEquals(Exception.class, JSON.parseObject(object.toJSONString(), Exception.class).getClass());
    }

    public static class Entity {

    }
}
