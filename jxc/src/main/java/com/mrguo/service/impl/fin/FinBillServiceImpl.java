package com.mrguo.service.impl.fin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.entity.PageParams;
import com.mrguo.common.exception.CustomsException;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.common.utils.IDUtil;
import com.mrguo.dao.fin.FinBillMapper;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.fin.FinBill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class FinBillServiceImpl extends BaseServiceImpl<FinBill> {

    @Autowired
    private FinBillMapper finBillMapper;

    @Override
    public MyMapper<FinBill> getMapper() {
        return finBillMapper;
    }

    public FinBill addData(FinBill entity) {
        entity.setId(IDUtil.getSnowflakeId());
        entity.setCreateTime(new Date());
        entity.setUpdateTime(entity.getCreateTime());
        super.saveData(entity);
        return entity;
    }

    public Bill finBill2bill(FinBill finBill) {
        Bill bill = new Bill();
        BeanUtils.copyProperties(finBill, bill);
        return bill;
    }

    public void cancleBill(FinBill finBill) throws CustomsException {
        finBill.setIsCancle("1");
        if (finBillMapper.updateByPrimaryKeySelective(finBill) == 0) {
            throw new CustomsException("作废失败！");
        }
    }

    public IPage<FinBill> listData(PageParams<FinBill> pageParams) {
        Page<FinBill> page = pageParams.getPage();
        Map<String, Object> data = pageParams.getData();
        page.setRecords(finBillMapper.listCustom(page, data));
        return page;
    }
}
