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
import com.mrguo.entity.fin.FinBill;
import com.mrguo.service.impl.bill.uils.BillOrderNoServiceImpl;
import com.mrguo.util.enums.BillCatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/3/154:00 PM
 * @updater 郭成兴
 * @updatedate 2020/3/154:00 PM
 */
@Service
public class FinPayDailyServiceImpl extends BaseServiceImpl<FinBill> {

    @Autowired
    private FinBillMapper finBillMapper;

    @Autowired
    private BillOrderNoServiceImpl billOrderNoService;

    @Override
    public MyMapper<FinBill> getMapper() {
        return finBillMapper;
    }

    /**
     * 日常-收入
     *
     * @param bill
     * @return
     */
    public Result addDayilyInData(FinBill bill) {
        bill.setDirection("1");
        bill.setBillCat(BillCatEnum.fin_dayily_in.getCode());
        bill.setBillCatName(BillCatEnum.fin_dayily_in.getMessage());
        return saveData(bill) == null
                ? Result.fail("新增失败！")
                : Result.ok(bill);
    }

    public Result getBillCode() throws CustomsException {
        return Result.ok(billOrderNoService.getBillCode(BillCatEnum.fin_dayily_in.getCode()));
    }

    /**
     * 日常-支出
     *
     * @param bill
     * @return
     */
    public Result addDayilyOutData(FinBill bill) {
        bill.setDirection("0");
        bill.setBillCat(BillCatEnum.fin_dayily_out.getCode());
        bill.setBillCatName(BillCatEnum.fin_dayily_out.getMessage());
        return saveData(bill) == null
                ? Result.fail("新增失败！")
                : Result.ok(bill);
    }

    public IPage listDayilyInData(PageParams<FinBill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("billCat", BillCatEnum.fin_dayily_in.getCode());
        return listData(pageParams);
    }

    public IPage listDayilyOutData(PageParams<FinBill> pageParams) {
        Map<String, Object> data = pageParams.getData();
        data.put("billCat", BillCatEnum.fin_dayily_out.getCode());
        return listData(pageParams);
    }

    @Override
    public FinBill saveData(FinBill entity) {
        entity.setId(IDUtil.getSnowflakeId());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(entity.getCreateTime());
        return super.saveData(entity);
    }

    public IPage<FinBill> listData(PageParams<FinBill> pageParams) {
        Page<FinBill> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(finBillMapper.listCustom(page, data));
        return page;
    }
}
