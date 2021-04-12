package com.mrguo.dao.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.dto.report.PurchaseSaleCompareReportDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName SalePurchaseCompareReportMapper
 * @Description 进销对比统计报表
 * @date 2020/5/125:26 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:26 PM
 */
@Repository("purchaseSaleCompareReportMapper")
public interface PurchaseSaleCompareReportMapper {

    List<PurchaseSaleCompareReportDto> listCompare(Page<PurchaseSaleCompareReportDto> page,
                                                   @Param("record") Map<String,Object> map);
}
