package com.mrguo.dao.bsd;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.entity.bill.Bill;
import com.mrguo.service.impl.excle.ExcleCustomerData;
import com.mrguo.service.impl.excle.ExcleSupplierData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName SupplierMapper
 * @Description 供应商Repository接口
 * @date 2019/8/5 6:43 PM
 * @updater 郭成兴
 * @updatedate 2019/8/5 6:43 PM
 */
@Repository("supplierMapper")
public interface SupplierMapper extends MyMapper<Supplier> {

    /**
     * 取消默认
     *
     * @return
     */
    @Update("update bsd_supplier set is_default='0'")
    int cancleDefault();

    @Select(value = "select * from bsd_supplier where name like #{keywords} or remarks like #{keywords} or address like #{keywords} or contacter like #{keywords}")
    List<Supplier> findByName(String keywords);

    List<Supplier> listOptions(String keywords);

    List<Supplier> listPage(Page<Supplier> page,
                            @Param("record") Supplier supplier);

    List<ExcleSupplierData> exportData();

    /**
     * 报表统计
     *
     * @param bill
     * @return
     */
    List<Bill> statistics(Page<Bill> page, Bill bill);

    /**
     * 查询bill单据中，是否有该供应商
     *
     * @param supplierId
     * @return
     */
    @Select("select count(1) from t_bill where comego_id = #{supplierId}")
    int countInBill(@Param("supplierId") Long supplierId);

    @Select("select s.*, c.id as customer_id, c.code as customer_code, c.name as customer_name from bsd_supplier s\n" +
            "left join bsd_customer_supplier cs on cs.supplier_id = s.id\n" +
            "left join bsd_customer c on c.id = cs.customer_id\n" +
            "where s.id = #{id}")
    Supplier findById(Long id);

    @Select("select debt from origin_comego_debt\n" +
            "where id = #{comegoId}\n" +
            "order by create_time desc\n" +
            "LIMIT 1")
    BigDecimal getOriginDebtByComegoId(Long comegoId);

    @Select("select count(1) from bsd_supplier where cat_id = #{catId}")
    int countByCatId(@Param("catId") String catId);

    @Select("select count(1) from bsd_supplier where code = #{code}")
    int countByCode(@Param("code") String code);
}