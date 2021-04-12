package com.mrguo.service.impl.bill.stock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.dao.bill.BillDispatchMapper;
import com.mrguo.dao.bill.BillStockMapper;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.*;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.service.impl.basedata.goods.GoodsStockUpdateServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillDetailExtDispatchServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillDispatchCancleBillServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillDispatchServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillExtDispatchServiceImpl;
import com.mrguo.service.impl.bill.uils.*;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
class BillStockOutServiceImpl extends BaseServiceImpl<BillStock> {

    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    private BillStockMapper billStockMapper;
    @Autowired
    private BillStockServiceImpl billStockService;
    @Autowired
    private BillHandleRelationQtyServiceImpl billHandleRelationService;
    @Autowired
    private BillDispatchCancleBillServiceImpl billDispatchCancleBillService;
    @Autowired
    private GoodsStockUpdateServiceImpl goodsStockUpdateService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillDispatchServiceImpl billDispatchService;
    @Autowired
    private BillDispatchMapper billDispatchMapper;
    @Autowired
    private BillExtDispatchServiceImpl billExtDispatchService;
    @Autowired
    private BillDetailExtDispatchServiceImpl billDetailExtDispatchService;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;


    @Override
    public MyMapper<BillStock> getMapper() {
        return billStockMapper;
    }

    /**
     * 新增出库单
     * 不需要写入库存日志（以为会根据业务单据关联到进出库）
     * 1. 新增单据和明细
     * 2. 库存，库存日志
     * 3. 关联单据的change_qty
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addStockOut(BillAndDetailsVo<BillStock> billAndDetailsVo) throws CustomsException {
        BillStock billStock = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        String direction = "0";
        // 处理关联单据
        for (BillDetail billdetail : billDetailList) {
            billdetail.setChangeQuantity(BigDecimal.ZERO);
            billdetail.setBillRelationId(billdetail.getId());
        }
        Bill bill = billStockService.billStock2bill(billStock);
        billHandleRelationService.handleRelationChangeQty(bill.getBillRelationId(), billDetailList);
        // 1 新增票据+明细
        billStock.setDirection(direction);
        billStock.setBillNo(billOrderNoService.genStockBillCode(billStock.getBillNo(),
                BillCatEnum.stock_out.getCode()));
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        billStock.setQuantity(billSkuQtyComputer.getQtyOfBaseUnitInDetails(billDetailList));
        billStockService.saveStockData(billStock, billDetailList);
        // 2 stock表
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, billStock.getStoreId());
        GoodsStockAndLog goodsStockDto = getGoodsStockOut(billStock, goodsStockList, billDetailList);
        List<GoodsStock> goodsStockListParams = goodsStockDto.getGoodsStockList();
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockListParams);
        return Result.ok();
    }

    /**
     * 出库单（调拨单的）
     * 区别：
     * 出库时，需要新增一个调拨入库单
     *
     * @param billAndDetailsVo
     * @return
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addStockOutDispatch(BillAndDetailsVo<BillStock> billAndDetailsVo) throws CustomsException {
        BillStock billStock = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        String direction = "0";
        // 处理关联单据
        List<BillDetailExtDispatch> detailExtDispatchList = billDetailExtDispatchService.getListDataByBillId(billStock.getBillRelationId());
        for (BillDetail detailParams : billDetailList) {
            for (BillDetailExtDispatch detailPost : detailExtDispatchList) {
                if (detailPost.getId().equals(detailParams.getId())) {
                    BigDecimal changeQuantityOutPost = detailPost.getChangeQuantityOut() == null
                            ? BigDecimal.ZERO : detailPost.getChangeQuantityOut();
                    detailPost.setChangeQuantityOut(changeQuantityOutPost.add(detailParams.getQuantity()));
                }
            }
        }
        billDetailExtDispatchService.batchUpdateByPrimaryKeySelective(detailExtDispatchList);
        // 1 新增票据+明细
        billStock.setCreateTime(new Date());
        billStock.setUpdateTime(billStock.getCreateTime());
        billStock.setDirection(direction);
        billStock.setBillNo(billOrderNoService.genStockBillCode(billStock.getBillNo(),
                BillCatEnum.stock_out.getCode()));
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        billStock.setQuantity(billSkuQtyComputer.getQtyOfBaseUnitInDetails(billDetailList));
        billStockService.saveStockData(billStock, billDetailList);
        // 调拨入库单
        handleStockDispatchIn(billStock, billDetailList);
        // 2 stock表
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> goodsStockOutList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, billStock.getStoreIdOut());
        List<GoodsStock> goodsStockInList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, billStock.getStoreIdIn());
        GoodsStockAndLog goodsStockOutDto = getGoodsStockOut(billStock, goodsStockOutList, billDetailList);
        GoodsStockAndLog goodsStockInDto = getGoodsStockIn(billStock, goodsStockInList, billDetailList);
        //
        List<GoodsStock> goodsStockListParams = goodsStockOutDto.getGoodsStockList();
        goodsStockListParams.addAll(goodsStockInDto.getGoodsStockList());
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockListParams);
        return Result.ok();
    }

    /**
     * 作废单据
     *
     * @param billId
     * @return
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public Result cancleBill(Long billId) throws CustomsException {
        BillStock billStock = billStockMapper.selectByPrimaryKey(billId);
        if ("1".equals(billStock.getIsCancle())) {
            throw new CustomsException("该单据已经作废！");
        }
        Long relationId = billStock.getBillRelationId();
        Bill relationBill = billCommonService.getBillById(relationId);
        switch (relationBill.getBillCat()) {
            case "dispatch":
                cancleDispatchBill(billStock);
                break;
            default:
                cancleDefaultQty(billId, billStock.getStoreId(), relationId);
                break;
        }
        // 3. 修改bill 的iscancle
        billStockService.cancleBill(billId);
        return Result.ok();
    }

    /**
     * 查询待出库单据
     *
     * @param pageParams
     * @return
     */
    public Result listWaiteStockOutBillList(PageParams<Bill> pageParams) {
        Page<Bill> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(billStockMapper.selectWaiteStockOutBillList(page, data));
        return Result.ok(page);
    }

    /**
     * 查询已出库单据
     *
     * @param pageParams
     * @return
     */
    public Result listHasStockOutBillList(PageParams<BillStock> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("billCat", BillCatEnum.stock_out.getCode());
        Page<BillStock> page = pageParams.getPage();
        page.setRecords(billStockMapper.getHasStockBillList(page, data));
        return Result.ok(page);
    }

    public BillAndDetailsVo<BillStock> getDefaultBillAndDetailByBillId(Long billId) {
        BillStock billStock = billStockMapper.getBillByBillId(billId);
        List<BillDetail> detailList = billDetailService.listDataByBillId(billId);
        BillAndDetailsVo<BillStock> billStockBillAndDetailsVo = new BillAndDetailsVo<>();
        billStockBillAndDetailsVo.setBill(billStock);
        billStockBillAndDetailsVo.setBillDetailList(detailList);
        return billStockBillAndDetailsVo;
    }

    public Map<String, Object> getDispatchBillAndDetailByBillId(Long billId) {
        return billDispatchService.getBillAndDetailByBillId("0", billId);
    }

    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.stock_out));
    }

    public Result getPrintData(Long billId) {
        TempPrintVo print = tempPrintConfig.getStockin();
        BillStock billStock = billStockMapper.getBillByBillId(billId);
        List<BillDetail> detailList = billDetailService.listDataByBillId(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getStockTempPrint(print,
                billStock, detailList);
        return Result.ok(defaultTempPrint);
    }

    private GoodsStockAndLog getGoodsStockOut(BillStock billStock, List<GoodsStock> goodsStockList, List<BillDetail> detailList) {
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        return billSkuQtyComputer.getDefaultSubExistQty(billStock, detailList, goodsStockList);
    }

    private GoodsStockAndLog getGoodsStockIn(BillStock billStock, List<GoodsStock> goodsStockList, List<BillDetail> detailList) {
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        return billSkuQtyComputer.getDefaultAddExistQty(billStock, detailList, goodsStockList);
    }

    /**
     * 调拨入库单
     */
    private void handleStockDispatchIn(BillStock billStock, List<BillDetail> billDetailList) {
        BillDispatch billDispatchByBillStock = getBillDispatchByBillStock(billStock);
        Long id = billDispatchByBillStock.getId();
        for (BillDetail billDetail : billDetailList) {
            billDetail.setId(IDUtil.getSnowflakeId());
            billDetail.setBillId(id);
            billDetail.setChangeQuantity2(BigDecimal.ZERO);
            billDetail.setChangeQuantity(BigDecimal.ZERO);
        }
        billDetailService.insertListData(billDetailList);
        billDispatchMapper.insert(billDispatchByBillStock);
    }

    private BillDispatch getBillDispatchByBillStock(BillStock billStock) {
        Long billMasterId = billStock.getBillRelationId();
        BillExtDispatch billExtDispatch = billExtDispatchService.selectByPrimaryKey(billMasterId);
        BillDispatch billDispatch = new BillDispatch();
        billDispatch.setId(IDUtil.getSnowflakeId());
        billDispatch.setBillMasterId(billMasterId);
        billDispatch.setStoreId(billExtDispatch.getStoreIdIn());
        billDispatch.setDirection("1");
        billDispatch.setChangeQuantity(BigDecimal.ZERO);
        billDispatch.setReturnQuantity(BigDecimal.ZERO);
        billDispatch.setCreateTime(billStock.getCreateTime());
        billDispatch.setUpdateTime(billStock.getUpdateTime());
        return billDispatch;
    }

    private void cancleDispatchBill(BillStock billStock) throws CustomsException {
        Long billStockId = billStock.getId();
        Long billRelationId = billStock.getBillRelationId();
        // 重置change_qty_out
        // 查询到stock的detail 和 detail_ext_dispatch的detail
        List<BillDetailExtDispatch> detailExtDispatchList = billDetailExtDispatchService.getListDataByBillId(billRelationId);
        List<BillDetail> detailStockList = billDetailService.listDataByBillId(billStockId);
        // 回滚goodsStock
        goodsStockUpdateService.updateStockAndWaite(billStock.getStoreId(),
                detailStockList, "out", "sub");
        // 回滚stockOutQty
        for (BillDetailExtDispatch detailExtDispatch : detailExtDispatchList) {
            for (BillDetail detailStock : detailStockList) {
                if (detailStock.getBillRelationId().equals(detailExtDispatch.getId())) {
                    BigDecimal changeQuantityOut = detailExtDispatch.getChangeQuantityOut();
                    BigDecimal targetQty = changeQuantityOut.subtract(detailStock.getQuantity());
                    detailExtDispatch.setChangeQuantityOut(targetQty);
                }
            }
        }
        billDetailExtDispatchService.batchUpdateByPrimaryKeySelective(detailExtDispatchList);
        // 作废调拨入库单
        billDispatchCancleBillService.cancleDispatchIn(billRelationId);
    }

    /**
     * 一般情况下，回滚detail里changeQty
     *
     * @param billStockId
     * @param storeId
     * @param relationId
     * @throws CustomsException
     */
    private void cancleDefaultQty(Long billStockId, Long storeId, Long relationId) throws CustomsException {

        List<BillDetail> detailStockList = billDetailService.listDataByBillId(billStockId);
        List<BillDetail> detailRelationList = billDetailService.listDataByBillId(relationId);
        // 1. 修改stock的入库量，待入库数量
        goodsStockUpdateService.updateStockAndWaite(storeId,
                detailStockList, "out", "sub");
        if (detailRelationList.size() != detailStockList.size()) {
            throw new BusinessException("关联单据明细和本单据明细，数量不一致！");
        }
        // 2. 修改关联bill的change_qty
        for (BillDetail detailStock : detailStockList) {
            for (BillDetail detailRelation : detailRelationList) {
                if (detailStock.getBillRelationId().equals(detailRelation.getId())) {
                    BigDecimal t = detailRelation.getChangeQuantity()
                            .subtract(detailStock.getQuantity());
                    detailRelation.setChangeQuantity(t);
                }
            }
        }
        billDetailService.updateListSelectiveData(detailRelationList);
    }
}
