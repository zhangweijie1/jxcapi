package com.mrguo.util.enums;

/**
 * @author 郭成兴
 * @ClassName BillCatEnum
 * @Description 单据类型枚举类
 * @date 2019/12/22 1:12 PM
 * @updater 郭成兴
 * @updatedate 2019/12/22 1:12 PM
 */
public enum BillCatEnum implements CodeEnum {
    origin("origin", "期初库存"),
    purchase_order("purchase_order", "进货订单"),
    purchase("purchase", "进货"),
    purchase_return("purchase_return", "进货退货"),
    sale_order("sale_order", "销售订单"),
    sale("sale", "销售"),
    sale_return("sale_return", "销售退货"),
    stock_in("stock_in", "入库"),
    stock_out("stock_out", "出库"),
    inventory("inventory", "库存盘点"),
    assemble("assemble", "组装拆卸单"),
    dispatch("dispatch", "库存调拨"),
    dispatch_out("dispatch_out", "调拨出库"),
    dispatch_in("dispatch_in", "调拨入库"),
    fin_daily("fin_daily","收支"),
    fin_dayily_in("fin_dayily_in", "日常收入"),
    fin_dayily_out("fin_dayily_out", "日常支出"),
    fin_comego_in("fin_comego_in", "收款单"),
    fin_comego_out("fin_comego_out", "付款单"),
    //下面不是单据
    sku("sku", "商品"),
    customer("customer", "客户"),
    supplier("supplier", "供应商");

    private String code;

    private String message;

    private BillCatEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByCode(String code) {
        for (BillCatEnum billCatEnum : BillCatEnum.values()) {
            if (code.equals(billCatEnum.getCode())) {
                return billCatEnum.getMessage();
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

