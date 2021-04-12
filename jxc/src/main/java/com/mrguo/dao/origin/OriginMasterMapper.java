package com.mrguo.dao.origin;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.origin.OriginMaster;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("originMasterMapper")
public interface OriginMasterMapper extends MyMapper<OriginMaster> {

    int count();

    OriginMaster selectLastData();

    Map<String,Object> selectBalanceTime();

    List<OriginMaster> selectOriginAll(Page page);
}