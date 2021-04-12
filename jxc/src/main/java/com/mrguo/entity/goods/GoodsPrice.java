package com.mrguo.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: GoodsPrice
 * @Description: 商品价格
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/12/11 9:38 上午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Data
@Table(name = "bsd_goods_price")
@TableName("bsd_goods_price")
public class GoodsPrice {

    @Id
    @Column(name = "sku_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long skuId;

    /**
     * 单位Id
     */
    @Id
    @Column(name = "unit_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long unitId;

    /**
     * 价格json
     */
    private String price;
}
