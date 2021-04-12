package com.mrguo.service.inter.bsd;

import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.vo.goods.GoodsAssemblyVo;
import jxl.Sheet;
import jxl.write.WriteException;

import java.io.IOException;

/**
 * 商品Service接口
 *
 * @author mrguo
 */
public interface GoodsService {

    /**
     * 新增商品Dto
     *
     * @param goodsAssemblyVo
     * @return
     */
    Result addGoodAssemblyData(GoodsAssemblyVo goodsAssemblyVo) throws Exception;

    /**
     * 导入数据
     *
     * @param sheet
     * @return
     */
    Result importData(Sheet sheet) throws Exception;

    /**
     * 导出数据
     *
     * @return
     * @throws Exception
     */
    Result exportData(Long storeId) throws Exception;

    Result createExcleTemp(Integer specsQty, Integer unitQty) throws IOException, WriteException;

    void clearAllData() throws CustomsException;
}
