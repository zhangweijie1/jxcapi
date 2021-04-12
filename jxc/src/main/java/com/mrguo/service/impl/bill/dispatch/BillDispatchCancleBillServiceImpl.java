package com.mrguo.service.impl.bill.dispatch;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bill.BillDispatchMapper;
import com.mrguo.entity.bill.*;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockUpdateServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.stock.BillStockServiceImpl;
import com.mrguo.util.enums.BillCatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/4/165:43 PM
 * @updater 郭成兴
 * @updatedate 2020/4/165:43 PM
 */
@Service
public class BillDispatchCancleBillServiceImpl extends BaseServiceImpl<BillDispatch> {

    @Autowired
    private BillCommonServiceImpl billService;
    @Autowired
    private BillStockServiceImpl billStockService;
    @Autowired
    private GoodsStockUpdateServiceImpl goodsStockUpdateService;
    @Autowired
    private BillDispatchServiceImpl billDispatchService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillExtDispatchServiceImpl billExtDispatchService;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private BillDispatchMapper billDispatchMapper;

    @Override
    public MyMapper<BillDispatch> getMapper() {
        return billDispatchMapper;
    }

    /**
     * 作废调拨单（先出库，后入库）
     *
     * @param billId
     * @return
     * @throws CustomsException
     */
    public Result cancleBill(Long billId) throws CustomsException {
        if (billStockService.countRelationIdByBillId(billId) > 0) {
            throw new CustomsException("该单据已关联出库单，请先作废对应的出库单！");
        }
        // 修改goodsStock的waiteOut
        BillExtDispatch billExtDispatch = billExtDispatchService.selectByPrimaryKey(billId);
        List<BillDetail> detailList = billDetailService.listDataByBillId(billId);
        goodsStockUpdateService.updateStockWaite(billExtDispatch.getStoreIdOut(),
                detailList, "out", "sub");
        // 修改is_cancle
        Bill bill = new Bill();
        bill.setId(billId);
        bill.setIsCancle("1");
        if (billService.updateData(bill) > 0) {
            return Result.okmsg("作废成功！");
        }
        return Result.fail("作废失败");
    }

    /**
     * 作废调拨入库单
     * 1. goods_stock: 待入库减少
     * 2. 入库bill is_cancle = "1"
     *
     * @param masterBillId 主bill的billId
     * @return
     * @throws CustomsException
     */
    public Result cancleDispatchIn(Long masterBillId) throws CustomsException {
        valiDispatchIn(masterBillId);
        // 1. goods_stock 待入库减少
        // sroreInId, billDetailList
        BillExtDispatch billExtDispatch = billExtDispatchService.selectByPrimaryKey(masterBillId);
        Long storeIdIn = billExtDispatch.getStoreIdIn();
        // billDetailList
        BillDispatch billDispatchIn = billDispatchService.getDispatchInByMasterId(masterBillId);
        Long dispatchInId = billDispatchIn.getId();
        List<BillDetail> detailDispatchInList = billDetailService.listDataByBillId(dispatchInId);
        // 查询库存
        List<Long> skuIds = detailDispatchInList.stream().map(BillDetail::getSkuId).distinct().collect(Collectors.toList());
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, storeIdIn);
        if (skuIds.size() != goodsStockList.size()) {
            throw new BusinessException("有商品没有库存信息");
        }
        for (GoodsStock goodsStock : goodsStockList) {
            for (BillDetail detail : detailDispatchInList) {
                BigDecimal waitQuantityIn = goodsStock.getWaitQuantityIn();
                BigDecimal qty = detail.getQuantity();
                BigDecimal targetQty = waitQuantityIn.subtract(qty);
                goodsStock.setWaitQuantityIn(targetQty);
            }
        }
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockList);
        return Result.ok();
    }

    /**
     * 如果调拨入库已经有入库单，则不能作废
     */
    private void valiDispatchIn(Long masterBillId) throws CustomsException {
        if (billStockService.countRelationIdByBillIdAndBillcat(masterBillId,
                BillCatEnum.stock_in.getCode()) > 0) {
            throw new CustomsException("已有关联入库单，不能作废！");
        }
    }
}
