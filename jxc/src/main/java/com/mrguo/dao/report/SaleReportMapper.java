package com.mrguo.dao.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.goods.GoodsSku;
import com.mrguo.dto.report.SaleInfoReportDto;
import com.mrguo.dto.report.SaleReportDto;
import com.mrguo.dto.report.SaleReportParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName SaleReportMapper
 * @Description 销售报表
 * @date 2020/5/125:26 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:26 PM
 */
@Repository("saleReportMapper")
public interface SaleReportMapper {

    /**
     * 按商品分组，查询销售单
     *
     * @return
     */
    List<SaleReportDto> listDataGroupByGoodPage(Page<SaleReportDto> page,
                                                @Param("record") Map<String,Object> params);
    /**
     * 按客户分组，查询销售单
     *
     * @return
     */
    List<SaleReportDto> listDataGroupByComegoPage(Page<SaleReportDto> page,
                                                  @Param("record") Map<String,Object> params);
    /**
     * 按单据分组，查询销售单
     *
     * @return
     */
    List<SaleReportDto> listDataGroupByBillPage(Page<SaleReportDto> page,
                                                @Param("record") Map<String,Object> params);

    /**
     * 统计报表 by 商品 - 按照单据分组
     *
     * @param page
     * @param data
     * @return
     */
    List<SaleReportDto> listGoodDetailBySkuIdGroupByBillPage(Page<SaleReportDto> page,
                                               @Param("record") Map<String, Object> data);

    /**
     * 统计报表 by 商品 - 按照客户分组
     *
     * @param page
     * @param params
     * @return
     */
    List<SaleReportDto> listGoodDetailBySkuIdGroupByComegoPage(Page<SaleReportDto> page,
                                                 @Param("record") Map<String, Object> params);

    /**
     * 统计报表 by客户 - 按照商品分组
     *
     * @param page
     * @param params
     * @return
     */
    List<SaleReportDto> listComegoDetailByComegoIdGroupByGoodPage(Page<SaleReportDto> page,
                                                 @Param("record") Map<String, Object> params);

    /**
     * 统计报表 by客户 - 按照单据分组
     *
     * @param page
     * @param params
     * @return
     */
    List<SaleReportDto> listComegoDetailByComegoIdGroupByBillPage(Page<SaleReportDto> page,
                                                @Param("record") Map<String, Object> params);


    List<SaleReportDto> listBillDetailByDate(Page<SaleReportDto> page,
                                         @Param("record") Map<String, Object> params);


    /**
     * 查询销售信息
     *
     * @return
     */
    SaleInfoReportDto selectSalesInfo(@Param("record") SaleReportParam saleReportParam);

    /**
     * 查询最大销售额商品
     *
     * @return
     */
    GoodsSku selectMaxSaleAmountGood(@Param("record") SaleReportParam saleReportParam);

    /**
     * 查询最大销售额客户
     *
     * @return
     */
    Customer selectMaxSaleAmountCustomer(@Param("record") SaleReportParam saleReportParam);

}
