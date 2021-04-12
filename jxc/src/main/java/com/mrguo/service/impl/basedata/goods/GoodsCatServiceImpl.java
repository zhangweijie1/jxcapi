package com.mrguo.service.impl.basedata.goods;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.goods.GoodscatMapper;
import com.mrguo.dao.goods.GoodsskuMapper;
import com.mrguo.entity.goods.Goodscat;
import com.mrguo.service.inter.bsd.GoodscatService;
import com.mrguo.util.MathUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品类别Service实现类
 *
 * @author mrguo
 */
@Service("goodsCatServiceImpl")
public class GoodsCatServiceImpl extends BaseServiceImpl<Goodscat> implements GoodscatService {

    @Autowired
    private GoodscatMapper goodscatMapper;
    @Autowired
    private GoodsskuMapper goodsskuMapper;

    @Override
    public MyMapper<Goodscat> getMapper() {
        return goodscatMapper;
    }

    @Override
    public Result addOrUpdateData(Goodscat goodscat) {
        String name = goodscat.getName();
        if (StringUtils.isBlank(name)) {
            return Result.fail("分类名称不能为空！");
        }
        if (goodscatMapper.countByName(name) > 0) {
            return Result.fail("分类【" + name + "】已存在！");
        }
        if (goodscat.getId() == null) {
            if (goodscat.getPid() == null) {
                return Result.fail("请选择父节点！");
            }
            goodscat.setId(getIdByPid(goodscat.getPid()));
            if (goodscatMapper.insertSelective(goodscat) > 0) {
                return Result.okmsg("新增成功！");
            } else {
                return Result.fail("新增失败！");
            }
        } else {
            if (goodscatMapper.updateByPrimaryKeySelective(goodscat) > 0) {
                return Result.okmsg("修改成功！");
            } else {
                return Result.fail("修改失败！");
            }
        }
    }

    @Override
    public int countByParentId(String parentId) {
        return goodscatMapper.countChildrens(parentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(String id) {
        if (countByParentId(id) > 0) {
            return Result.fail("删除失败", "该类别下有子类别！");
        }
        if (goodsskuMapper.countByCatId(id) > 0) {
            return Result.fail("删除失败", "该分类下已经有商品！");
        }
        if (goodscatMapper.deleteByPrimaryKey(id) > 0) {
            return Result.okmsg("删除成功");
        }
        return Result.fail("删除失败");
    }

    @Override
    public List<Goodscat> findByParentId(String parentId) {
        return goodscatMapper.findByParentId(parentId);
    }

    @Override
    public Goodscat findById(String id) {
        return goodscatMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Goodscat> listAllData() {
        return goodscatMapper.selectAll();
    }

    @Override
    public List<Goodscat> listDataByNames(List<String> names) {
        return goodscatMapper.listDataByNames(names);
    }

    private String getIdByPid(String pid) {
        String maxIdByPid = goodscatMapper.getMaxIdByPid(pid);
        if (StringUtils.isBlank(maxIdByPid)) {
            // 没有子类别,直接给子类别0
            return pid + "0";
        } else {
            // 已有子类别，子类别+1
            return MathUtil.stringAddOne(maxIdByPid);
        }
    }
}
