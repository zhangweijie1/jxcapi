package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.Level;
import com.mrguo.dto.Option;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("levelMapper")
public interface LevelMapper extends MyMapper<Level> {

    List<Level> listPage(IPage<Level> page,
                           @Param("data") Map<String, Object> data);

    List<Option> listOptions(@Param("data") Map<String, Object> data);

    /**
     * 获取系统内置等级
     *
     * @return
     */
    Level selectSysData();

    int countByCode(@Param("code") String code);

}