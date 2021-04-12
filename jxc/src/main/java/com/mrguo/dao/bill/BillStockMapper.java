package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillStock;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description 和真实库存相关（表都是一个表）
 * @date 2020/1/22 8:35 PM
 * @updater 郭成兴
 * @updatedate 2020/1/22 8:35 PM
 */
@Repository("billStockMapper")
public interface BillStockMapper extends MyMapper<BillStock> {

    BillStock getBillByBillId(@Param("billId") Long billId);

    /**
     * 查询出入库单据
     *
     * @param data
     * @return
     */
    List<BillStock> getHasStockBillList(Page page, @Param("record") Map data);

    /**
     * 查询bill单据，被转化的count（判断是否被转化）
     *
     * @param billId
     * @return
     */
    int countRelationIdByBillId(@Param("billId") Long billId);

    int countRelationIdByBillIdAndBillCat(@Param("billId") Long billId,
                                @Param("billCat") String billCat);

    /**
     * 判断是否存在此单据号
     *
     * @param billNo
     * @return
     */
    @Select("select count(1) from t_bill_stock WHERE bill_no = #{billNo}")
    int countBybillNo(@Param("billNo") String billNo);

    /**
     * 查询待入库单( 采购单，调拨单(已出库))
     *
     * @param data
     * @return
     */
    List<Bill> selectWaiteStockInBillList(Page page, @Param("record") Map data);

    /**
     * 查询待出库单
     *
     * @param data
     * @return
     */
    List<Bill> selectWaiteStockOutBillList(Page page, @Param("record") Map data);
}