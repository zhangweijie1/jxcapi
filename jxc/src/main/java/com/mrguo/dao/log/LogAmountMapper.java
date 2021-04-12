package com.mrguo.dao.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.log.LogAmount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: LogAmountMapper
 * @Description: 资金日志logger
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/11 9:46 上午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Repository
public interface LogAmountMapper extends MyMapper<LogAmount> {

    /**
     * 自定义资金查询
     *
     * @param page 分页
     * @param data 查询参数
     * @return
     */
    List<Map<String, Object>> listCustom(Page<Map<String, Object>> page,
                                         @Param("record") Map<String, Object> data);
}