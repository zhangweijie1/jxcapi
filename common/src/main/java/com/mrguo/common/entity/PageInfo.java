package com.mrguo.common.entity;

/**
 * @ClassName PageInfo
 * @Description 前后台分页排序对象
 * @Author 沈晨延
 * @Date 2019-01-12 17:01
 * Version 1.0
 **/
public class PageInfo {
    private Integer page = 1;
    private Integer rows = 10;
    private String sort;
    private String order;
    private long totalCount = 0L;

    public Integer getPage() {
        return (page == null || page == 0) ? 1 : page;
    }

    public Integer getRows() {
        return (rows == null || rows == 0) ? 10 : rows;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
