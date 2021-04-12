package com.mrguo.dao.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.dto.report.ProfitPay;
import com.mrguo.dto.report.ProfitRece;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/1710:23 AM
 * @updater 郭成兴
 * @updatedate 2020/7/1710:23 AM
 */
@Repository("reportFinMapper")
public interface ReportFinMapper {

    /**
     * 收入
     *
     * @param data
     * @return
     */
    ProfitRece selectRece(@Param("record") Map data);

    /**
     * 支出
     *
     * @param data
     * @return
     */
    ProfitPay selectPay(@Param("record") Map data);

    /**
     * 业绩查询
     *
     * @param data
     * @return
     */
    List<Map<String, Object>> selectPerformanceGroupByHandUserId(Page page, @Param("record") Map data);

    /**
     * 业绩最大员工
     *
     * @param data
     * @return
     */
    Map<String, Object> selectMaxSaleHandUser(@Param("record") Map data);

    /**
     * 查询销售count数据
     *
     * @param data
     * @return
     */
    Map<String, Object> selectCountSaleBill(@Param("record") Map data);
}
