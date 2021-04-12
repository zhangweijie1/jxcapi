package com.mrguo.common.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class PageParams<T> implements Serializable {

    /**
     * mybatis-plus的page分页
     */
    private Page<T> page;

    /**
     * 查询参数
     */
    private Map<String, Object> data;
}
