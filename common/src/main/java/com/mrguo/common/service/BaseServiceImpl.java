package com.mrguo.common.service;

import com.mrguo.common.dao.MyMapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Service层基础实现类
 */
public abstract class BaseServiceImpl<T> {

    /**
     * 获取Mapper
     *
     * @param
     * @return MyMapper
     * @throws
     * @author 郭成兴
     * @createdate 2019/4/14 11:49 AM
     * @updater 郭成兴
     * @updatedate 2019/4/14 11:49 AM
     */
    public abstract MyMapper<T> getMapper();

    public T saveData(T entity){
        return getMapper().insertSelective(entity) > 0 ? entity : null;
    }

    public int insertListData(List<T> entitys) {
        return getMapper().insertList(entitys);
    }

    public Integer updateData(T entity) {
        return getMapper().updateByPrimaryKeySelective(entity);
    }

    public int updateListSelectiveData(List<T> entitys) {
        return getMapper().batchUpdateByPrimaryKeySelective(entitys);
    }

    public int batchUpdateByPrimaryKeySelective(List<T> entitys) {
        return getMapper().batchUpdateByPrimaryKeySelective(entitys);
    }

    public int deleteData(T entity) {
        return getMapper().delete(entity);
    }

    public int deleteListData(List<T> entitys) throws Exception {
        for (T entity : entitys) {
            int i = deleteData(entity);
            if (i == 0) {
                throw new Exception("删除失败！");
            }
        }
        return entitys.size();
    }

    public int deleteDataByKey(Long id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    public int deleteDataByKey(Object id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    public int deleteListDataByKey(List<Long> ids) throws Exception {
        for (Long id : ids) {
            int i = deleteDataByKey(id);
            if (i == 0) {
                throw new Exception("删除失败！");
            }
        }
        return ids.size();
    }

    public int deleteData(Long id) {
        return getMapper().deleteByPrimaryKey(id);
    }

    public int deleteByExample(Example example) {
        return getMapper().deleteByExample(example);
    }

    public List<T> listData(T entity) {
        return getMapper().select(entity);
    }

    public List<T> selectByExample(Example example) {
        return getMapper().selectByExample(example);
    }

    public List<T> select(T t) {
        return getMapper().select(t);
    }

    public List<T> selectAll() {
        return getMapper().selectAll();
    }

    public T selectByPrimaryKey(String id) {
        return getMapper().selectByPrimaryKey(id);
    }

    public T selectByPrimaryKey(Long id) {
        return getMapper().selectByPrimaryKey(id);
    }
}
