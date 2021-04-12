package com.mrguo.service.impl.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.fin.FinBillMapper;
import com.mrguo.dao.fin.FinCapitalCatMapper;
import com.mrguo.entity.fin.FinCapitalCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service("finPayCatServiceImpl")
public class FinCapitalCatServiceImpl extends BaseServiceImpl<FinCapitalCat> {

    @Autowired
    private FinCapitalCatMapper finCapitalCatMapper;
    @Autowired
    private FinBillMapper finBillMapper;

    @Override
    public MyMapper<FinCapitalCat> getMapper() {
        return finCapitalCatMapper;
    }

    public List<FinCapitalCat> getPayOptions() {
        return finCapitalCatMapper.getPayOptions();
    }

    public List<FinCapitalCat> getReceOptions() {
        return finCapitalCatMapper.getReceOptions();
    }

    @Override
    public FinCapitalCat saveData(FinCapitalCat entity) {
        entity.setId(IDUtil.getSnowflakeId());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(entity.getCreateTime());
        return super.saveData(entity);
    }

    public Result delData(Long id) throws CustomsException {
        if (finBillMapper.countByCapitalCat(id) > 0) {
            return Result.fail("删除失败", "该类别已被引用！");
        }
        if (finCapitalCatMapper.deleteByPrimaryKey(id) <= 0) {
            return Result.fail("删除失败！");
        }
        return Result.ok();
    }

    public Result listPage(PageParams<FinCapitalCat> pageParams) {
        Page<FinCapitalCat> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(finCapitalCatMapper.listCustom(page, data));
        return Result.ok(page);
    }
}
