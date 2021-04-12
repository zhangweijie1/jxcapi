package com.mrguo.service.impl.bill.dispatch;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.config.TempPrintConfig;
import com.mrguo.dao.bill.BillDispatchMapper;
import com.mrguo.dao.log.LogStockMapper;
import com.mrguo.entity.bill.*;
import com.mrguo.vo.bill.*;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.goods.GoodsStockAndLog;
import com.mrguo.entity.log.LogGoodsStock;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillDetailServiceImpl;
import com.mrguo.service.impl.bill.basebill.BillValidationServiceImpl;
import com.mrguo.service.impl.bill.uils.*;
import com.mrguo.service.inter.bill.BillBaseService;
import com.mrguo.service.inter.bill.BillCommonService;
import com.mrguo.util.enums.BillCatEnum;
import com.mrguo.vo.TempPrintVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class BillDispatchServiceImpl implements BillBaseService<BillDiapatchVo> {

    @Autowired
    private BillValidationServiceImpl billValidationServiceImpl;
    @Autowired
    private BillDispatchMapper billDispatchMapper;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private BillCommonService billCommonService;
    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;
    @Autowired
    private BillExtDispatchServiceImpl billExtDispatchService;
    @Autowired
    private BillDetailExtDispatchServiceImpl billDetailExtDispatchService;
    @Autowired
    private LogStockMapper logStockMapper;
    @Autowired
    private TempPrintConfig tempPrintConfig;
    @Autowired
    private BillHandleTempPrintServiceImpl billHandleTempPrintService;

    /**
     * 新增调拨单
     * 1. 新增一个调拨出库
     * 2. 出库的时候，如果是调拨单，则出库多少，新增一个出库数量的调拨入库单
     *
     * @param billAndDetailsVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result addData(BillAndDetailsVo<BillDiapatchVo> billAndDetailsVo) throws CustomsException {
        // 主bill新增一个数据（展示列表）
        // bill_dispatch新增调拨出库
        // 1. 新增bill表
        BillDiapatchVo bill = (BillDiapatchVo) billAndDetailsVo.getBill();
        bill.setStoreId(0L);
        List<BillDetail> billDetailList = billAndDetailsVo.getBillDetailList();
        bill.setBillCat(BillCatEnum.dispatch.getCode());
        bill.setBillCatName(BillCatEnum.dispatch.getMessage());
        bill.setBillNo(billOrderNoService.genBillCode(bill.getBillNo(),
                BillCatEnum.dispatch.getCode()));
        // vali
        billValidationServiceImpl.valiStoreIsLock(bill.getStoreIdIn());
        billValidationServiceImpl.valiStoreIsLock(bill.getStoreIdOut());
        //
        BillSetDataSetter billSetDataSetter = BillUtilsGenerator.getBillSetDataSetter();
        billSetDataSetter.setBillAndDetailList(bill, billDetailList);
        for (BillDetail billDetail : billDetailList) {
            billDetail.setChangeQuantity(BigDecimal.ZERO);
            billDetail.setChangeQuantity2(BigDecimal.ZERO);
            billDetail.setReturnQuantity(BigDecimal.ZERO);
        }
        //
        billCommonService.addData(bill, billDetailList);
        BillExtDispatch billExtDispatch = getBillExtDispatch(bill);
        billExtDispatchService.saveData(billExtDispatch);
        List<BillDetailExtDispatch> billDetailExtDispatchList = getBillDetailExtDispatch(billDetailList);
        billDetailExtDispatchService.insertListData(billDetailExtDispatchList);
        // 2. 新增bill_dispatch表,out
        BillDispatch billDispatch = getBillDispatch(bill);
        billDispatchMapper.insertSelective(billDispatch);
        //修改t_stock表
        List<Long> skuIds = billDetailList.stream().map(BillDetail::getSkuId).collect(Collectors.toList());
        List<GoodsStock> goodsStockList = goodsStockService.listStockBySkuIdsAndStoreId(skuIds, bill.getStoreIdOut());
        BillSkuQtyComputer billSkuQtyComputer = BillUtilsGenerator.getBillSkuQtyComputer();
        GoodsStockAndLog goodsStockAndLog = billSkuQtyComputer.getDefaultAddWaiteQty(bill, billDetailList, goodsStockList);
        goodsStockList = goodsStockAndLog.getGoodsStockList();
        List<LogGoodsStock> logGoodsStockList = goodsStockAndLog.getLogGoodsStockList();
        // 数据库操作
        //修改bill表关联库存表
        goodsStockService.batchUpdateByPrimaryKeySelective(goodsStockList);
        //插入stockLog表
        logStockMapper.insertList(logGoodsStockList);
        return Result.ok();
    }

    @Override
    public Result cancle(Long billId) throws CustomsException {
        return null;
    }

    @Override
    public Result getBillNo() throws CustomsException {
        return Result.ok(billCommonService.getBillNo(BillCatEnum.dispatch));
    }

    /**
     * 查询调拨单
     *
     * @param pageParams
     * @return
     */
    @Override
    public Result listPage(PageParams<BillDiapatchVo> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<BillDiapatchVo> page = pageParams.getPage();
        page.setRecords(billDispatchMapper.selectList(page, data));
        return Result.ok(page);
    }

    @Override
    public Result export(Long billId) {
        return null;
    }

    @Override
    public Result print(Long billId) {
        TempPrintVo print = tempPrintConfig.getDispatch();
        BillDiapatchVo billDiapatchVo = billDispatchMapper.selectBillByBillId(billId);
        List<BillDetail> billDetailList = billDetailService.listDataByBillId(billId);
        HashMap<String, Object> defaultTempPrint = billHandleTempPrintService.getDispatchTempPrint(
                print,
                billDiapatchVo,
                billDetailList);
        return Result.ok(defaultTempPrint);
    }

    /**
     * 获取调拨入库明细数量，出库数量
     * 在出入库时用到
     *
     * @param direction
     * @param billId
     * @return
     */
    public Map<String, Object> getBillAndDetailByBillId(String direction, Long billId) {
        BillDiapatchVo billDiapatchVo = billDispatchMapper.selectBillByBillId(billId);
        List<BillDetailDispatchVo> billDetailList = null;
        if ("0".equals(direction)) {
            billDetailList = billDetailExtDispatchService.getListOutDataByBillId(billId);
        } else {
            billDetailList = billDetailExtDispatchService.getListInDataByBillId(billId);
        }
        Map<String, Object> billDto = new HashMap<>();
        billDto.put("billDetailList", billDetailList);
        billDto.put("bill", billDiapatchVo);
        return billDto;
    }

    public BillAndDetailsVo<BillDiapatchVo> getBillAndDetailByBillId(Long billId) {
        BillDiapatchVo billDiapatchVo = billDispatchMapper.selectBillByBillId(billId);
        List<BillDetail> billDetailList = billDetailService.listDataByBillId(billId);
        BillAndDetailsVo<BillDiapatchVo> billAndDetailsVo = new BillAndDetailsVo<>();
        billAndDetailsVo.setBill(billDiapatchVo);
        billAndDetailsVo.setBillDetailList(billDetailList);
        return billAndDetailsVo;
    }

    public BillDispatch getDispatchInByMasterId(Long masterBillId) {
        return billDispatchMapper.selectDispatchInByMasterId(masterBillId);
    }

    /**
     * 组装调拨单
     *
     * @param bill
     * @return
     */
    private BillDispatch getBillDispatch(BillDiapatchVo bill) {
        BillDispatch billDispatch = new BillDispatch();
        billDispatch.setId(IDUtil.getSnowflakeId());
        billDispatch.setStoreId(bill.getStoreIdOut());
        billDispatch.setBillMasterId(bill.getId());
        billDispatch.setDirection("0");
        billDispatch.setCreateTime(new Date());
        billDispatch.setUpdateTime(billDispatch.getCreateTime());
        return billDispatch;
    }

    private BillExtDispatch getBillExtDispatch(BillDiapatchVo bill) {
        BillExtDispatch billExtDispatch = new BillExtDispatch();
        billExtDispatch.setId(bill.getId());
        billExtDispatch.setStoreIdIn(bill.getStoreIdIn());
        billExtDispatch.setStoreIdOut(bill.getStoreIdOut());
        return billExtDispatch;
    }

    private List<BillDetailExtDispatch> getBillDetailExtDispatch(List<BillDetail> detailList) {
        ArrayList<BillDetailExtDispatch> billDetailExtDispatches = new ArrayList<>();
        for (BillDetail detail : detailList) {
            BillDetailExtDispatch detailExtDispatch = new BillDetailExtDispatch();
            detailExtDispatch.setChangeQuantityOut(BigDecimal.ZERO);
            detailExtDispatch.setChangeQuantityIn(BigDecimal.ZERO);
            detailExtDispatch.setId(detail.getId());
            detailExtDispatch.setBillId(detail.getBillId());
            detailExtDispatch.setSkuId(detail.getSkuId());
            billDetailExtDispatches.add(detailExtDispatch);
        }
        return billDetailExtDispatches;
    }
}
