package com.mrguo.service.impl.basedata;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bsd.UnitMapper;
import com.mrguo.entity.bsd.Unit;
import com.mrguo.service.impl.basedata.goods.GoodsUnitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName UnitServiceImpl
 * @Description 单位服务层
 * @date 2019/11/149:28 AM
 * @updater 郭成兴
 * @updatedate 2019/11/149:28 AM
 */
@Service
public class UnitServiceImpl extends BaseServiceImpl<Unit> {

    @Autowired
    private UnitMapper unitMapper;
    @Autowired
    private GoodsUnitServiceImpl goodsUnitService;

    @Override
    public MyMapper<Unit> getMapper() {
        return unitMapper;
    }

    public Result saveOrUpdateData(Unit entity) {
        if (entity.getId() == null) {
            entity.setId(IDUtil.getSnowflakeId());
            super.saveData(entity);
        } else {
            super.updateData(entity);
        }
        return Result.ok();
    }

    public Result delData(Long unitId) {
        if (goodsUnitService.countByUnitId(unitId) > 0) {
            return Result.fail("该单位已被商品引用，不能删除！");
        }
        if (this.deleteData(unitId) == 0) {
            return Result.fail("删除0条数据！");
        }
        return Result.ok("删除成功！");
    }

    public Result listPage(PageParams<Unit> pageParams) throws IOException {
        Page<Unit> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        List<Unit> units = unitMapper.listPage(page, data);
        page.setRecords(units);
        return Result.ok(page);
    }

    public Result listOptions() throws IOException {
        return Result.ok(unitMapper.listOptions());
    }

    public List<Unit> listDataByNames(List<String> names) {
        return unitMapper.listDataByNames(names);
    }
}
