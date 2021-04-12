package com.mrguo.service.impl.bill.uils;

import org.springframework.stereotype.Service;

/**
 * 和单据有关的所有计算
 */
@Service
public class BillUtilsGenerator {

    private final static BillSkuNamesComputer BILL_SKU_NAMES_COMPUTER = new BillSkuNamesComputer();
    private final static BillSkuQtyComputer BILL_SKU_QTY_COMPUTER = new BillSkuQtyComputer();
    private final static BillSetDataSetter BILL_SET_DATA_SETTER = new BillSetDataSetter();

    private BillUtilsGenerator() {
    }

    public static BillSkuNamesComputer getBillSkuNamesComputer() {
        return BILL_SKU_NAMES_COMPUTER;
    }

    public static BillSkuQtyComputer getBillSkuQtyComputer() {
        return BILL_SKU_QTY_COMPUTER;
    }

    public static BillSetDataSetter getBillSetDataSetter() {
        return BILL_SET_DATA_SETTER;
    }
}
