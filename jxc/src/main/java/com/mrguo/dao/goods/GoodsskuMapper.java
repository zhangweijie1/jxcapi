package com.mrguo.dao.goods;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.dto.goods.SkuInfoDto;
import com.mrguo.entity.excle.ExcelExportSkuVo;
import com.mrguo.entity.goods.GoodsSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("goodsskuMapper")
public interface GoodsskuMapper extends MyMapper<GoodsSku> {


    /**
     * 自定义查询
     *
     * @param data
     * @return
     */
    List<GoodsSku> listCustom(Page page, @Param("record") Map data);

    /**
     * 查询单个仓库sku包含库存信息
     *
     * @param data
     * @return
     */
    List<GoodsSku> selectContainerStockByStore(@Param("page") Page page,
                                               @Param("record") Map data);

    /**
     * 查询商品更多信息包含unit，price等
     *
     * @param page
     * @param data
     * @return
     */
    List<GoodsSku> listDataMore(@Param("page") Page page,
                                @Param("record") Map data);

    /**
     * 导出数据
     *
     * @param data
     * @return
     */
    List<ExcelExportSkuVo> exportExcleData(@Param("record") Map data);

    /**
     * 热销商品
     *
     * @param page
     * @param data
     * @return
     */
    List<GoodsSku> selectHotSaleData(Page page, @Param("record") Map data);

    /**
     * 批量更新sku的成本价
     *
     * @param goodsSkuList
     * @return
     */
    int updatePriceCostBySkus(@Param("list") List<GoodsSku> goodsSkuList);

    /**
     * 查询sku的详情信息
     * 包含 价格，单位，库存等
     *
     * @param skuId
     * @return
     */
    GoodsSku selectAllDetailById(@Param("skuId") Long skuId);

    GoodsSku selectByBarcode(@Param("barcode") String barcode);

    List<String> selectCodesByCodeList(List<String> codes);

    List<GoodsSku> selectIdCodeListByCodeList(List<String> codes);

    /**
     * 查询sku的stock等信息
     *
     * @param skuId
     * @return
     */
    List<SkuInfoDto> selectAllStockBySkuId(@Param("skuId") Long skuId);

    @Select("select count(1) from bsd_goods_sku where code = #{code}")
    int countByCode(@Param("code") String code);

    @Select("select count(1) from bsd_goods_sku where cat_id = #{catId}")
    int countByCatId(@Param("catId") String catId);
}