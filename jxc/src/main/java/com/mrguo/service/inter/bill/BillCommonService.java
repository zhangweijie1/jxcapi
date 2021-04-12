package com.mrguo.service.inter.bill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.util.enums.BillCatEnum;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BillCommonService
 * @Description: 1. 新增单据: addData
 * 2. 作废单据: cancle
 * 3. 查询单据: listPage
 * 4. 导出单据: export
 * 5. 打印单据: print
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/4 2:34 下午
 * @Copyright 南通市韶光科技有限公司
 **/
public interface BillCommonService {

    /**
     * 新增单据
     *
     * @param bill       单据
     * @param detailList 单据明细
     */
    void addData(Bill bill, List<BillDetail> detailList) throws CustomsException;

    /**
     * 作废单据
     *
     * @param billId 单据Id
     * @return
     */
    void cancle(Long billId) throws CustomsException;

    void close(Long billId) throws CustomsException;

    /**
     * 更新单据
     *
     * @param bill       单据
     * @param detailList 单据明细
     * @return
     */
    void updateBillAndDetails(Bill bill, List<BillDetail> detailList) throws CustomsException;

    /**
     * 获取单据编号
     *
     * @return
     */
    String getBillNo(BillCatEnum billCatEnum) throws CustomsException;

    /**
     * 获取单据BY单据Id
     *
     * @param billId 单据Id
     * @return
     */
    Bill getBillById(Long billId);

    /**
     * 查询单据和单据明细
     *
     * @param billId
     * @return
     */
    BillAndDetailsVo<Bill> getBillAndDetailsById(Long billId);

    /**
     * 查询待退回单据
     *
     * @param pageParams  查询参数
     * @param billCatEnum 单据类型
     * @return IPage
     */
    IPage<Bill> listWaiteReturnBillsPage(PageParams<Bill> pageParams, BillCatEnum billCatEnum);

    /**
     * 查询单据
     *
     * @param page 查询参数
     * @param data 查询参数
     * @return
     */
    IPage<Bill> listPage(Page<Bill> page, Map<String, Object> data);
}
