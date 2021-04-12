
package com.mrguo.common.dao.base;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 通用Mapper接口,批量更新
 *
 * @author cxguo
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RegisterMapper
public interface BatchUpdateByPrimaryKeySelectiveMapper<T>
{

    /**
     * 根据主键批量更新属性不为null的值
     *
     * @param recordList
     * @return int [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    @UpdateProvider(type = BatchUpdateProvider.class, method = "dynamicSQL")
    @Options(useCache = false, useGeneratedKeys = false)
    int batchUpdateByPrimaryKeySelective(List<? extends T> recordList);

}