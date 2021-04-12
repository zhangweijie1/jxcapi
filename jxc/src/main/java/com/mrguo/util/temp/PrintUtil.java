package com.mrguo.util.temp;

import com.mrguo.vo.TempPrintMaster;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class PrintUtil {

    public List<TempPrintMaster> getTempPrintMasterData(List<TempPrintMaster> masterList,
                                                        Object object) {

        for (TempPrintMaster master : masterList) {
            String fieldName = master.getField();
            String value = getFieldValueByFieldName(fieldName, object);
            master.setValue(value);
        }
        return masterList;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private static String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = getDeclaredField(object, fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            assert field != null;
            field.setAccessible(true);
            Object o = field.get(object);
            if (o == null) {
                return "";
            }
            if (o instanceof BigDecimal) {
                return o.toString();
            } else if (o instanceof Date) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                return sf.format(o);
            } else {
                return o.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field getDeclaredField(Object object, String fieldName){
        Field field = null ;
        Class<?> clazz = object.getClass() ;
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName) ;
                return field ;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }
}
