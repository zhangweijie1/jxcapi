package com.mrguo.dao.fin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.fin.FinBill;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("finBillMapper")
public interface FinBillMapper extends MyMapper<FinBill> {

    /**
     * 查询billNo的数量
     *
     * @param billNo
     * @return
     */
    @Select("select count(1) from fin_bill where bill_no = #{billNo}")
    int countBybillNo(String billNo);

    /**
     * 查询客户欠款
     *
     * @param page
     * @return
     */
    List<FinBill> selectDebtReceiptGroup(Page<FinBill> page);

    /**
     * 查询客户欠款 - 明细
     *
     * @return
     */
    List<FinBill> selectDebtReceiptDetailByMaster(Map<String, Object> finBill);

    /**
     * 自定义查询
     *
     * @param finBill
     * @return
     */
    List<FinBill> listCustom(Page<FinBill> page,
                             @Param("record") Map<String, Object> finBill);

    @Select("select count(1) from fin_bill where capital_cat = #{capitalCat}")
    int countByCapitalCat(@Param("capitalCat") Long capitalCat);
}