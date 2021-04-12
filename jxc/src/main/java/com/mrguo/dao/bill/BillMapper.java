package com.mrguo.dao.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bill.Bill;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("billMapper")
public interface BillMapper extends MyMapper<Bill> {

    /**
     * 自定义查询
     *
     * @param data 查询参数
     * @return
     */
    List<Bill> listCustom(Page<Bill> page, @Param("record") Map<String, Object> data);

    /**
     * 根据Id获取详情
     *
     * @param billId 单据Id
     * @return
     */
    Bill getDataById(Long billId);

    /**
     * 待退货的单据
     *
     * @param page    分页
     * @param billCat 单据类型
     * @return
     */
    List<Bill> listWaiteReturnBills(Page<Bill> page,
                                    @Param("billCat") String billCat);


    /**
     * 该单据编号的数量
     *
     * @param billNo 单据号
     * @return int
     */
    @Select("select count(1) from t_bill WHERE bill_no = #{billNo}")
    int countBybillNo(@Param("billNo") String billNo);

    /**
     * 查询单据的关联单据数量（比如，查询进货订单 关联的进货单的数量）
     *
     * @param billId
     * @return
     */
    int countRelationBillByBillId(@Param("billId") Long billId);

    /**
     * 查询单据的关联单据数量（跟billId，和billCat）
     *
     * @param billId
     * @param billCat
     * @return
     */
    int countRelationBillByBillIdAndCat(@Param("billId") Long billId,
                                        @Param("billCat") String billCat);

    int countByComegoId(@Param("comegoId") Long comegoId);

    int countByAccountId(@Param("accountId") Long accountId);

    int countByStoreIds(@Param("list") List<String> storeIds);
}