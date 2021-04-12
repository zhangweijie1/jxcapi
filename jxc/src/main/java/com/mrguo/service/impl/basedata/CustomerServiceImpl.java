package com.mrguo.service.impl.basedata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.BusinessException;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bsd.CustomerMapper;
import com.mrguo.dao.bsd.CustomerSupplierMapper;
import com.mrguo.entity.bsd.CustomerSupplier;
import com.mrguo.entity.origin.OriginComegoDebt;
import com.mrguo.entity.sys.SysDataPermission;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.service.impl.excle.ExcleCustomerData;
import com.mrguo.service.impl.excle.ExcleDefaultTempGenerater;
import com.mrguo.service.impl.origin.OriginComegoDebtServiceImpl;
import com.mrguo.service.impl.utils.ExcleServieUtils;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.util.business.OrderNoGenerator;
import com.mrguo.util.enums.ElmType;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrguo.entity.bsd.Customer;
import com.mrguo.service.inter.bsd.ComegoService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户Service实现类
 *
 * @author mrguo
 */
@Service("customerService")
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements ComegoService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerSupplierMapper customerSupplierMapper;
    @Autowired
    private OriginComegoDebtServiceImpl originComegoDebtService;
    @Autowired
    private CustomerSupplierServiceImpl customerSupplierService;
    @Autowired
    private ExcleServieUtils excleServieUtils;
    @Autowired
    private OrderNoGenerator orderNoGenerator;
    @Autowired
    private BillCountService billCountService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public MyMapper<Customer> getMapper() {
        return customerMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveOrUpdateData(Customer customer) throws CustomsException {
        if (customer.getId() != null) {
            return updateCusData(customer);
        } else {
            return addData(customer);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addData(Customer customer) throws CustomsException {
        // 1 如果是默认
        if ("1".equals(customer.getIsDefault())) {
            cancleDefault();
            customer.setIsDefault("1");
        }
        if (customer.getIsEnable() == null) {
            customer.setIsEnable("1");
        }
        customer.setId(IDUtil.getSnowflakeId());
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        Long userId = (Long) request.getAttribute("userId");
        customer.setDebt(BigDecimal.ZERO);
        customer.setCreateTime(new Date());
        customer.setCreateUser(userId);
        customer.setUpdateTime(customer.getCreateTime());
        customer.setUpdateUser(userId);
        if (customerMapper.countByCode(customer.getCode()) > 0) {
            customer.setCode(genCode());
        } else {
            genCode();
        }
        if (customer.getSupplierId() != null) {
            CustomerSupplier customerSupplier = new CustomerSupplier();
            customerSupplier.setCustomerId(customer.getId());
            customerSupplier.setSupplierId(customer.getSupplierId());
            customerSupplierMapper.insert(customerSupplier);
        }
        // 期初
        OriginComegoDebt originComegoDebt = new OriginComegoDebt();
        originComegoDebt.setComegoId(customer.getId());
        originComegoDebt.setDebt(customer.getOriginDebt());
        originComegoDebt.setCat("1");
        originComegoDebt.setId(IDUtil.getSnowflakeId());
        originComegoDebt.setParentId(0L);
        originComegoDebtService.saveData(originComegoDebt);
        if (customerMapper.insertSelective(customer) > 0) {
            return Result.okmsg("新增成功！");
        } else {
            return Result.okmsg("新增失败！");
        }
    }

    public Result updateCusData(Customer customer) {
        if (customer.getSupplierId() != null) {
            CustomerSupplier customerSupplier = new CustomerSupplier();
            customerSupplier.setCustomerId(customer.getId());
            customerSupplier.setSupplierId(customer.getSupplierId());
            customerSupplierService.saveOrUpdatebycustomer(customerSupplier);
        } else {
            customerSupplierMapper.deleteByCustomerId(customer.getId());
        }
        Customer postCustomer = customerMapper.selectByPrimaryKey(customer.getId());
        if (!postCustomer.getOriginDebt().equals(customer.getOriginDebt())) {
            originComegoDebtService.updateLastOriginData(customer.getOriginDebt(), customer.getId());
        }
        if (customerMapper.updateByPrimaryKeySelective(customer) > 0) {
            return Result.okmsg("修改成功！");
        } else {
            return Result.fail("修改失败！");
        }
    }

    @Override
    public Result listPage(PageParams<Customer> pageParams) {
        Page<Customer> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        UserInfo userInfo = (UserInfo) request.getAttribute("userInfo");
        Long userId = (Long) request.getAttribute("userId");
        if (ElmType.manager.getCode().equals(userInfo.getEmpType())) {
            // 管理员
            data.put("isCanViewOtherUserCustomer", "1");
        } else {
            SysDataPermission dataPermission = userInfo.getDataPermission();
            String isCanViewOtherUserCustomer = dataPermission.getIsCanViewOtherUserCustomer();
            data.put("isCanViewOtherUserCustomer", isCanViewOtherUserCustomer);
            data.put("employeeId", userId);
        }
        page.setRecords(customerMapper.listPage(page, data));
        return Result.ok(page);
    }

    @Override
    public Result listOptions(String keywords) {
        return Result.ok(customerMapper.listOptions(keywords));
    }

    @Override
    public Result deleteById(Long id) {
        if (billCountService.countByComegoId(id) > 0) {
            return Result.fail("该客户已有单据引用，不能删除！");
        }
        if (customerMapper.deleteByPrimaryKey(id) <= 0) {
            return Result.fail("删除失败");
        } else {
            return Result.okmsg("删除成功");
        }
    }

    @Override
    public Result getDataById(Long id) {
        Customer customer = customerMapper.findById(id);
        if (customer == null) {
            return Result.fail("该客户不存在");
        } else {
            return Result.ok(customer);
        }
    }

    /**
     * 获取欠款
     * 应收欠款 = 期初 + 新增欠款 - 收回欠款 - 优惠额
     *
     * @param id
     * @return
     */
    @Override
    public Result getDebtById(Long id) throws CustomsException {
        Customer customer = customerMapper.selectByPrimaryKey(id);
        if (customer == null) {
            return Result.fail("该客户不存在！");
        }
        BigDecimal originDebt = customer.getOriginDebt() == null
                ? BigDecimal.ZERO : customer.getOriginDebt();
        BigDecimal amounrAddDebt = customer.getDebt() == null ?
                BigDecimal.ZERO : customer.getDebt();
        return Result.ok(originDebt.add(amounrAddDebt));
    }

    @Override
    public Result listDataByName(String name) {
        return Result.ok(customerMapper.findByName(name));
    }

    public Customer addDebt(Long comegoId, BigDecimal disDebt) {
        Customer customer = customerMapper.selectByPrimaryKey(comegoId);
        if (customer == null) {
            throw new BusinessException("该客户不存在");
        }
        BigDecimal targetDebt = customer.getDebt().add(disDebt);
        customer.setDebt(targetDebt);
        customerMapper.updateByPrimaryKeySelective(customer);
        return customer;
    }

    @Override
    public Result getCode() {
        return Result.ok(orderNoGenerator.getCustomerCode("KH", 4));
    }

    @Override
    public Result getExcleTemp() throws IOException, WriteException {
        ExcleDefaultTempGenerater tempGenerater = new ExcleDefaultTempGenerater(ExcleCustomerData.class, "sasa");
        ByteArrayOutputStream outputStream = tempGenerater.createExcleTemp("客户资料");
        String fileName = "客户资料导入模板.xls";
        excleServieUtils.stream2excle(fileName, outputStream);
        return Result.ok();
    }

    @Override
    public Result exportData() throws IOException, WriteException {
        List<ExcleCustomerData> excleCustomerDataList = customerMapper.exportData();
        ExcleDefaultTempGenerater tempGenerater = new ExcleDefaultTempGenerater(ExcleCustomerData.class, "sa");
        ByteArrayOutputStream outputStream = tempGenerater.listData2Excle(excleCustomerDataList, "客户资料");
        String fileName = "客户资料列表.xls";
        excleServieUtils.stream2excle(fileName, outputStream);
        return Result.ok();
    }

    @Override
    public Result importData() throws IOException, WriteException {
        return null;
    }

    @Override
    public Result clearAllData() {
        return Result.ok();
    }

    private String genCode() {
        return orderNoGenerator.genCustomerCode("KH", 4);
    }

    /**
     * 取消默认
     */
    private void cancleDefault() {
        customerMapper.cancleDefault();
    }
}
