package com.mrguo.entity.log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * @author 郭成兴
 * @ClassName LogGoodsStock
 * @Description 商品库存日志
 * 库存日志需要参数：
 * skuId，业务日期，出入库单号，门店，仓库，关联业务（销售等），往来单位，出入库量
 * @date 2020/3/27 10:08 PM
 * @updater 郭成兴
 * @updatedate 2020/3/27 10:08 PM
 */
@Data
@Table(name = "log_goods_stock")
public class LogGoodsStock {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 商品SKU_Id
     */
    @Column(name = "sku_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    /**
     * 关联业务单据
     */
    @Column(name = "bill_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billId;

    /**
     * 余量数量（全是基于基础单位）
     */
    @Column(name = "rem_qty_in")
    private BigDecimal remQtyIn;
    @Column(name = "rem_qty_out")
    private BigDecimal remQtyOut;
    @Column(name = "rem_waite_qty_in")
    private BigDecimal remWaiteQtyIn;
    @Column(name = "rem_waite_qty_out")
    private BigDecimal remWaiteQtyOut;
    /**
     * 进出库方向 (1进 0出)
     */
    @Column(name = "direction")
    private String direction;

    /**
     * 单据类型（0期初库存）
     */
    @Column(name = "bill_cat")
    private String billCat;

    @Column(name = "comego_id")
    private Long comegoId;

    @Column(name = "comego_name")
    private String comegoName;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 业务日期
     */
    @Column(name = "business_time")
    private Date businessTime;

    /**
     * 入出库量
     */
    @Column(name = "quantity")
    private BigDecimal quantity;

    /**
     * 起始时间，查询时用到
     */
    @Column(name = "start_time")
    @Transient
    private String startTime;
    @Column(name = "end_time")
    @Transient
    private String endTime;
    @Transient
    @Column(name = "bill_no")
    private String billNo;
    @Transient
    @Column(name = "bill_stock_no")
    private String billStockNo;
    @Transient
    @Column(name = "bill_cat_name")
    private String billCatName;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "store_id")
    private Long storeId;
    @Transient
    @Column(name = "store_name")
    private String storeName;

    @Transient
    @Column(name = "bill_cancle")
    private String billCancle;
    @Transient
    @Column(name = "stock_cancle")
    private String stockCancle;

    @Transient
    @Column(name = "stock_quantity")
    private BigDecimal stockQuantity;
}