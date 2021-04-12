package com.mrguo.service.impl.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.entity.Result;
import com.mrguo.dao.fin.FinComegoPayDebtMapper;
import com.mrguo.dao.fin.FinComegoReceDebtMapper;
import com.mrguo.entity.sys.SysDataPermission;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.enums.ElmType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName FinComegoPayDebtServiceImpl
 * @Description 应付欠款
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service
public class FinComegoReceDebtServiceImpl {

    @Autowired
    private FinComegoReceDebtMapper finComegoReceDebtMapper;
    @Autowired
    private HttpServletRequest request;


    public Result listDataGroupByComegoPage(PageParams<Map<String, Object>> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Map<String, Object>> page = pageParams.getPage();
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
        page.setRecords(finComegoReceDebtMapper.listDataGroupByComegoPage(page, data));
        return Result.ok(page);
    }

    public Result listDebtDetailByComegoId(PageParams<Map<String, Object>> pageParams) {
        Map<String, Object> data = pageParams.getData();
        Page<Map<String, Object>> page = pageParams.getPage();
        page.setRecords(finComegoReceDebtMapper.listDebtDetailByComegoId(page, data));
        return Result.ok(page);
    }

    public Map<String, Object> getStatistics(Map<String, Object> data) {
        return finComegoReceDebtMapper.selectStatistics(data);
    }
}
