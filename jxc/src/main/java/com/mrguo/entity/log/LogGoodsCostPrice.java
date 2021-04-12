package com.mrguo.entity.log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * @author 郭成兴
 * @ClassName LogGoodsCostPrice
 * @Description 成本价格明细
 * @date 2020/3/26 5:03 PM
 * @updater 郭成兴
 * @updatedate 2020/3/26 5:03 PM
 */
@Data
@Table(name = "log_goods_costprice")
public class LogGoodsCostPrice {
    /**
     * 商品ID
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 单据ID
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "bill_cat")
    private String billCat;

    @Column(name = "bill_cat_name")
    private String billCatName;

    /**
     * 结余成本价
     */
    @Column(name = "remain_price_cost")
    private BigDecimal remainPriceCost;

    /**
     * 当次成本价
     */
    @Column(name = "price_cost")
    private BigDecimal priceCost;

    @Column(name = "business_time")
    private Date businessTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 期初库存
     */
    @Transient
    @Column(name = "origin_quantity")
    private BigDecimal originQuantity;

    private BigDecimal quantity;

    /**
     * 结余数量
     */
    @Column(name = "remain_qty")
    private BigDecimal remainQty;

    @Transient
    @Column(name = "bill_no")
    private String billNo;

    @Transient
    private BigDecimal unitMulti;

    @Transient
    private String direction;
}