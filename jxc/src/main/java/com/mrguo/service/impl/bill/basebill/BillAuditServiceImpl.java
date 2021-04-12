package com.mrguo.service.impl.bill.basebill;

import com.mrguo.common.entity.Result;
import com.mrguo.dao.bill.BillMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.service.inter.bill.BillAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 郭成兴
 * @ClassName BillServiceImpl
 * @Description 单据表
 * @date 2019/12/175:08 PM
 * @updater 郭成兴
 * @updatedate 2019/12/175:08 PM
 */
@Service("billAuditServiceImpl")
public class BillAuditServiceImpl implements BillAuditService {

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private BillDetailServiceImpl billDetailService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public Result passAuditList(List<Long> ids) {
        Bill bill = new Bill();
        Long userId = (Long) request.getAttribute("userId");
        for (Long id : ids) {
            bill.setId(id);
            bill.setAuditStatus("1");
            bill.setAuditUserId(userId);
            billMapper.updateByPrimaryKeySelective(bill);
        }
        return Result.ok();
    }

    @Override
    public Result antiAuditList(List<Long> billIds) {
        ArrayList<Bill> bills = new ArrayList<Bill>();
        for (Long id : billIds) {
            Integer changeQtyByBillId = billDetailService.getChangeQtyByBillId(id);
            if (changeQtyByBillId > 0) {
                return Result.fail("已经有转进货的单据, 请重新选择！");
            }
            Bill bill = new Bill();
            bill.setId(id);
            bill.setAuditStatus("0");
            bills.add(bill);
        }
        int i = billMapper.batchUpdateByPrimaryKeySelective(bills);
        if (i < 0) {
            return Result.fail("反审核失败！");
        }
        return Result.ok();
    }
}
