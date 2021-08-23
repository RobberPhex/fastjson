/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.fastjson.withjavasql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.CalendarCodec;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Time;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class TypeUtils extends com.alibaba.fastjson.util.TypeUtils {
    @Override
    public boolean isClob(Class clazz) {
        return Clob.class.isAssignableFrom(clazz);
    }

    @Override
    public java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;
        if (value instanceof BigDecimal) {
            longValue = longValue((BigDecimal) value);
        } else if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }
            if (isNumber(strVal)) {
                longValue = Long.parseLong(strVal);
            } else {
                JSONScanner scanner = new JSONScanner(strVal);
                if (scanner.scanISO8601DateIfMatch(false)) {
                    longValue = scanner.getCalendar().getTime().getTime();
                } else {
                    throw new JSONException("can not cast to Timestamp, value : " + strVal);
                }
            }
        }
        if (longValue <= 0) {
            throw new JSONException("can not cast to Date, value : " + value); // TODO 忽略 1970-01-01 之前的时间处理？
        }
        return new java.sql.Date(longValue);
    }


    @Override
    public Time castToSqlTime(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Time){
            return (Time) value;
        }
        if(value instanceof java.util.Date){
            return new Time(((java.util.Date) value).getTime());
        }
        if(value instanceof Calendar){
            return new Time(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;
        if(value instanceof BigDecimal){
            longValue = longValue((BigDecimal) value);
        } else if(value instanceof Number){
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 //
                    || "null".equalsIgnoreCase(strVal)) {
                return null;
            }

            if (isNumber(strVal)) {
                longValue = Long.parseLong(strVal);
            } else {
                if (strVal.length() == 8 && strVal.charAt(2) == ':' && strVal.charAt(5) == ':') {
                    return Time.valueOf(strVal);
                }

                JSONScanner scanner = new JSONScanner(strVal);
                if (scanner.scanISO8601DateIfMatch(false)) {
                    longValue = scanner.getCalendar().getTime().getTime();
                } else {
                    throw new JSONException("can not cast to Timestamp, value : " + strVal);
                }

            }
        }
        if(longValue <= 0){
            throw new JSONException("can not cast to Date, value : " + value); // TODO 忽略 1970-01-01 之前的时间处理？
        }
        return new Time(longValue);
    }

    @Override
    public java.sql.Timestamp castToTimestamp(Object value){
        if(value == null){
            return null;
        }
        if(value instanceof Calendar){
            return new java.sql.Timestamp(((Calendar) value).getTimeInMillis());
        }
        if(value instanceof java.sql.Timestamp){
            return (java.sql.Timestamp) value;
        }
        if(value instanceof java.util.Date){
            return new java.sql.Timestamp(((java.util.Date) value).getTime());
        }
        long longValue = 0;
        if(value instanceof BigDecimal){
            longValue = longValue((BigDecimal) value);
        } else if(value instanceof Number){
            longValue = ((Number) value).longValue();
        }
        if(value instanceof String){
            String strVal = (String) value;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }
            if(strVal.endsWith(".000000000")){
                strVal = strVal.substring(0, strVal.length() - 10);
            } else if(strVal.endsWith(".000000")){
                strVal = strVal.substring(0, strVal.length() - 7);
            }

            if (strVal.length() == 29
                    && strVal.charAt(4) == '-'
                    && strVal.charAt(7) == '-'
                    && strVal.charAt(10) == ' '
                    && strVal.charAt(13) == ':'
                    && strVal.charAt(16) == ':'
                    && strVal.charAt(19) == '.') {
                int year = num(
                        strVal.charAt(0),
                        strVal.charAt(1),
                        strVal.charAt(2),
                        strVal.charAt(3));
                int month = num(
                        strVal.charAt(5),
                        strVal.charAt(6));
                int day = num(
                        strVal.charAt(8),
                        strVal.charAt(9));
                int hour = num(
                        strVal.charAt(11),
                        strVal.charAt(12));
                int minute = num(
                        strVal.charAt(14),
                        strVal.charAt(15));
                int second = num(
                        strVal.charAt(17),
                        strVal.charAt(18));
                int nanos = num(
                        strVal.charAt(20),
                        strVal.charAt(21),
                        strVal.charAt(22),
                        strVal.charAt(23),
                        strVal.charAt(24),
                        strVal.charAt(25),
                        strVal.charAt(26),
                        strVal.charAt(27),
                        strVal.charAt(28));
                return new java.sql.Timestamp(year - 1900, month - 1, day, hour, minute, second, nanos);
            }

            if(isNumber(strVal)){
                longValue = Long.parseLong(strVal);
            } else {
                JSONScanner scanner = new JSONScanner(strVal);
                if(scanner.scanISO8601DateIfMatch(false)){
                    longValue = scanner.getCalendar().getTime().getTime();
                } else{
                    throw new JSONException("can not cast to Timestamp, value : " + strVal);
                }
            }
        }

        return new java.sql.Timestamp(longValue);
    }


    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T cast(Object obj, Class<T> clazz, ParserConfig config){
        if(obj == null){
            if(clazz == int.class){
                return (T) Integer.valueOf(0);
            } else if(clazz == long.class){
                return (T) Long.valueOf(0);
            } else if(clazz == short.class){
                return (T) Short.valueOf((short) 0);
            } else if(clazz == byte.class){
                return (T) Byte.valueOf((byte) 0);
            } else if(clazz == float.class){
                return (T) Float.valueOf(0);
            } else if(clazz == double.class){
                return (T) Double.valueOf(0);
            } else if(clazz == boolean.class){
                return (T) Boolean.FALSE;
            }
            return null;
        }

        if(clazz == null){
            throw new IllegalArgumentException("clazz is null");
        }

        if(clazz == obj.getClass()){
            return (T) obj;
        }

        if(obj instanceof Map){
            if(clazz == Map.class){
                return (T) obj;
            }

            Map map = (Map) obj;
            if(clazz == Object.class && !map.containsKey(JSON.DEFAULT_TYPE_KEY)){
                return (T) obj;
            }
            return castToJavaBean((Map<String,Object>) obj, clazz, config);
        }

        if(clazz.isArray()){
            if(obj instanceof Collection){
                Collection collection = (Collection) obj;
                int index = 0;
                Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                for(Object item : collection){
                    Object value = cast(item, clazz.getComponentType(), config);
                    Array.set(array, index, value);
                    index++;
                }
                return (T) array;
            }
            if(clazz == byte[].class){
                return (T) castToBytes(obj);
            }
        }

        if(clazz.isAssignableFrom(obj.getClass())){
            return (T) obj;
        }

        if(clazz == boolean.class || clazz == Boolean.class){
            return (T) castToBoolean(obj);
        }

        if(clazz == byte.class || clazz == Byte.class){
            return (T) castToByte(obj);
        }

        if(clazz == char.class || clazz == Character.class){
            return (T) castToChar(obj);
        }

        if(clazz == short.class || clazz == Short.class){
            return (T) castToShort(obj);
        }

        if(clazz == int.class || clazz == Integer.class){
            return (T) castToInt(obj);
        }

        if(clazz == long.class || clazz == Long.class){
            return (T) castToLong(obj);
        }

        if(clazz == float.class || clazz == Float.class){
            return (T) castToFloat(obj);
        }

        if(clazz == double.class || clazz == Double.class){
            return (T) castToDouble(obj);
        }

        if(clazz == String.class){
            return (T) castToString(obj);
        }

        if(clazz == BigDecimal.class){
            return (T) castToBigDecimal(obj);
        }

        if(clazz == BigInteger.class){
            return (T) castToBigInteger(obj);
        }

        if(clazz == Date.class){
            return (T) castToDate(obj);
        }

        if(clazz == java.sql.Date.class){
            return (T) castToSqlDate(obj);
        }

        if(clazz == Time.class){
            return (T) castToSqlTime(obj);
        }

        if(clazz == java.sql.Timestamp.class){
            return (T) castToTimestamp(obj);
        }

        if(clazz.isEnum()){
            return castToEnum(obj, clazz, config);
        }

        if(Calendar.class.isAssignableFrom(clazz)){
            Date date = castToDate(obj);
            Calendar calendar;
            if(clazz == Calendar.class){
                calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            } else{
                try{
                    calendar = (Calendar) clazz.newInstance();
                } catch(Exception e){
                    throw new JSONException("can not cast to : " + clazz.getName(), e);
                }
            }
            calendar.setTime(date);
            return (T) calendar;
        }

        String className = clazz.getName();
        if(className.equals("javax.xml.datatype.XMLGregorianCalendar")){
            Date date = castToDate(obj);
            Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
            calendar.setTime(date);
            return (T) CalendarCodec.instance.createXMLGregorianCalendar(calendar);
        }

        if(obj instanceof String){
            String strVal = (String) obj;
            if(strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)){
                return null;
            }

            if(clazz == java.util.Currency.class){
                return (T) java.util.Currency.getInstance(strVal);
            }

            if(clazz == java.util.Locale.class){
                return (T) toLocale(strVal);
            }

            if (className.startsWith("java.time.")) {
                String json = JSON.toJSONString(strVal);
                return JSON.parseObject(json, clazz);
            }
        }

        final ObjectDeserializer objectDeserializer = config.get(clazz);
        if (objectDeserializer != null) {
            String str = JSON.toJSONString(obj);
            return JSON.parseObject(str, clazz);
        }
        throw new JSONException("can not cast to : " + clazz.getName());
    }

    private void addBaseClassMappings(){
        mappings.put("byte", byte.class);
        mappings.put("short", short.class);
        mappings.put("int", int.class);
        mappings.put("long", long.class);
        mappings.put("float", float.class);
        mappings.put("double", double.class);
        mappings.put("boolean", boolean.class);
        mappings.put("char", char.class);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put("[B", byte[].class);
        mappings.put("[S", short[].class);
        mappings.put("[I", int[].class);
        mappings.put("[J", long[].class);
        mappings.put("[F", float[].class);
        mappings.put("[D", double[].class);
        mappings.put("[C", char[].class);
        mappings.put("[Z", boolean[].class);
        Class<?>[] classes = new Class[]{
                Object.class,
                java.lang.Cloneable.class,
                loadClass("java.lang.AutoCloseable"),
                java.lang.Exception.class,
                java.lang.RuntimeException.class,
                java.lang.IllegalAccessError.class,
                java.lang.IllegalAccessException.class,
                java.lang.IllegalArgumentException.class,
                java.lang.IllegalMonitorStateException.class,
                java.lang.IllegalStateException.class,
                java.lang.IllegalThreadStateException.class,
                java.lang.IndexOutOfBoundsException.class,
                java.lang.InstantiationError.class,
                java.lang.InstantiationException.class,
                java.lang.InternalError.class,
                java.lang.InterruptedException.class,
                java.lang.LinkageError.class,
                java.lang.NegativeArraySizeException.class,
                java.lang.NoClassDefFoundError.class,
                java.lang.NoSuchFieldError.class,
                java.lang.NoSuchFieldException.class,
                java.lang.NoSuchMethodError.class,
                java.lang.NoSuchMethodException.class,
                java.lang.NullPointerException.class,
                java.lang.NumberFormatException.class,
                java.lang.OutOfMemoryError.class,
                java.lang.SecurityException.class,
                java.lang.StackOverflowError.class,
                java.lang.StringIndexOutOfBoundsException.class,
                java.lang.TypeNotPresentException.class,
                java.lang.VerifyError.class,
                java.lang.StackTraceElement.class,
                java.util.HashMap.class,
                java.util.LinkedHashMap.class,
                java.util.Hashtable.class,
                java.util.TreeMap.class,
                java.util.IdentityHashMap.class,
                java.util.WeakHashMap.class,
                java.util.LinkedHashMap.class,
                java.util.HashSet.class,
                java.util.LinkedHashSet.class,
                java.util.TreeSet.class,
                java.util.ArrayList.class,
                java.util.concurrent.TimeUnit.class,
                java.util.concurrent.ConcurrentHashMap.class,
                java.util.concurrent.atomic.AtomicInteger.class,
                java.util.concurrent.atomic.AtomicLong.class,
                java.util.Collections.EMPTY_MAP.getClass(),
                java.lang.Boolean.class,
                java.lang.Character.class,
                java.lang.Byte.class,
                java.lang.Short.class,
                java.lang.Integer.class,
                java.lang.Long.class,
                java.lang.Float.class,
                java.lang.Double.class,
                java.lang.Number.class,
                java.lang.String.class,
                java.math.BigDecimal.class,
                java.math.BigInteger.class,
                java.util.BitSet.class,
                java.util.Calendar.class,
                java.util.Date.class,
                java.util.Locale.class,
                java.util.UUID.class,
                Time.class,
                java.sql.Date.class,
                java.sql.Timestamp.class,
                java.text.SimpleDateFormat.class,
                com.alibaba.fastjson.JSONObject.class,
                com.alibaba.fastjson.JSONPObject.class,
                com.alibaba.fastjson.JSONArray.class,
        };
        for(Class clazz : classes){
            if(clazz == null){
                continue;
            }
            mappings.put(clazz.getName(), clazz);
        }
    }


}
