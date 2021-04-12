
package com.mrguo.common.dao.base;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Date;
import java.util.Set;

/**
 * BaseUpdateProvider实现类，基础方法实现类
 *
 * @author liuzh
 */
public class BatchUpdateProvider extends MapperTemplate{


    public BatchUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 通过主键批量更新全部字段
     *
     * @param ms
     * @return String [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public String batchUpdateByPrimaryKey(MappedStatement ms)
    {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));

        // 获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        // 获取主键列
        Set<EntityColumn> pkList = EntityHelper.getPKColumns(entityClass);
        sql.append("<trim prefix=\"SET\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList)
        {
            if (!column.isId() && column.isUpdatable())
            {
                if (("lastmodifytime".equalsIgnoreCase(column.getColumn())
                        || "lastupdatetime".equalsIgnoreCase(column.getColumn())) && null != column.getJavaType()
                        && Date.class.isAssignableFrom(column.getJavaType()))
                {
                    // 最后更新时间使用数据库当前时间now()填充
                    sql.append(column.getColumn() + " = now()" + ",");
                }
                else
                {
                    sql.append("<trim prefix=\" " + column.getColumn() + " = CASE\" suffix=\" END,\">");
                    sql.append("<foreach collection=\"list\" item=\"updateItem\">");
                    sql.append("<trim prefix=\" WHEN \" suffix=\" THEN \" suffixOverrides=\" AND \">");
                    for (EntityColumn pk : pkList)
                    {
                        sql.append(pk.getColumnEqualsHolder("updateItem") + " AND ");
                    }
                    sql.append("</trim>");
                    sql.append(column.getColumnHolder("updateItem"));
                    sql.append("</foreach>");
                    sql.append("</trim>");
                }
            }
        }
        sql.append("</trim>");

        // where条件
        sql.append("<where>");
        sql.append(
                "<foreach collection=\"list\" item=\"updateItem\" open=\"(\" close=\")\" separator=\"OR\">");
        sql.append("<trim prefix=\"\" suffix=\"\" suffixOverrides=\" AND \">");
        for (EntityColumn pk : pkList)
        {
            sql.append(pk.getColumnEqualsHolder("updateItem") + " AND ");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        sql.append("</where>");
        return sql.toString();
    }

    /**
     * 通过主键批量更新不为null的字段
     *
     * @param ms
     * @return String [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public String batchUpdateByPrimaryKeySelective(MappedStatement ms)
    {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));

        // 获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        // 获取主键列
        Set<EntityColumn> pkList = EntityHelper.getPKColumns(entityClass);
        sql.append("<trim prefix=\"SET\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList)
        {
            if (!column.isId() && column.isUpdatable())
            {
                if (("lastmodifytime".equalsIgnoreCase(column.getColumn())
                        || "lastupdatetime".equalsIgnoreCase(column.getColumn())) && null != column.getJavaType()
                        && Date.class.isAssignableFrom(column.getJavaType()))
                {
                    // 最后更新时间使用数据库当前时间now()填充
                    sql.append(column.getColumn() + " = now()" + ",");
                }
                else
                {
                    sql.append("<trim prefix=\" " + column.getColumn() + " = CASE\" suffix=\" END,\">");
                    sql.append("<foreach collection=\"list\" item=\"updateItem\">");
                    sql.append("<trim prefix=\" WHEN \" suffix=\" THEN \" suffixOverrides=\" AND \">");
                    for (EntityColumn pk : pkList)
                    {
                        sql.append(pk.getColumnEqualsHolder("updateItem") + " AND ");
                    }
                    sql.append("</trim>");
                    sql.append("COALESCE(" + column.getColumnHolder("updateItem") + "," + column.getColumn() + ")");
                    // 参数值不为空则进行修改
                    // SqlHelper.getIfNotNull("updateItem", column, column.getColumnHolder("updateItem"), isNotEmpty());
                    // 参数值为空则赋予字段原来的值
                    // SqlHelper.getIfIsNull("updateItem", column, column.getColumn(), isNotEmpty());
                    sql.append("</foreach>");
                    sql.append("</trim>");
                }
            }
        }
        sql.append("</trim>");

        // where条件
        sql.append("<where>");
        sql.append(
                "<foreach collection=\"list\" item=\"updateItem\" open=\"(\" close=\")\" separator=\"OR\">");
        sql.append("<trim prefix=\"\" suffix=\"\" suffixOverrides=\" AND \">");
        for (EntityColumn pk : pkList)
        {
            sql.append(pk.getColumnEqualsHolder("updateItem") + " AND ");
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        sql.append("</where>");
        return sql.toString();
    }
}
