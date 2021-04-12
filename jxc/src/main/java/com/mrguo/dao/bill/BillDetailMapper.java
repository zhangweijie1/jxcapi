package com.mrguo.dao.bill;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.bill.BillDetailStock;
import com.mrguo.entity.goods.GoodsUnit;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("billdetailMapper")
public interface BillDetailMapper extends MyMapper<BillDetail> {

    /**
     * 查询明细by masterId
     *
     * @param billId
     * @return
     */
    @Select("select * from t_bill_detail where bill_id = #{billId}")
    List<BillDetail> selectListDataByBillId(Long billId);

    /**
     * 查询明细by masterId
     * 查询到的更多信息：单位，价格等
     *
     * @param billId
     * @return
     */
    List<BillDetail> selectMoreListDataByBillId(@Param("billId") Long billId);

    /**
     * 删除billId的明细
     *
     * @param billId
     */
    @Delete("delete from t_bill_detail where bill_id = #{billId}")
    int delDataByBillId(@Param("billId") Long billId);


    /**
     * 查询bill明细里，转化量
     *
     * @param billId
     * @return
     */
    @Select("select sum(ifnull(change_quantity,0)) from t_bill_detail where bill_id = #{billId}")
    int selectChangeQtyByBillId(@Param("billId") Long billId);

    /**
     * 查询没被转化的票据明细
     *
     * @param billId
     * @return
     */
    List<BillDetail> listNotTransDetailByBillId(Long billId);

    /**
     * 查询已经被转化的票据明细
     *
     * @param billId
     * @return
     */
    List<BillDetail> listHasTransDetailByBillId(@Param("billId") Long billId);

    @Select("select count(1) from t_bill_detail where sku_id = #{skuId}")
    int countBySkuId(@Param("skuId") Long skuId);

    List<Long> selectUnitsBySkuIdUnitIds(@Param("skuId") Long skuId,
                                         @Param("list") List<String> unitIds);
}