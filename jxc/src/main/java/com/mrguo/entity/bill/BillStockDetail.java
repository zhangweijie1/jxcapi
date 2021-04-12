package com.mrguo.entity.bill;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "t_bill_stock_detail")
public class BillStockDetail {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 单据Id
     */
    @Column(name = "bill_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long billId;

    /**
     * 商品sku_id
     */
    @Column(name = "sku_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    /**
     * 商品编码
     */
    private String code;

    /**
     * 商品名
     */
    private String name;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 总价
     */
    private BigDecimal total;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 下拉框数据（用于展示）
     */
    private String options;
}