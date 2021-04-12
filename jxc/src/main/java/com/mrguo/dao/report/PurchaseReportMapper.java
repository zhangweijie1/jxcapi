package com.mrguo.dao.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.entity.goods.GoodsSku;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.dto.report.PurchaseInfoReportDto;
import com.mrguo.dto.report.PurchaseReportDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName SaleReportMapper
 * @Description 进货报表
 * @date 2020/5/125:26 PM
 * @updater 郭成兴
 * @updatedate 2020/5/125:26 PM
 */
@Repository("purchaseReportMapper")
public interface PurchaseReportMapper {

    /**
     * 按商品分组，查询进货单
     *
     * @return
     */
    List<PurchaseReportDto> listDataGroupByGoodPage(Page<PurchaseReportDto> page,
                                                    @Param("record") Map<String, Object> params);

    /**
     * 按供货商分组，查询进货单
     *
     * @return
     */
    List<PurchaseReportDto> listDataGroupByComegoPage(Page<PurchaseReportDto> page,
                                                      @Param("record") Map<String, Object> params);

    /**
     * 按单据分组，查询进货单
     *
     * @return
     */
    List<PurchaseReportDto> listDataGroupByBillPage(Page<PurchaseReportDto> page,
                                                    @Param("record") Map<String, Object> params);

    /**
     * 统计报表 by 商品 - 按照单据分组
     *
     * @param page
     * @param params
     * @return
     */
    List<PurchaseReportDto> listGoodDetailBySkuIdGroupByBillPage(Page<PurchaseReportDto> page,
                                                                 @Param("record") Map<String, Object> params);

    /**
     * 统计报表 by 商品 - 按照客户分组
     *
     * @param page
     * @param params
     * @return
     */
    List<PurchaseReportDto> listGoodDetailBySkuIdGroupByComegoPage(Page<PurchaseReportDto> page,
                                                                   @Param("record") Map<String, Object> params);

    /**
     * 统计报表 by客户 - 按照商品分组
     *
     * @param page
     * @param params
     * @return
     */
    List<PurchaseReportDto> listComegoDetailByComegoIdGroupByGoodPage(Page<PurchaseReportDto> page,
                                                                      @Param("record") Map<String, Object> params);

    /**
     * 统计报表 by客户 - 按照单据分组
     *
     * @param page
     * @param params
     * @return
     */
    List<PurchaseReportDto> listComegoDetailByComegoIdGroupByBillPage(Page<PurchaseReportDto> page,
                                                                      @Param("record") Map<String, Object> params);

    /**
     * 查询某天的单据
     * @param page
     * @param params
     * @return
     */
    List<PurchaseReportDto> listBillDetailByDatePage(Page<PurchaseReportDto> page,
                                                     @Param("record") Map<String, Object> params);


    /**
     * ============== 查询进货信息 ============
     *
     * @return
     */
    PurchaseInfoReportDto selectPurchasesInfo(@Param("record") Map<String, Object> params);

    /**
     * 查询最大进货额商品
     *
     * @return
     */
    GoodsSku selectMaxPurchaseAmountGood(@Param("record") Map<String, Object> params);

    /**
     * 查询最大供货额供应商
     *
     * @return
     */
    Supplier selectMaxPurchaseAmountComego(@Param("record") Map<String, Object> params);

}
