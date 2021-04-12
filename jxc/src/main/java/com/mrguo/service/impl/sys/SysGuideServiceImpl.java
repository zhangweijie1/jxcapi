package com.mrguo.service.impl.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.common.utils.MapToEntityUtil;
import com.mrguo.dao.sys.SysEmployeeMapper;
import com.mrguo.entity.sys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/2710:03 AM
 * @updater 郭成兴
 * @updatedate 2019/12/2710:03 AM
 */
@Service("sysGuideServiceImpl")
public class SysGuideServiceImpl {

    @Autowired
    private SysEmployeeMapper sysEmployeeMapper;
    @Autowired
    private HttpServletRequest request;

    @Transactional(rollbackFor = Exception.class)
    public Result addOrUpdateData(SysEmployee employee) {
        if (employee.getId() != null) {
            return updateData(employee);
        } else {
            return addData(employee);
        }
    }

    public Result delDataById(Long id) {
        if (sysEmployeeMapper.deleteByPrimaryKey(id) > 0) {
            return Result.okmsg("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }

    /**
     * 查询导购员
     *
     * @param pageParams
     * @return
     * @throws IOException
     */
    public Result listGuidePage(PageParams<SysEmployee> pageParams) throws IOException {
        Page<SysEmployee> page = pageParams.getPage();
        SysEmployee sysEmployee = MapToEntityUtil.map2Entity(pageParams.getData(), SysEmployee.class);
        ArrayList<String> types = new ArrayList<>();
        types.add("1");
        page.setRecords(sysEmployeeMapper.listPage(page, types, sysEmployee));
        return Result.ok(page);
    }


    private Result addData(SysEmployee sysEmployee) {
        Date date = new Date();
        sysEmployee.setId(IDUtil.getSnowflakeId());
        sysEmployee.setType("1");
        sysEmployee.setCreateTime(date);
        sysEmployee.setUpdateTime(date);
        if (sysEmployee.getStatus() == null) {
            sysEmployee.setStatus("1");
        }
        if (sysEmployeeMapper.insertSelective(sysEmployee) > 0) {
            return Result.okmsg("新增成功");
        } else {
            return Result.fail("新增失败");
        }
    }

    private Result updateData(SysEmployee employee) {
        if (sysEmployeeMapper.updateByPrimaryKey(employee) > 0) {
            return Result.okmsg("修改成功");
        } else {
            return Result.ok("修改失败");
        }
    }
}
