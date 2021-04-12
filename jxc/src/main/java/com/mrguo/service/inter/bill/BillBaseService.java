package com.mrguo.service.inter.bill;

import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;

/**
 * 默认的接口
 * 1. 新增单据: addData
 * 2. 作废单据: cancle
 * 3. 查询单据: listPage
 * 4. 导出单据: export
 * 5. 打印单据: print
 */
public interface BillBaseService<T> {

    /**
     * 作废单据
     *
     * @param billId
     * @return
     */
    Result cancle(Long billId) throws CustomsException;

    /**
     * 获取单据编号
     *
     * @return
     */
    Result getBillNo() throws CustomsException;

    /**
     * 查询单据
     *
     * @param pageParams 查询参数
     * @return
     */
    Result listPage(PageParams<T> pageParams);

    /**
     * 导出单据
     *
     * @param billId 单据Id
     * @return
     */
    Result export(Long billId);

    /**
     * 打印单据
     *
     * @param billId 单据Id
     * @return
     */
    Result print(Long billId);
}
