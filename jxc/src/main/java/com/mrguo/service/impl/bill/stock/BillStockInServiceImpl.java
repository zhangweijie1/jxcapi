package com.mrguo.service.impl.bill.stock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.dao.bill.BillDispatchMapper;
import com.mrguo.dao.bill.BillStockMapper;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.entity.bill.*;
import com.mrguo.vo.bill.BillDiapatchVo;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockUpdateServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillCommonServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillDetailExtDispatchServiceImpl;
import com.mrguo.service.impl.bill.dispatch.BillExtDispatchServiceImpl;
import com.mrguo.service.impl.bill.uils.*;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
class BillStockInServiceImpl extends BaseServiceImpl<BillStock> {

    @Autowired
    private BillCommonServiceImpl billCommonService;
    @Autowired
    private BillHandleRelationQtyServiceImpl billHandleRelationQtyService;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    private BillDispatchMapper billDispatchMapper;
    @Autowired
    private BillStockMapper billStockMapper;
    @Autowired
    private BillStockServiceImpl billStockService;
    @Autowired
    private BillHandleRelationQtyServiceImpl billHandleRelationService;
    @Autowired
    private GoodsStockUpdateServiceImpl goodsStockUpdateService;
    @Autowired
    private BillDetailServiceImpl billDetailService;
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
     * 新增入库单
     * 不需要写入库存日志（以为会根据业务单据关联到进出库）
     * 1. 新增单据和明细
     * 2. 库存，库存日志
     * 3. 关联单据的change_qty
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addStockIn(BillAndDetailsVo<BillStock> billAndDetailsVo) throws CustomsException {
        BillStock billStock = billAndDetailsVo.getBill();
        Long billRelationId = billStock.getBillRelationId();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        String direction = "1";
        // 处理关联单据
        for (BillDetail billdetail : billDetailList) {
            billdetail.setChangeQuantity(BigDecimal.ZERO);
            billdetail.setBillRelationId(billdetail.getId());
        }
        billHandleRelationService.handleRelationChangeQty(billRelationId, billDetailList);
        // 1 新增单据和单据明细
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        billStock.setDirection(direction);
        billStock.setBillNo(billOrderNoService.genStockBillCode(billStock.getBillNo(),
                BillCatEnum.stock_in.getCode()));
        billStock.setQuantity(billSkuQtyComputer.getQtyOfBaseUnitInDetails(billDetailList));
        billStockService.saveStockData(billStock, billDetailList);
        // 单据影响到的元素： 库存
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, billStock.getStoreId());
        GoodsStockAndLog goodsStockDto = getGoodsStockIn(billStock, goodsStockList, billDetailList);
        List<GoodsStock> goodsStockListParams = goodsStockDto.getGoodsStockList();
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockListParams);
        return Result.ok();
    }

    /**
     * 新增入库单
     * 不需要写入库存日志（以为会根据业务单据关联到进出库）
     * 1. 新增单据和明细
     * 2. 关联单据的change_qty
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addStockInDispatch(BillAndDetailsVo<BillStock> billAndDetailsVo) throws CustomsException {
        BillStock billStock = billAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        String direction = "1";
        // 校验
        Long billRelationId = billStock.getBillRelationId();
        List<BillDetail> detailListPost = billDetailService.listDataByBillId(billRelationId);
        valiAddStock(detailListPost, billDetailList);
        // 1 新增入库票据
        billStock.setDirection(direction);
        billStock.setBillNo(billOrderNoService.genStockBillCode(billStock.getBillNo(),
                BillCatEnum.stock_in.getCode()));
        // 2 修改关联单据(进货单等)的change_qty字段
        billHandleRelationQtyService.handleRelationChangeQty(detailListPost, billDetailList);
        // 关联id设置为t_bill的id
        BillDispatch billDispatch = billDispatchMapper.selectByPrimaryKey(billRelationId);
        Long billMasterId = billDispatch.getBillMasterId();
        billStock.setBillRelationId(billMasterId);
        billStockService.saveStockData(billStock, billDetailList);
        // 3 stock表，入库量
        List<Long> skuids = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> stockArrayList = goodsStockService.listStockBySkuIdsAndStoreId(skuids, billStock.getStoreIdIn());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        for (GoodsStock goodsStock : stockArrayList) {
            BigDecimal qty = billSkuQtyComputer.getQtyOfBaseUnitInDetailsBySkuId(goodsStock.getSkuId(), billDetailList);
            goodsStock.setQuantityIn(goodsStock.getQuantityIn().add(qty));
            goodsStock.setWaitQuantityIn(goodsStock.getWaitQuantityIn().add(qty.negate()));
        }
        goodsStockService.batchUpdateByPrimaryKeySelective(stockArrayList);
        // 4 stock_log，库存日志
        Bill bill = billStockService.billStock2bill(billStock);
        bill.setStoreId(bill.getId());
        bill.setDirection(direction);
        return Result.ok();
    }


    /**
     * 作废入库单据
     * qty_in 减少，waite_qty_in 增加
     *
     * @param billId
     * @return
     * @throws CustomsException
     */
    @Transactional(rollbackFor = Exception.class)
    public Result cancleBill(Long billId) throws CustomsException {
        BillStock billStock = billStockMapper.selectByPrimaryKey(billId);
        Long relationId = billStock.getBillRelationId();
        if ("1".equals(billStock.getIsCancle())) {
            throw new CustomsException("该单据已经作废！");
        }
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
     * 获取待入库单据
     *
     * @param pageParams
     * @return
     */
    public Result listWaiteStockInBillList(PageParams<Bill> pageParams) {
        Page<Bill> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(billStockMapper.selectWaiteStockInBillList(page, data));
        return Result.ok(page);
    }

    /**
     * 获取已入库单据
     *
     * @param pageParams
     * @return
     */
    public Result listHasStockInBillList(PageParams<BillStock> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("billCat", BillCatEnum.stock_in.getCode());
        Page<BillStock> page = pageParams.getPage();
        page.setRecords(billStockMapper.getHasStockBillList(page, data));
        return Result.ok(page);
    }


    /**
     * 获取未入库的商品
     *
     * @param billId
     * @return
     */
    public List<BillDetail> getWaiteGoodsByBillId(Long billId) {
        return billStockService.getWaiteGoodsByBillId(billId);
    }

    /**
     * 获取已入库商品
     *
     * @param billId
     * @return
     */
    public List<BillDetail> getHasInGoodsByBillId(Long billId) {
        return billStockService.getHasStockGoodsByBillId(billId);
    }

    /**
     * 获取单据明细
     *
     * @param billId
     * @return
     */
    public BillAndDetailsVo<BillStock> getDefaultBillAndDetailByBillId(Long billId) {
        BillStock billStock = billStockMapper.getBillByBillId(billId);
        List<BillDetail> detailList = billDetailService.listDataByBillId(billId);
        BillAndDetailsVo<BillStock> billStockBillAndDetailsVo = new BillAndDetailsVo<>();
        billStockBillAndDetailsVo.setBill(billStock);
        billStockBillAndDetailsVo.setBillDetailList(detailList);
        return billStockBillAndDetailsVo;
    }

    /**
     * @param billId t_bill_dispatch的主键id
     * @return
     * @throws
     * @author 郭成兴
     * @createdate 2020/7/1 2:19 PM
     * @updater 郭成兴
     * @updatedate 2020/7/1 2:19 PM
     */
    public BillAndDetailsVo<BillDiapatchVo> getDispatchBillAndDetailByBillId(Long billId) {
        BillDispatch billDispatch = billDispatchMapper.selectByPrimaryKey(billId);
        Long billMasterId = billDispatch.getBillMasterId();
        BillDiapatchVo billDiapatchVo = billDispatchMapper.selectBillByBillId(billMasterId);
        List<BillDetail> billDetailList = billDetailService.listDataByBillId(billId);
        BillAndDetailsVo<BillDiapatchVo> billAndDetailsVo = new BillAndDetailsVo<>();
        billAndDetailsVo.setBill(billDiapatchVo);
        billAndDetailsVo.setBillDetailList(billDetailList);
        return billAndDetailsVo;
    }


    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.stock_in));
    }

    public Result getPrintData(Long billId) {
        TempPrintVo print = tempPrintConfig.getStockin();
        BillStock billStock = billStockMapper.getBillByBillId(billId);
        List<BillDetail> detailList = billDetailService.listDataByBillId(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getStockTempPrint(print,
                billStock, detailList);
        return Result.ok(defaultTempPrint);
    }

    private GoodsStockAndLog getGoodsStockIn(BillStock billStock, List<GoodsStock> goodsStockList, List<BillDetail> detailList) {
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        return billSkuQtyComputer.getDefaultAddExistQty(billStock, detailList, goodsStockList);
    }

    private void valiAddStock(List<BillDetail> detailListPost, List<BillDetail> detailListParams) throws CustomsException {
        for (BillDetail detailPost : detailListPost) {
            for (BillDetail detailParam : detailListParams) {
                if (detailParam.getId().equals(detailPost.getId())) {
                    if (detailPost.getChangeQuantity() == null) {
                        detailPost.setChangeQuantity(BigDecimal.ZERO);
                    }
                    BigDecimal notTransQty = detailPost.getQuantity().subtract(detailPost.getChangeQuantity());
                    if (detailParam.getQuantity().compareTo(notTransQty) > 0) {
                        throw new CustomsException("商品(" + detailParam.getName() + "): 入库量不能超过,未入库量！");
                    }
                }
            }
        }
    }

    /**
     * 作废调拨入库单产生的，入库单
     *
     * @param billStock
     */
    private void cancleDispatchBill(BillStock billStock) throws CustomsException {
        // 1. 修改t_bill_detail_ext_dispatch的 change_qty_in减少
        Long relationId = billStock.getBillRelationId();
        List<BillDetailExtDispatch> detailExtDispatchList = billDetailExtDispatchService.getListDataByBillId(relationId);
        List<BillDetail> detailStockList = billDetailService.listDataByBillId(billStock.getId());
        if (detailExtDispatchList.size() != detailStockList.size()) {
            throw new BusinessException("入库单明细和调拨入库单明细，不一致");
        }
        for (BillDetailExtDispatch detailExtDispatch : detailExtDispatchList) {
            for (BillDetail detailStock : detailStockList) {
                if (detailStock.getBillRelationId().equals(detailExtDispatch.getId())) {
                    BigDecimal changeQuantityIn = detailExtDispatch.getChangeQuantityIn();
                    BigDecimal targetQty = changeQuantityIn.subtract(detailStock.getQuantity());
                    detailExtDispatch.setChangeQuantityIn(targetQty);
                }
            }
        }
        billDetailExtDispatchService.batchUpdateByPrimaryKeySelective(detailExtDispatchList);
        // 2. 修改goodsStock, qtyIn减少，qtyWaiteIn增加
        BillExtDispatch billExtDispatch = billExtDispatchService.selectByPrimaryKey(relationId);
        Long storeIdIn = billExtDispatch.getStoreIdIn();
        goodsStockUpdateService.updateStockAndWaite(storeIdIn,
                detailStockList, "in", "sub");
    }

    private void cancleDefaultQty(Long billStockId, Long storeId, Long relationId) throws CustomsException {
        List<BillDetail> detailStockList = billDetailService.listDataByBillId(billStockId);
        List<BillDetail> detailRelationList = billDetailService.listDataByBillId(relationId);
        if (detailRelationList.size() != detailStockList.size()) {
            throw new BusinessException("关联单据明细和本单据明细，数量不一致");
        }
        // 1. 修改stock的入库量，待入库数量
        goodsStockUpdateService.updateStockAndWaite(storeId,
                detailStockList, "in", "sub");
        // 2. 修改关联bill的change_qty
        for (BillDetail detailStock : detailStockList) {
            for (BillDetail detailRelation : detailRelationList) {
                if (detailStock.getBillRelationId().equals(detailRelation.getId())) {
                    BigDecimal targetQty = detailRelation.getChangeQuantity().subtract(detailStock.getQuantity());
                    detailRelation.setChangeQuantity(targetQty);
                }
            }
        }
        billDetailService.updateListSelectiveData(detailRelationList);
    }
}
