package com.mrguo.service.impl.basedata;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bsd.CustomerSupplierMapper;
import com.mrguo.entity.bsd.CustomerSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


/**
 * 客户Service实现类
 *
 * @author mrguo
 */
@Service("customerSupplierService")
public class CustomerSupplierServiceImpl extends BaseServiceImpl<CustomerSupplier> {

    @Autowired
    private CustomerSupplierMapper customerSupplierMapper;

    @Override
    public MyMapper<CustomerSupplier> getMapper() {
        return customerSupplierMapper;
    }

    public void saveOrUpdatebycustomer(CustomerSupplier customerSupplier) {
        CustomerSupplier params = new CustomerSupplier();
        params.setCustomerId(customerSupplier.getCustomerId());
        CustomerSupplier customerSupplierR = customerSupplierMapper.selectOne(params);
        if (customerSupplierR == null) {
            customerSupplierMapper.insert(customerSupplier);
        } else {
            Example example = new Example(CustomerSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("customerId", customerSupplier.getCustomerId());
            customerSupplierMapper.updateByExampleSelective(customerSupplier, example);
        }
    }

    public void saveOrUpdateBySupplier(CustomerSupplier customerSupplier) {
        CustomerSupplier params = new CustomerSupplier();
        params.setSupplierId(customerSupplier.getSupplierId());
        CustomerSupplier customerSupplierR = customerSupplierMapper.selectOne(params);
        if (customerSupplierR == null) {
            customerSupplierMapper.insert(customerSupplier);
        } else {
            Example example = new Example(CustomerSupplier.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("supplierId", customerSupplier.getSupplierId());
            customerSupplierMapper.updateByExampleSelective(customerSupplier, example);
        }
    }

}
