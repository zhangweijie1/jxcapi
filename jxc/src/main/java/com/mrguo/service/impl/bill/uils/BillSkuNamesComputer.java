package com.mrguo.service.impl.bill.uils;

import com.mrguo.entity.bill.BillDetail;

import java.util.List;

/**
 * @ClassName: BillSkuNamesComputer
 * @Description: 获取单据明细所有商品名称
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 1:38 下午
 * @Copyright 南通市韶光科技有限公司
 **/
public class BillSkuNamesComputer {

    public String getSkuNamesOfDetailList(List<BillDetail> detailList) {
        StringBuilder sb = new StringBuilder();
        for (BillDetail b : detailList) {
            sb.append(b.getName()).append(", ");
        }
        return sb.toString();
    }
}
