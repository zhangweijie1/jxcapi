package com.mrguo.service.inter.bill;

import com.mrguo.entity.bill.BillDetail;

import java.util.List;

/**
 * 单据明细的方法
 */
public interface BillDetailService {


    /**
     * 获取单据已经转化的数量
     *
     * @param billId
     * @return
     */
    Integer getChangeQtyByBillId(Long billId);

    /**
     * 查询某单据的明细
     *
     * @param billId
     * @return
     */
    List<BillDetail> listDataByBillId(Long billId);

    /**
     * 查询某单据的明细( 包含更多信息 )
     *
     * @param billId
     * @return
     */
    List<BillDetail> listMoreDataByBillId(Long billId);

    /**
     * 查询待转化的单据明细
     *
     * @param billId
     * @return
     */
    List<BillDetail> listWaiteTransDataByBillId(Long billId);

    /**
     * 查询已经转化的单据明细
     *
     * @param billId
     * @return
     */
    List<BillDetail> listHasTransDataByBillId(Long billId);


}
