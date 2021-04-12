package com.mrguo.config;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.MapWrapper;

import java.util.Map;

/**
 * 重写MapWrapper
 *
 * @author xieds
 * @date 2018/11/5 10:10
 * @updater xieds
 * @updatedate 2018/11/5 10:10
 */
public class CustomWrapper extends MapWrapper {

    public CustomWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject, map);
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping) {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
        }
        return name;
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        // 把Long转化成String
        if (value instanceof Long) {
            value = String.valueOf(value);
        }
        super.set(prop, value);
    }
}
