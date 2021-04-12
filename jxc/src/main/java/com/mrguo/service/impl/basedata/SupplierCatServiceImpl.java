package com.mrguo.service.impl.basedata;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.bsd.SupplierCatMapper;
import com.mrguo.dao.bsd.SupplierMapper;
import com.mrguo.entity.bsd.SupplierCat;
import com.mrguo.util.MathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 供货商类别Service实现类
 *
 * @author mrguo
 */
@Service("supplierCatServiceImpl")
public class SupplierCatServiceImpl extends BaseServiceImpl<SupplierCat> {

    @Resource
    private SupplierCatMapper supplierCatMapper;
    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public MyMapper<SupplierCat> getMapper() {
        return supplierCatMapper;
    }

    public Result addOrUpdateData(SupplierCat supplierCat) throws CustomsException {
        String name = supplierCat.getName();
        if (StringUtils.isBlank(name)) {
            return Result.fail("分类名称不能为空！");
        }
        if (supplierCatMapper.countByName(name) > 0) {
            return Result.fail("分类【" + name + "】已存在！");
        }
        if (supplierCat.getId() == null) {
            if (supplierCat.getPid() == null) {
                return Result.fail("请选择父节点！");
            }
            supplierCat.setId(getIdByPid(supplierCat.getPid()));
            if (supplierCatMapper.insertSelective(supplierCat) > 0) {
                return Result.okmsg("新增成功！");
            } else {
                return Result.fail("新增失败！");
            }
        } else {
            if (supplierCatMapper.updateByPrimaryKeySelective(supplierCat) > 0) {
                return Result.okmsg("修改成功！");
            } else {
                return Result.fail("修改失败！");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws CustomsException {
        SupplierCat supplierCat = new SupplierCat();
        supplierCat.setPid(id);
        if (supplierCatMapper.countChildrensByPid(id) > 0) {
            throw new CustomsException("存在子类,不能删除!");
        }
        if (supplierMapper.countByCatId(id) > 0) {
            throw new CustomsException("该分类下存在供应商,不能删除!");
        }
        supplierCatMapper.deleteByPrimaryKey(id);
    }

    public List<SupplierCat> findByParentId(String parentId) {
        return supplierCatMapper.findByParentId(parentId);
    }

    public SupplierCat findById(String id) {
        return supplierCatMapper.selectByPrimaryKey(id);
    }


    public List<SupplierCat> getAllData() {
        return supplierCatMapper.selectAll();
    }

    private String getIdByPid(String pid) {
        String maxIdByPid = supplierCatMapper.getMaxIdByPid(pid);
        if (StringUtils.isBlank(maxIdByPid)) {
            // 没有子类别,直接给子类别0
            return pid + "0";
        } else {
            // 已有子类别，子类别+1
            return MathUtil.stringAddOne(maxIdByPid);
        }
    }
}
