package com.mrguo.service.impl.basedata;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bsd.SpecsMapper;
import com.mrguo.entity.bsd.Specs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/6/1110:28 AM
 * @updater 郭成兴
 * @updatedate 2020/6/1110:28 AM
 */
@Service
public class SpecsServiceImpl extends BaseServiceImpl<Specs> {

    @Autowired
    private SpecsMapper specsMapper;

    @Override
    public MyMapper<Specs> getMapper() {
        return specsMapper;
    }

    public int countNum() {
        return specsMapper.selectCount();
    }

    public Result saveOrUpdateData(Specs entity) throws CustomsException {
        String name = entity.getName();
        if (specsMapper.selectCountByName(name) > 0) {
            throw new CustomsException("规格名:[" + name + "]已存在");
        }
        if (entity.getPid() == null) {
            entity.setPid((long) -1);
        }
        if (entity.getId() == null) {
            entity.setId(IDUtil.getSnowflakeId());
            return Result.ok(super.saveData(entity));
        } else {
            if (super.updateData(entity) > 0) {
                return Result.okmsg("修改成功");
            }
            return Result.fail("修改失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Result delData(Long id) {
        // 1. 判断是否被商品引用, 暂时不需要校验
        // 2. 是否是父节点
        Specs specs = specsMapper.selectByPrimaryKey(id);
        if (specs.getPid() == -1) {
            // 是父节点，删除子节点
            specsMapper.deleteChildrensByPid(id);
        }
        if (specsMapper.deleteByPrimaryKey(id) <= 0) {
            return Result.fail("删除失败！");
        } else {
            return Result.okmsg("删除成功！");
        }
    }

    public List<Specs> listDataByNames(List<String> names) {
        return specsMapper.listDataByNames(names);
    }
}
