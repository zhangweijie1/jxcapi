package com.mrguo.config;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/1/54:32 PM
 * @updater 郭成兴
 * @updatedate 2020/1/54:32 PM
 */

/**
 * 重写ObjectWrapperFactory
 * @author xieds
 * @date 2018/11/5 10:10
 * @updater xieds
 * @updatedate 2018/11/5 10:10
 */
public class MapWrapperFactory implements ObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return object != null && object instanceof Map;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new CustomWrapper(metaObject,(Map)object);
    }
}
