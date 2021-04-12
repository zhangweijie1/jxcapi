package com.mrguo.common.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName: MapToEntityUtil
 * @Description: map转实体
 * @date 2018/10/28下午8:16
 * @updater 郭成兴
 * @updatedate 2018/10/28下午8:16
 */
public class MapToEntityUtil {

    public static <T> T map2Entity(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String filedTypeName = field.getType().getName();
                if (filedTypeName.equalsIgnoreCase("java.util.date")) {
                    String datetimestamp = String.valueOf(map.get(field.getName()));
                    if (datetimestamp.equalsIgnoreCase("null")) {
                        field.set(obj, null);
                    } else {
                        field.set(obj, new Date(Long.parseLong(datetimestamp)));
                    }
                } else if (filedTypeName.equalsIgnoreCase("java.lang.Long")) {
                    Object o = map.get(field.getName());
                    if (o == null) {
                        field.set(obj, null);
                    } else {
                        field.set(obj, Long.valueOf(String.valueOf(o)));
                    }
                } else {
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> T map2Entity(Object map, Class<T> clazz) {
        Map<String, Object> m = (Map) map;
        return map2Entity(m, clazz);
    }

        /**
         * 实体转map
         *
         * @param obj
         * @return Map
         * @throws
         * @author 郭成兴
         * @createdate 2018/11/12 上午10:04
         * @updater 郭成兴
         * @updatedate 2018/11/12 上午10:04
         */
    public static Map<String, Object> entity2Map(Object obj) {
        Map<String, Object> map = new HashMap<>(30);
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
