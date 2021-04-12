package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.vo.basedata.StoreVo;
import com.mrguo.entity.bsd.Store;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("storeMapper")
public interface StoreMapper extends MyMapper<Store> {


    int deleteByIds(List<String> storeIds);

    /**
     * 自定义查询
     *
     * @param storeVo
     * @return
     */
    List<StoreVo> listPage(Page<StoreVo> page, @Param("record") StoreVo storeVo);

    List<StoreVo> listAllData();

    StoreVo selectById(@Param("storeId") Long id);

    /**
     * 仓库qty排名
     *
     * @return
     */
    List<Map<String,Object>> listStockDataGroupByStore(@Param("record") Map data);

    @Select("select count(1) from bsd_store")
    int countStore();

}