package com.mrguo.service.impl.origin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.origin.OriginMasterMapper;
import com.mrguo.entity.bsd.Account;
import com.mrguo.entity.bsd.Customer;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.entity.goods.GoodsStock;
import com.mrguo.entity.origin.OriginAccountAmount;
import com.mrguo.entity.origin.OriginComegoDebt;
import com.mrguo.entity.origin.OriginMaster;
import com.mrguo.entity.origin.OriginSkuStock;
import com.mrguo.service.impl.basedata.AccountServiceImpl;
import com.mrguo.service.impl.basedata.CustomerServiceImpl;
import com.mrguo.service.impl.basedata.goods.GoodsStockServiceImpl;
import com.mrguo.service.impl.basedata.SupplierServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/8/910:17 AM
 * @updater 郭成兴
 * @updatedate 2020/8/910:17 AM
 */
@Service
public class OriginMasterServiceImpl extends BaseServiceImpl<OriginMaster> {

    @Autowired
    private OriginMasterMapper originMasterMapper;
    @Autowired
    private OriginSkuStockServiceImpl originSkuStockService;
    @Autowired
    private OriginSkuCostpriceServiceImpl originSkuCostpriceService;
    @Autowired
    private OriginComegoDebtServiceImpl originComegoDebtService;
    @Autowired
    private OriginAccountAmountServiceImpl originAccountAmountService;

    @Autowired
    private GoodsStockServiceImpl goodsStockService;
    @Autowired
    private SupplierServiceImpl supplierService;
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<OriginMaster> getMapper() {
        return originMasterMapper;
    }

    /**
     * 结存
     * 一个主表：记录结存节点（时间，id）
     * 各个数据附表，关联主表
     * 1. 商品库存，
     * 2. 商品成本价
     * 2. 往来单位欠款
     * 3. 结算账户金额
     *
     * @return
     */
    public Result balance(OriginMaster originMaster) throws CustomsException {
        valiBalance(originMaster);
        Long userId = (Long) request.getAttribute("userId");
        // 1. 主表
        Long masterId = IDUtil.getSnowflakeId();
        originMaster.setId(masterId);
        originMaster.setCreateTime(new Date());
        originMaster.setCreateUser(userId);
        originMasterMapper.insert(originMaster);
//        // 2. 附表
//        Date balanceTime = originMaster.getBalanceTime();
//        // 商品库存
//        List<GoodsStock> goodsStockList = goodsStockService.selectAll();
//        List<OriginSkuStock> originSkuStock = getOriginSkuStock(goodsStockList, masterId, createTime);
//        // 商品成本价
//        // 往来单位欠费
//        List<Supplier> suppliers = supplierService.selectAll();
//        List<Customer> customers = customerService.selectAll();
//        List<OriginComegoDebt> originSupplierDebt = getOriginSupplierDebt(suppliers, masterId, createTime);
//        List<OriginComegoDebt> originCustomerDebt = getOriginCustomerDebt(customers, masterId, createTime);
//        ArrayList<OriginComegoDebt> originComegoDebts = new ArrayList<>();
//        originComegoDebts.addAll(originSupplierDebt);
//        originComegoDebts.addAll(originCustomerDebt);
//        // 结算账户金额
//        List<Account> accountList = accountService.selectAll();
//        List<OriginAccountAmount> originAccountAmounts = getOriginAccountAmount(accountList, masterId, createTime);
//        // 插入数值
//        originSkuStockService.insertListData(originSkuStock);
//        originComegoDebtService.insertListData(originComegoDebts);
//        originAccountAmountService.insertListData(originAccountAmounts);
        return Result.ok();
    }

    /**
     * 反结存
     *
     * @return
     */
    public Result unbalance() {
        if (originMasterMapper.count() <= 1) {
            return Result.fail("反结账失败", "系统初始化结账时间，不能反结账.");
        }
        OriginMaster originMaster = originMasterMapper.selectLastData();
        if (originMasterMapper.deleteByPrimaryKey(originMaster) > 0) {
            return Result.ok("反结账成功！");
        } else {
            return Result.fail("反结账失败！");
        }
    }

    public Result getBalanceTime() {
        return Result.ok(originMasterMapper.selectBalanceTime());
    }

    /**
     * 查询结存记录
     *
     * @param pageParams
     * @return
     */
    public Result listOriginPage(PageParams<OriginMaster> pageParams) {
        Page<OriginMaster> page = pageParams.getPage();
        List<OriginMaster> originMasters = originMasterMapper.selectOriginAll(page);
        page.setRecords(originMasters);
        return Result.ok(page);
    }

    private void valiBalance(OriginMaster originMaster) throws CustomsException {
        if (originMaster.getBalanceTime() == null) {
            throw new CustomsException("结账时间不能为空！");
        }
        // 不能早于系统开启日期，不得早于等于上次结算日期
        Map<String, Object> map = originMasterMapper.selectBalanceTime();
        String lastBalanceTimeStr = (String) map.get("lastBalanceTime");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date lastBalanceTime = null;
        Date balanceTime = null;
        try {
            lastBalanceTime = simpleDateFormat.parse(lastBalanceTimeStr);
            balanceTime = simpleDateFormat.parse(simpleDateFormat.format(originMaster.getBalanceTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new CustomsException("上次结算时间解析有误！");
        }
        if (balanceTime.compareTo(lastBalanceTime) <= 0) {
            throw new CustomsException("结账时间不得等于或小于上次结账时间：" + lastBalanceTimeStr);
        }
    }

    private List<OriginSkuStock> getOriginSkuStock(List<GoodsStock> goodsStockList,
                                                   Long masterId,
                                                   Date balanceTime) {
        // 获取所有商品，每个仓库的当前库存
        ArrayList<OriginSkuStock> originSkuStocks = new ArrayList<>();
        for (GoodsStock goodsStock : goodsStockList) {
            BigDecimal originQuantity = goodsStock.getOriginQuantity();
            // 当前存货
            BigDecimal remainQty = originQuantity.add(goodsStock.getQuantityIn())
                    .add(goodsStock.getQuantityOut().negate());
            OriginSkuStock originSkuStock = new OriginSkuStock();
            // 构建 OriginSkuStock
            originSkuStock.setOriginQuantity(goodsStock.getOriginQuantity());
            originSkuStock.setQuantityIn(goodsStock.getQuantityIn());
            originSkuStock.setQuantityOut(goodsStock.getQuantityOut());
            originSkuStock.setWaitQuantityIn(goodsStock.getWaitQuantityIn());
            originSkuStock.setWaitQuantityOut(goodsStock.getWaitQuantityOut());
            originSkuStock.setQty(remainQty);
            originSkuStock.setBalanceTime(balanceTime);
            originSkuStock.setParentId(masterId);
            //
            originSkuStocks.add(originSkuStock);
        }
        return originSkuStocks;
    }


    private List<OriginComegoDebt> getOriginSupplierDebt(List<Supplier> supplierList,
                                                         Long masterId,
                                                         Date balanceTime) {
        ArrayList<OriginComegoDebt> originComegoDebts = new ArrayList<>();
        for (Supplier supplier : supplierList) {
            // 当前欠款
            BigDecimal originDebt = supplier.getOriginDebt();
            BigDecimal nowDebt = originDebt.add(supplier.getDebt());
            // 构建 OriginComegoDebt
            OriginComegoDebt originComegoDebt = new OriginComegoDebt();
            originComegoDebt.setId(IDUtil.getSnowflakeId());
            originComegoDebt.setCat("0");
            originComegoDebt.setDebt(nowDebt);
            originComegoDebt.setComegoId(supplier.getId());
            originComegoDebt.setBalanceTime(balanceTime);
            originComegoDebt.setParentId(masterId);
            originComegoDebts.add(originComegoDebt);
        }
        return originComegoDebts;
    }

    private List<OriginComegoDebt> getOriginCustomerDebt(List<Customer> customerList,
                                                         Long masterId,
                                                         Date balanceTime) {
        ArrayList<OriginComegoDebt> originComegoDebts = new ArrayList<>();
        for (Customer customer : customerList) {
            // 当前欠款
            BigDecimal originDebt = customer.getOriginDebt();
            BigDecimal nowDebt = originDebt.add(customer.getDebt());
            // 构建 OriginComegoDebt
            OriginComegoDebt originComegoDebt = new OriginComegoDebt();
            originComegoDebt.setId(IDUtil.getSnowflakeId());
            originComegoDebt.setCat("1");
            originComegoDebt.setDebt(nowDebt);
            originComegoDebt.setComegoId(customer.getId());
            originComegoDebt.setBalanceTime(balanceTime);
            originComegoDebt.setParentId(masterId);
            originComegoDebts.add(originComegoDebt);
        }
        return originComegoDebts;
    }

    private List<OriginAccountAmount> getOriginAccountAmount(List<Account> accountList,
                                                             Long masterId,
                                                             Date balanceTime) {
        ArrayList<OriginAccountAmount> originAccountAmounts = new ArrayList<>();
        for (Account account : accountList) {
            // 当前欠款
            BigDecimal originAmount = account.getOriginAmount();
            BigDecimal nowAmount = originAmount.add(account.getAmount());
            // 构建 OriginAccountAmount
            OriginAccountAmount originAccountAmount = new OriginAccountAmount();
            originAccountAmount.setId(IDUtil.getSnowflakeId());
            originAccountAmount.setAcId(account.getId());
            originAccountAmount.setAmount(nowAmount);
            originAccountAmount.setBalanceTime(balanceTime);
            originAccountAmount.setParentId(masterId);
            originAccountAmounts.add(originAccountAmount);
        }
        return originAccountAmounts;
    }

    public static void main(String[] args) throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.toString();
        String format = simpleDateFormat.format(date);
        Date parse = simpleDateFormat.parse(format);
        System.out.println(date);
        System.out.println(format);
        System.out.println(parse);
    }
}
