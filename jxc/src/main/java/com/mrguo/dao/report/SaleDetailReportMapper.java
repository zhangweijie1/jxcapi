package com.mrguo.dao.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.dto.report.SaleDetailReportDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName SaleReportMapper
 * @Description 销售明细报表
 * @date 2020/5/125:26 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:26 PM
 */
@Repository("saleDetailReportMapper")
public interface SaleDetailReportMapper {

    /**
     * 查询销售明细报表
     *
     * @return
     */
    List<SaleDetailReportDto> listSaleDetail(Page page, @Param("record") Map<String,Object> data);
}
