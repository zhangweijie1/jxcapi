package com.mrguo.service.impl.bill.basebill;

import com.mrguo.dao.bill.BillMapper;
import com.mrguo.service.inter.bill.BillCountService;
import com.mrguo.util.enums.BillCatEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("billCountImpl")
public class BillCountServiceImpl implements BillCountService {

    @Autowired
    private BillMapper billMapper;

    @Override
    public Integer countByComegoId(Long comegoId) {
        return billMapper.countByComegoId(comegoId);
    }

    @Override
    public Integer countByAccountId(Long accountId) {
        return billMapper.countByAccountId(accountId);
    }

    @Override
    public Integer countRelationBillByBillId(Long billId) {
        return billMapper.countRelationBillByBillId(billId);
    }

    @Override
    public Integer countRelationBillByBillIdAndCat(Long billId, BillCatEnum catEnum) {
        return billMapper.countRelationBillByBillIdAndCat(billId, catEnum.getCode());
    }
}
