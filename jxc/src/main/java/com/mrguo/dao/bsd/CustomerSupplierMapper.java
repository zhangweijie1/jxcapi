package com.mrguo.dao.bsd;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.entity.bsd.CustomerSupplier;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

@Repository("customerSupplierMapper")
public interface CustomerSupplierMapper extends MyMapper<CustomerSupplier> {

    @Delete("delete from bsd_customer_supplier where customer_id = #{id}")
    int deleteByCustomerId(Long id);

    @Delete("delete from bsd_customer_supplier where supplier_id = #{id}")
    int deleteBySupplierId(Long id);
}