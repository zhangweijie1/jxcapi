package com.mrguo.service.inter.bill;


import com.mrguo.util.enums.BillCatEnum;

public interface BillCountService {

    /**
     * 统计By往来单位Id
     * @param comegoId
     * @return
     */
    Integer countByComegoId(Long comegoId);

    /**
     * 统计by账户Id
     *
     * @return
     */
    Integer countByAccountId(Long accountId);

    /**
     * Count该单据的关联单据的数量（比如：查询进货订单，关联的进货单的数量）
     *
     * @param billId
     * @return
     */
    Integer countRelationBillByBillId(Long billId);

    /**
     * Count该单据的关联单据的数量（比如：查询进货订单，关联的进货单的数量）
     *
     * @param billId
     * @return
     */
    Integer countRelationBillByBillIdAndCat(Long billId, BillCatEnum catEnum);
}
