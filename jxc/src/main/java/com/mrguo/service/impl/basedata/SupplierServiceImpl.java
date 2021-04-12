package com.mrguo.service.impl.basedata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.common.utils.MapToEntityUtil;
import com.mrguo.dao.bsd.CustomerSupplierMapper;
import com.mrguo.dao.bsd.SupplierMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bsd.CustomerSupplier;
import com.mrguo.entity.bsd.Supplier;
import com.mrguo.entity.origin.OriginComegoDebt;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.service.impl.excle.ExcleDefaultTempGenerater;
import com.mrguo.service.impl.excle.ExcleSupplierData;
import com.mrguo.service.impl.origin.OriginComegoDebtServiceImpl;
import com.mrguo.service.impl.utils.ExcleServieUtils;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.util.business.OrderNoGenerator;
import com.mrguo.util.business.UserInfoThreadLocalUtils;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 郭成兴（wx:512830037）
 * @Description 供应商Service实现类
 * @Date
 * @Param
 * @return
 **/
@Service("supplierService")
public class SupplierServiceImpl extends BaseServiceImpl<Supplier> {

    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private CustomerSupplierMapper customerSupplierMapper;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    private OriginComegoDebtServiceImpl originComegoDebtService;
    @Autowired
    CustomerSupplierServiceImpl customerSupplierService;
    @Autowired
    private ExcleServieUtils excleServieUtils;
    @Autowired
    private OrderNoGenerator orderNoGenerator;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<Supplier> getMapper() {
        return supplierMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result saveOrUpdateData(Supplier supplier) throws CustomsException {
        if (supplier.getId() == null) {
            return addData(supplier);
        } else {
            return updateCusData(supplier);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result delDataById(Long id) {
        if (billCountService.countByComegoId(id) > 0) {
            return Result.fail("该供应商在单据中被引用，不能删除");
        }
        if (supplierMapper.deleteByPrimaryKey(id) <= 0) {
            return Result.fail("删除失败");
        }
        return Result.ok("删除成功");
    }

    /**
     * 增加欠款
     *
     * @param comegoId
     * @param disDebt
     * @return Supplier
     */
    public Supplier addDebt(Long comegoId, BigDecimal disDebt) {
        Supplier supplier = supplierMapper.selectByPrimaryKey(comegoId);
        if (supplier == null) {
            throw new BusinessException("该供应商不存在");
        }
        if (!BigDecimal.ZERO.equals(disDebt)) {
            BigDecimal debt = supplier.getDebt();
            BigDecimal targetDebt = debt.add(disDebt);
            supplier.setDebt(targetDebt);
            supplierMapper.updateByPrimaryKeySelective(supplier);
        }
        return supplier;
    }

    public Result updateCusData(Supplier supplier) {
        if (supplier.getCustomerId() != null) {
            CustomerSupplier customerSupplier = new CustomerSupplier();
            customerSupplier.setSupplierId(supplier.getId());
            customerSupplier.setCustomerId(supplier.getCustomerId());
            customerSupplierService.saveOrUpdateBySupplier(customerSupplier);
        } else {
            customerSupplierMapper.deleteBySupplierId(supplier.getId());
        }
        originComegoDebtService.updateLastOriginData(supplier.getOriginDebt(), supplier.getId());
        if (supplierMapper.updateByPrimaryKeySelective(supplier) > 0) {
            return Result.okmsg("修改成功");
        }
        return Result.fail("修改失败");
    }

    public Result getDataById(Long id) {
        Supplier supplier = supplierMapper.selectByPrimaryKey(id);
        return Result.ok(supplier);
    }

    public Result listPage(PageParams<Supplier> pageParams) {
        Page<Supplier> page = pageParams.getPage();
        Supplier supplier = MapToEntityUtil.map2Entity(pageParams.getData(), Supplier.class);
        page.setRecords(supplierMapper.listPage(page, supplier));
        return Result.ok(page);
    }

    public Result listOptions(String keywords) {
        return Result.ok(supplierMapper.listOptions(keywords));
    }

    /**
     * 获取欠款
     * 应收欠款 = 期初 + 新增欠款 - 收回欠款 - 优惠额
     *
     * @param id
     * @return
     */
    public Result getDebtById(Long id) {
        Supplier supplier = supplierMapper.selectByPrimaryKey(id);
        if (supplier == null) {
            return Result.fail("该供应商不存在！");
        }
        BigDecimal originDebt = supplier.getOriginDebt() == null
                ? BigDecimal.ZERO : supplier.getOriginDebt();
        BigDecimal amounrAddDebt = supplier.getAmountDebt() == null ?
                BigDecimal.ZERO : supplier.getAmountDebt();
        return Result.ok(originDebt.add(amounrAddDebt));
    }

    public Result findByName(String name) {
        return Result.ok(supplierMapper.findByName(name));
    }

    public Result statistics(PageParams<Bill> pageParams) {
        Page<Bill> page = pageParams.getPage();
        Bill bill = MapToEntityUtil.map2Entity(pageParams.getData(), Bill.class);
        page.setRecords(supplierMapper.statistics(page, bill));
        return Result.ok(page);
    }

    public Result getCode() {
        return Result.ok(orderNoGenerator.getCustomerCode("GYS", 4));
    }

    public Result exportData() throws IOException, WriteException {
        List<ExcleSupplierData> excleSupplierDataList = supplierMapper.exportData();
        ExcleDefaultTempGenerater tempGenerater = new ExcleDefaultTempGenerater(ExcleSupplierData.class, "sa");
        ByteArrayOutputStream outputStream = tempGenerater.listData2Excle(excleSupplierDataList, "客户资料");
        String fileName = "供应商资料列表.xls";
        excleServieUtils.stream2excle(fileName, outputStream);
        return Result.ok();
    }

    public Result clearAllData() {
        return Result.ok();
    }

    public Result createExcleTemp() throws IOException, WriteException {
        ExcleDefaultTempGenerater tempGenerater = new ExcleDefaultTempGenerater(ExcleSupplierData.class, "sasa");
        ByteArrayOutputStream outputStream = tempGenerater.createExcleTemp("供应商资料");
        String fileName = "供应商导入模板.xls";
        excleServieUtils.stream2excle(fileName, outputStream);
        return Result.ok();
    }

    private Result addData(Supplier supplier) throws CustomsException {
        Date date = new Date();
        // 1 如果是默认
        if ("1".equals(supplier.getIsDefault())) {
            supplierMapper.cancleDefault();
            supplier.setIsDefault("1");
        }
        supplier.setId(IDUtil.getSnowflakeId());
        if (supplierMapper.countByCode(supplier.getCode()) > 0) {
            supplier.setCode(genCode());
        } else {
            genCode();
        }
        UserInfo userInfo = UserInfoThreadLocalUtils.get();
        Long userId = userInfo.getUserId();
        supplier.setDebt(BigDecimal.ZERO);
        supplier.setCreateTime(date);
        supplier.setCreateUser(userId);
        supplier.setUpdateTime(supplier.getCreateTime());
        supplier.setUpdateUser(userId);
        if (supplier.getRelationId() != null) {
            CustomerSupplier customerSupplier = new CustomerSupplier();
            customerSupplier.setSupplierId(supplier.getId());
            customerSupplier.setCustomerId(supplier.getRelationId());
            if (customerSupplierMapper.insert(customerSupplier) <= 0) {
                throw new CustomsException("客户供货商关联关系失败");
            }
        }
        // 期初
        OriginComegoDebt originComegoDebt = new OriginComegoDebt();
        originComegoDebt.setId(IDUtil.getSnowflakeId());
        originComegoDebt.setComegoId(supplier.getId());
        originComegoDebt.setBalanceTime(date);
        originComegoDebt.setDebt(supplier.getOriginDebt());
        originComegoDebt.setCat("0");
        originComegoDebt.setParentId(0L);
        originComegoDebtService.saveData(originComegoDebt);
        if (supplierMapper.insertSelective(supplier) <= 0) {
            throw new CustomsException("新增供货商失败");
        }
        return Result.ok();
    }

    private String genCode() {
        return orderNoGenerator.genCustomerCode("GYS", 4);
    }
}
