package com.huawei.hicloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ClassName MongoDBUtils
 * @Description Generate mongoDB query criteria utils.
 * @Author JiaxuYan
 * @Date 2019-3-23 17:02:16
 * @Version 1.0
 */
public class MongoDBUtils {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBUtils.class);

    /**
     * java.util.Date format
     */
    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Map<String, Object> genCriteria(Object obj, OprationMethod opt) throws Exception {
        Map<String, Object> allFieldsMap = getAllFieldsMap(obj, obj.getClass(), null, opt);

        return allFieldsMap;
    }

    /**
     * @param obj
     * @param clazz
     * @param topLayer
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getAllFieldsMap(Object obj, Class<?> clazz, String topLayer, OprationMethod optMethod) throws Exception {

        if (obj == null || obj.getClass() == Object.class) {
            return null;
        }
        if (clazz == null || clazz == Object.class) {
            return null;
        }

        topLayer = topLayer == null || "".equals(topLayer) ? "" : topLayer + ".";

        HashMap<String, Object> fieldMap = new HashMap<>();

        /* Map process */
        if (obj instanceof Map) {
            Map<String, Object> mapObj = (Map<String, Object>) obj;
            Set<Map.Entry<String, Object>> entries = mapObj.entrySet();
            if (entries.size() == 0) {
                return null;
            }
            for (Map.Entry<String, Object> entry : entries) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (isIgnoreKey(key, optMethod, topLayer)) {
                    continue;
                }

                if (isSimpleType(value, optMethod)) {
                    if (value != null) {
                        fieldMap.put(topLayer + key, covertHandler(value));
                    }
                } else {
                    if (!isIgnoreType(value)) {
                        Map<String, Object> complexObjFields = getAllFieldsMap(value, value.getClass(), topLayer + key, optMethod);
                        if (complexObjFields != null && complexObjFields.size() > 0) {
                            fieldMap.putAll(complexObjFields);
                        }
                    }
                }
            }
        } else {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length != 0) {
                for (Field field : fields) {
                    String key = field.getName();

                    if (isIgnoreKey(key, optMethod, topLayer)) {
                        continue;
                    }

                    PropertyDescriptor descriptor = null;
                    try {
                        descriptor = new PropertyDescriptor(key, clazz);
                    } catch (IntrospectionException e) {
                        //e.printStackTrace();
                        // Class field doesn't has get method.
                        logger.info("Class {} field {} doesn't has get method.", clazz.getName(), key);
                        continue;
                    }
                    Method method = descriptor.getReadMethod();
                    Object value = method.invoke(obj);

                    if (isSimpleType(value, optMethod)) {
                        if (value != null) {
                            fieldMap.put(topLayer + key, covertHandler(value));
                        }
                    } else {
                        if (!isIgnoreType(value)) {
                            // Complex object.
                            Map<String, Object> complexObjFields = getAllFieldsMap(value, value.getClass(), topLayer + key, optMethod);
                            if (complexObjFields != null && complexObjFields.size() > 0) {
                                fieldMap.putAll(complexObjFields);
                            }
                        }
                    }
                }
            }

            Class<?> superclass = clazz.getSuperclass();
            Map<String, Object> superFields = getAllFieldsMap(obj, superclass, topLayer, optMethod);
            if (superFields != null && superFields.size() != 0) {
                fieldMap.putAll(superFields);
            }
        }

        return fieldMap;
    }

    /**
     * Data format convert.
     *
     * @param obj
     * @return
     */
    private static Object covertHandler(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            Date dateTime = (Date) obj;
            Instant instant = dateTime.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            String dateTimeStr = dateTimeFormatter.format(localDateTime);

            return dateTimeStr;
        }

        return obj;
    }

    /**
     * Judge whether it is a simple data type.
     *
     * @param obj
     * @return
     */
    private static boolean isSimpleType(Object obj, OprationMethod optMethod) {
        if (obj == null) {
            return true;
        }
        if (optMethod == OprationMethod.UPDATE) {
            if (obj.getClass().isArray() || obj instanceof Collection) {
                return true;
            }
        }
        if (obj instanceof Number || obj instanceof String || obj instanceof Boolean) {
            return true;
        }

        return false;
    }

    /**
     * Ignore some type.
     *
     * @param obj
     * @return
     */
    private static boolean isIgnoreType(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Object[] || obj instanceof Collection) {
            return true;
        }

        return false;
    }

    /**
     * Ignore some key.
     *
     * @param key
     * @return
     */
    private static boolean isIgnoreKey(String key, OprationMethod optMethod, String topLayer) {
        if (key == null || "".equals(key)) {
            return true;
        }
        // Not first layer.
        if (topLayer != null && !"".equals(topLayer)) {
            return false;
        }
        if (optMethod != OprationMethod.UPDATE) {
            return false;
        }

        List<String> ignoreKey = Arrays.asList("id");
        if (ignoreKey.contains(key)) {
            return true;
        }

        return false;
    }

    public enum OprationMethod {
        GET, UPDATE
    }

}


