package com.mrguo.dao.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.log.LogDebt;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LogDebtMapper extends MyMapper<LogDebt> {

    /**
     * 自定义查询
     *
     * @param page
     * @return
     */
    public List<LogDebt> listCustom(Page page, @Param("record") Map data);
}