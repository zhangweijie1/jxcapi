package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.SupplierCat;
import com.mrguo.entity.bsd.Unit;
import com.mrguo.entity.bsd.UnitDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("unitMapper")
public interface UnitMapper extends MyMapper<Unit> {

    /**
     * 查询单位by names
     *
     * @return
     */
    List<Unit> listDataByNames(List<String> names);

    /**
     * 分页查询
     * @param page
     * @param data
     * @return
     */
    List<Unit> listPage(Page<Unit> page, @Param("record") Map<String, Object> data);

    /**
     *
     * @return
     */
    List<UnitDto> listOptions();

}