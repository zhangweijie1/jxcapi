package com.mrguo.entity.goods;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 * @author 郭成兴
 * @ClassName Goodssku
 * @Description sku表
 * @date 2019/11/13 1:59 PM
 * @updater 郭成兴
 * @updatedate 2019/11/13 1:59 PM
 */

@Data
@Table(name = "bsd_goods_sku")
@TableName("bsd_goods_sku")
public class GoodsSku {
    /**
     * 商品SKU_ID
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 商品SPU_ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "spu_id")
    private Long spuId;

    /**
     * 商品标题
     */
    private String name;


    private String code;

    /**
     * 商品规格
     */
    private String specs;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 所属类目，叶子类目Id
     */
    @Column(name = "cat_id")
    private String catId;

    /**
     * 所属类目，叶子类目Name
     */
    @Column(name = "cat_name")
    private String catName;

    @Column(name = "is_enable_specs")
    private String isEnableSpecs;

    /**
     * 商品状态，0-下架，1-正常，2-删除
     */
    private String status;

    /**
     * 是否启用 0否，1是
     */
    @Column(name = "is_enable")
    private String isEnable;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 缩略图
     */
    @Column(name = "thumbnail")
    private String thumbnail;

    /**
     * 品牌Id
     */
    @Column(name = "brand_id")
    private String brandId;

    /**
     * 备注
     */
    private String remarks;


    @Transient
    @Column(name = "barcode")
    private String barcode;
    @Transient
    @Column(name = "price")
    private String price;
    @Transient
    @Column(name = "price_str")
    private String priceStr;
    @Transient
    @Column(name = "cost_price")
    private String costPrice;
    @Transient
    @Column(name = "unit_id")
    private String unitId;
    @Transient
    @Column(name = "unit_name")
    private String unitName;
    @Transient
    @Column(name = "unit_multi")
    private String unitMulti;
    @Transient
    @Column(name = "unit_isbase")
    private String unitIsbase;
    @Transient
    @Column(name = "is_base_unit")
    private String isBaseUnit;
    @Transient
    @Column(name = "unit_id_str")
    private String unitIdStr;
    @Transient
    @Column(name = "unit_name_str")
    private String unitNameStr;
    @Transient
    @Column(name = "unit_multi_str")
    private String unitMultiStr;
    @Transient
    @Column(name = "unit_isbase_str")
    private String unitIsbaseStr;
    @Transient
    @Column(name = "is_base_unit_str")
    private String isBaseUnitStr;

    /**
     * 关键词，搜索时用到
     */
    @Transient
    private String keywords;
    @Transient
    @Column(name = "origin_quantity")
    private Integer originQuantity;
    @Transient
    @Column(name = "quantity_in")
    private Integer quantityIn;
    @Transient
    @Column(name = "quantity_out")
    private Integer quantityOut;
    @Transient
    @Column(name = "wait_quantity_in")
    private Integer waitQuantityIn;
    @Transient
    @Column(name = "wait_quantity_out")
    private Integer waitQuantityOut;
    @Transient
    @Column(name = "store_id")
    private Long storeId;
}