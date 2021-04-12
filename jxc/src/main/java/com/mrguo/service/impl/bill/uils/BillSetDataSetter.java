package com.mrguo.service.impl.bill.uils;

import com.mrguo.common.utils.IDUtil;
import com.mrguo.entity.bill.Bill;
import com.mrguo.entity.bill.BillDetail;
import com.mrguo.entity.sys.UserInfo;
import com.mrguo.util.business.UserInfoThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: BillSetDataSetter
 * @Description:  初始化设置单据的值
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 1:37 下午
 * @Copyright 南通市韶光科技有限公司
 **/
public class BillSetDataSetter {

    public void setBillAndDetailList(Bill bill,
                                     List<BillDetail> billDetailList) {

        Date date = new Date();
        UserInfo userInfo = (UserInfo) UserInfoThreadLocalUtils.get();
        BillSkuNamesComputer billSkuNamesComputer = BillUtilsGenerator.getBillSkuNamesComputer();
        // 处理数据
        bill.setId(IDUtil.getSnowflakeId());
        bill.setIsCancle("0");
        bill.setIsClose("0");
        bill.setAuditStatus("0");
        bill.setCreateTime(date);
        bill.setCreateUserId(userInfo.getUserId());
        bill.setUpdateTime(date);
        bill.setUpdateUserId(userInfo.getUserId());
        bill.setAmountDebt(bill.getAmountPayable().subtract(bill.getAmountPaid()));
        bill.setGoodsNamestr(billSkuNamesComputer.getSkuNamesOfDetailList(billDetailList));
        for (BillDetail billDetail : billDetailList) {
            billDetail.setId(IDUtil.getSnowflakeId());
            billDetail.setBillRelationId(billDetail.getId());
            billDetail.setBillId(bill.getId());
            billDetail.setStoreId(bill.getStoreId());
            if (StringUtils.isBlank(billDetail.getDirection())) {
                billDetail.setDirection(bill.getDirection());
            }
            if (billDetail.getUnitMulti() == null) {
                billDetail.setUnitMulti(BigDecimal.ONE);
            }
        }
    }
}
