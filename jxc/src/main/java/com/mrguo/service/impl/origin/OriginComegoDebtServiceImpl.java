package com.mrguo.service.impl.origin;

import com.mrguo.common.dao.MyMapper;
import com.mrguo.common.service.BaseServiceImpl;
import com.mrguo.dao.origin.OriginComegoDebtMapper;
import com.mrguo.entity.origin.OriginComegoDebt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2020/7/158:42 AM
 * @updater 郭成兴
 * @updatedate 2020/7/158:42 AM
 */

@Service
public class OriginComegoDebtServiceImpl extends BaseServiceImpl<OriginComegoDebt> {

    @Autowired
    private OriginComegoDebtMapper originComegoDebtMapper;

    @Override
    public MyMapper<OriginComegoDebt> getMapper() {
        return originComegoDebtMapper;
    }

    /**
     * 修改最新的期初值
     * @param data
     */
    public void updateLastOriginData(BigDecimal data, Long comegoId){
        OriginComegoDebt originComegoDebt = originComegoDebtMapper.selectLastOriginData(comegoId);
        originComegoDebt.setDebt(data);
        originComegoDebtMapper.updateByPrimaryKeySelective(originComegoDebt);
    }
}
