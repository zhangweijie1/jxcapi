package com.mrguo.service.impl.basedata;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bsd.CustomerCatMapper;
import com.mrguo.dao.bsd.CustomerMapper;
import com.mrguo.entity.bsd.CustomerCat;
import com.mrguo.entity.bsd.SupplierCat;
import com.mrguo.util.MathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户类别Service实现类
 *
 * @author mrguo
 */
@Service("customerCatServiceImpl")
public class CustomerCatServiceImpl extends BaseServiceImpl<CustomerCat> {

    @Autowired
    private CustomerCatMapper customerCatMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public MyMapper<CustomerCat> getMapper() {
        return customerCatMapper;
    }

    public Result addOrUpdateData(CustomerCat customerCat) throws CustomsException {
        String name = customerCat.getName();
        if (StringUtils.isBlank(name)) {
            return Result.fail("分类名称不能为空！");
        }
        if (customerCatMapper.countByName(name) > 0) {
            return Result.fail("分类【" + name + "】已存在！");
        }
        if (customerCat.getId() == null) {
            if (customerCat.getPid() == null) {
                return Result.fail("请选择父节点！");
            }
            customerCat.setId(getIdByPid(customerCat.getPid()));
            if (customerCatMapper.insertSelective(customerCat) > 0) {
                return Result.okmsg("新增成功！");
            } else {
                return Result.fail("新增失败！");
            }
        } else {
            if (customerCatMapper.updateByPrimaryKeySelective(customerCat) > 0) {
                return Result.okmsg("修改成功！");
            } else {
                return Result.fail("修改失败！");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result delete(String id) throws CustomsException {
        CustomerCat customerCat = new CustomerCat();
        customerCat.setPid(id);
        if (customerCatMapper.countChildrensByPid(id) > 0) {
            throw new CustomsException("存在子类,不能删除!");
        }
        if (customerMapper.countByCatId(id) > 0) {
            throw new CustomsException("该分类下存在客户,不能删除!");
        }
        if(customerCatMapper.deleteByPrimaryKey(id) > 0){
            return Result.ok("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    public List<CustomerCat> listAllData() {
        return customerCatMapper.selectAll();
    }

    public List<CustomerCat> findByParentId(String parentId) {
        return customerCatMapper.findByParentId(parentId);
    }

    public CustomerCat findById(String id) {
        return customerCatMapper.selectByPrimaryKey(id);
    }

    private String getIdByPid(String pid) {
        String maxIdByPid = customerCatMapper.getMaxIdByPid(pid);
        if (StringUtils.isBlank(maxIdByPid)) {
            // 没有子类别,直接给子类别0
            return pid + "0";
        } else {
            // 已有子类别，子类别+1
            return MathUtil.stringAddOne(maxIdByPid);
        }
    }
}
