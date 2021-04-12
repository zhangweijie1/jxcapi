package com.mrguo.entity.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @ClassName: GoodsDesc
 * @Description:
 * @Author: 郭成兴（wx:512830037）
 * @Date 2020/12/11 9:35 上午
 * @Copyright 如皋市韶光科技有限公司
 **/
@Data
public class GoodsDesc {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 描述
     */
    private String introduction;

    /**
     * 规格结果集，所有规格，包含isSelected
     */
    @Column(name = "specification_items")
    private String specificationItems;

    /**
     * 自定义属性（参数结果）
     */
    @Column(name = "custom_attribute_items")
    private String customAttributeItems;

    @Column(name = "item_images")
    private String itemImages;

    /**
     * 包装列表
     */
    @Column(name = "package_list")
    private String packageList;

    /**
     * 售后服务
     */
    @Column(name = "sale_service")
    private String saleService;
}
