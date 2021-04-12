package com.mrguo.dao.bsd;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Store;
import com.mrguo.dto.OptionStore;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("storeOptionsMapper")
public interface StoreOptionsMapper extends MyMapper<Store> {
    /**
     * 获取所有的store
     *
     * @return
     */
    List<OptionStore> selectStoreAllOptions();

}