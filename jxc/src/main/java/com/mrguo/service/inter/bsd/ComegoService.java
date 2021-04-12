package com.mrguo.service.inter.bsd;

import java.io.IOException;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.bsd.Customer;
import jxl.write.WriteException;

/**
 * 往来单位Service接口
 *
 * @author mrguo
 */
public interface ComegoService {

    /**
     * 新增或修改
     *
     * @param customer
     * @return
     * @throws CustomsException
     */
    Result saveOrUpdateData(Customer customer) throws CustomsException;

    /**
     * 根据id删除客户
     *
     * @param id
     */
    Result deleteById(Long id);

    /**
     * 根据名称模糊查询客户信息
     *
     * @param name
     * @return
     */
    Result listDataByName(String name);

    /**
     * 根据条件分页查询客户信息
     *
     * @return
     */
    Result listPage(PageParams<Customer> pageParams);

    Result listOptions(String keywords);

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    Result getDataById(Long id);

    /**
     * 获取客户的欠款
     *
     * @param id
     * @return
     */
    Result getDebtById(Long id) throws CustomsException;

    /**
     * 获取编号
     *
     * @return
     */
    Result getCode();

    /**
     * 获取excle模板
     *
     * @return
     * @throws IOException
     * @throws WriteException
     */
    Result getExcleTemp() throws IOException, WriteException;

    /**
     * 导出数据
     *
     * @return
     * @throws IOException
     * @throws WriteException
     */
    Result exportData() throws IOException, WriteException;

    /**
     * 导入数据
     *
     * @return
     * @throws IOException
     * @throws WriteException
     */
    Result importData() throws IOException, WriteException;

    /**
     * 清空数据
     *
     * @return
     */
    Result clearAllData();
}
