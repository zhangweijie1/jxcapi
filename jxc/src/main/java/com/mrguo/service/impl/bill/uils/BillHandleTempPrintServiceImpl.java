package com.mrguo.service.impl.bill.uils;

import com.mrguo.entity.bill.*;
import com.mrguo.vo.bill.BillDiapatchVo;
import com.mrguo.vo.bill.BillAndDetailsVo;
import com.mrguo.vo.bill.BillInventoryDetailVo;
import com.mrguo.util.temp.PrintUtil;
import com.mrguo.vo.TempPrintVo;
import com.mrguo.vo.TempPrintColumns;
import com.mrguo.vo.TempPrintMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: BillHandleTempPrintServiceImpl
 * @Description: 单据打印
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/11/8 1:40 下午
 * @Copyright 南通市韶光科技有限公司
 **/
@Service
public class BillHandleTempPrintServiceImpl {

    @Autowired
    private PrintUtil printUtil;

    public HashMap<String, Object> getDefaultTempPrint(TempPrintVo print, BillAndDetailsVo<Bill> billBillAndDetailsVo) {
        List<TempPrintMaster> masters = print.getMaster();
        List<TempPrintColumns> cols = print.getCols();
        Bill bill = billBillAndDetailsVo.getBill();
        List<BillDetail> billDetailList = billBillAndDetailsVo.getBillDetailList();
        masters = printUtil.getTempPrintMasterData(masters, bill);
        HashMap<String, Object> result = new HashMap<>();
        result.put("masters", masters);
        result.put("cols", cols);
        result.put("details", billDetailList);
        return result;
    }

    public HashMap<String, Object> getDefaultTempPrint(TempPrintVo print,
                                                       Object bill,
                                                       List<BillDetail> detailList) {
        List<TempPrintMaster> masters = print.getMaster();
        List<TempPrintColumns> cols = print.getCols();
        masters = printUtil.getTempPrintMasterData(masters, bill);
        HashMap<String, Object> result = new HashMap<>();
        result.put("masters", masters);
        result.put("cols", cols);
        result.put("details", detailList);
        return result;
    }

    public HashMap<String, Object> getInventoryTempPrint(TempPrintVo print,
                                                         Bill bill,
                                                         List<BillInventoryDetailVo> detailList) {
        List<TempPrintMaster> masters = print.getMaster();
        List<TempPrintColumns> cols = print.getCols();
        masters = printUtil.getTempPrintMasterData(masters, bill);
        HashMap<String, Object> result = new HashMap<>();
        result.put("masters", masters);
        result.put("cols", cols);
        result.put("details", detailList);
        return result;
    }

    public HashMap<String, Object> getDispatchTempPrint(TempPrintVo print,
                                                        BillDiapatchVo bill,
                                                        List<BillDetail> detailList) {
        List<TempPrintMaster> masters = print.getMaster();
        List<TempPrintColumns> cols = print.getCols();
        masters = printUtil.getTempPrintMasterData(masters, bill);
        HashMap<String, Object> result = new HashMap<>();
        result.put("masters", masters);
        result.put("cols", cols);
        result.put("details", detailList);
        return result;
    }

    public HashMap<String, Object> getStockTempPrint(TempPrintVo print,
                                                     BillStock bill,
                                                     List<BillDetail> detailList) {
        List<TempPrintMaster> masters = print.getMaster();
        List<TempPrintColumns> cols = print.getCols();
        masters = printUtil.getTempPrintMasterData(masters, bill);
        HashMap<String, Object> result = new HashMap<>();
        result.put("masters", masters);
        result.put("cols", cols);
        result.put("details", detailList);
        return result;
    }

    public HashMap<String, Object> getBorrowTempPrint(TempPrintVo print,
                                                      BillBorrow bill,
                                                      List<BillDetail> detailList) {
        List<TempPrintMaster> masters = print.getMaster();
        List<TempPrintColumns> cols = print.getCols();
        masters = printUtil.getTempPrintMasterData(masters, bill);
        HashMap<String, Object> result = new HashMap<>();
        result.put("masters", masters);
        result.put("cols", cols);
        result.put("details", detailList);
        return result;
    }
}
