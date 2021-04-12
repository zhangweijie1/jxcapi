package com.mrguo.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mrguo.common.dao.base.BatchUpdateByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * mybatis数据库操作接口
 *
 * @author meguo
 * @date 2018/10/2414:17
 * @updater xieds
 * @updatedate 2018/10/2414:17
 */
@RegisterMapper
public interface MyMapper<T>
        extends Mapper<T>,
        BaseMapper<T>,
        InsertListMapper<T>,
        BatchUpdateByPrimaryKeySelectiveMapper<T>
        {

}