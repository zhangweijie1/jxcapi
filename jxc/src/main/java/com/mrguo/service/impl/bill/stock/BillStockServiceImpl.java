package com.mrguo.service.impl.bill.stock;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bill.BillStockMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillStock;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.uils.BillUtilsGenerator;
import com.mrguo.util.enums.BillCatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 郭成兴
 * @ClassName BillStockServiceImpl
 * @Description 出入库单据
 * @date 2019/12/314:57 PM
 * @updater 郭成兴
 * @updatedate 2019/12/314:57 PM
 */
@Service
public
class BillStockServiceImpl extends BaseServiceImpl<BillStock> {

    @Autowired
    private BillStockMapper billStockMapper;
    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;
    @Autowired
    private BillDetailServiceImpl billDetailService;

    @Override
    public MyMapper<BillStock> getMapper() {
        return billStockMapper;
    }

    /**
     * 新增一个单据
     *
     * @param billStock
     * @param billDetailList
     * @throws CustomsException
     */
    void saveStockData(BillStock billStock, List<BillDetail> billDetailList) throws CustomsException {
        billValidationServiceImpl.valiBusinessTime(billStock.getBusinessTime());
        billValidationServiceImpl.valiOperPermission(billStock.getStoreId());
        // 新增入库表本身
        Long billId = IDUtil.getSnowflakeId();
        billStock.setId(billId);
        String goodsNameByDetails = BillUtilsGenerator.getBillSkuNamesComputer().getSkuNamesOfDetailList(billDetailList);
        billStock.setGoodsNamestr(goodsNameByDetails);
        if ("0".equals(billStock.getDirection())) {
            billStock.setBillCat(BillCatEnum.stock_out.getCode());
            billStock.setBillCatName(BillCatEnum.stock_out.getMessage());
        }
        if ("1".equals(billStock.getDirection())) {
            billStock.setBillCat(BillCatEnum.stock_in.getCode());
            billStock.setBillCatName(BillCatEnum.stock_in.getMessage());
        }
        billStock.setCreateTime(new Date());
        billStock.setUpdateTime(billStock.getCreateTime());
        super.saveData(billStock);
        for (BillDetail billDetail : billDetailList) {
            billDetail.setBillId(billId);
            billDetail.setChangeQuantity(BigDecimal.ZERO);
            billDetail.setChangeQuantity2(BigDecimal.ZERO);
            billDetail.setBillRelationId(billDetail.getId());
            billDetail.setId(IDUtil.getSnowflakeId());
        }
        billDetailService.insertListData(billDetailList);
    }

    void cancleBill(Long billId) throws CustomsException {
        BillStock bill = billStockMapper.selectByPrimaryKey(billId);
        billValidationServiceImpl.valiCanclePermission(bill.getCreateUserId());
        //
        BillStock billStock = new BillStock();
        billStock.setId(billId);
        billStock.setIsCancle("1");
        if (billStockMapper.updateByPrimaryKeySelective(billStock) < 1) {
            throw new CustomsException("作废失败！");
        }
    }

    public int countRelationIdByBillId(Long billId) {
        return billStockMapper.countRelationIdByBillId(billId);
    }

    public int countRelationIdByBillIdAndBillcat(Long billId, String billCat) {
        return billStockMapper.countRelationIdByBillIdAndBillCat(billId, billCat);
    }

    /**
     * 获取未进出库的商品
     *
     * @param billId
     * @return
     */
    List<BillDetail> getWaiteGoodsByBillId(Long billId) {
        return billDetailService.listWaiteTransDataByBillId(billId);
    }

    /**
     * 已进出库商品
     *
     * @param billId
     * @return
     */
    List<BillDetail> getHasStockGoodsByBillId(Long billId) {
        return billDetailService.listHasTransDataByBillId(billId);
    }

    Bill billStock2bill(BillStock billStock) {
        Bill bill = new Bill();
        bill.setId(billStock.getBillRelationId());
        bill.setBillStockId(billStock.getId());
        bill.setStoreId(billStock.getStoreId());
        bill.setBillNo(billStock.getBillNo());
        bill.setDirection(billStock.getDirection());
        bill.setBillCatName(billStock.getBillCatName());
        bill.setComegoId(billStock.getComegoId());
        bill.setComegoName(billStock.getComegoName());
        bill.setCreateTime(billStock.getCreateTime());
        bill.setBillRelationId(billStock.getBillRelationId());
        return bill;
    }
}
