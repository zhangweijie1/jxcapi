package com.mrguo.service.impl.basedata;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.bsd.CustomerMapper;
import com.mrguo.dao.bsd.LevelMapper;
import com.mrguo.entity.bsd.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Service
public class LevelServiceImpl extends BaseServiceImpl<Level> {

    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private HttpServletRequest request;


    @Override
    public MyMapper<Level> getMapper() {
        return levelMapper;
    }

    public Result addOrUpdateData(Level level) {
        if (level.getId() == null) {
            return addData(level);
        } else {
            if (levelMapper.updateByPrimaryKeySelective(level) > 0) {
                return Result.okmsg("修改成功！");
            } else {
                return Result.fail("修改失败！");
            }
        }
    }

    private Result addData(Level level) {
        if (levelMapper.countByCode(level.getCode()) > 0) {
            return Result.fail("该编号已存在！");
        }
        level.setId(IDUtil.getSnowflakeId());
        level.setIsSys("0");
        if (levelMapper.insertSelective(level) > 0) {
            return Result.okmsg("新增成功！");
        } else {
            return Result.fail("新增失败！");
        }
    }

    public Result delData(Long id) {
        // 客户是否引用
        if (customerMapper.countByLevelId(id) > 0) {
            return Result.fail("该等级已有客户引用，不能删除!");
        }
        if (levelMapper.deleteByPrimaryKey(id) > 0) {
            return Result.okmsg("删除成功！");
        } else {
            return Result.okmsg("删除失败！");
        }
    }

    public Result listPage(PageParams<Level> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Level> page = pageParams.getPage();
        page.setRecords(levelMapper.listPage(page, data));
        return Result.ok(page);
    }

    public Result getOptions() {
        HashMap<String, Object> data = new HashMap<>();
        return Result.ok(levelMapper.listOptions(data));
    }

    public Result listAllData() {
        return Result.ok(levelMapper.selectAll());
    }
}
