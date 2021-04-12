package com.mrguo.entity.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author 郭成兴
 * @ClassName Goodsspu
 * @Description 商品SPU实体
 * @date 2019/11/13 1:59 PM
 * @updater 郭成兴
 * @updatedate 2019/11/13 1:59 PM
 */
@Data
@Table(name = "bsd_goods_spu")
public class GoodsSpu {
    /**
     * 编号
     */
    @Id
    @Column(name = "spu_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long spuId;

    /**
     * SPU商品编码
     */
    @Column(length = 50)
    private String code;

    /**
     * SPU商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Column(length = 50)
    private String name;

    /**
     * 副标题（促销信息）
     */
    @Column(length = 50)
    private String caption;

    /**
     * 选中的规格(前端显示)
     */
    @Column(name = "specs_list")
    private String specsList;

    /**
     * 是否启用规格
     */
    @Column(name = "is_enable_specs")
    private String isEnableSpecs;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private String isDelete;

    /**
     * 品牌
     */
    @Column(name = "brand_id")
    private String brandId;

    /**
     * 商品类别
     */
    @NotBlank(message = "商品分类不能Wie空")
    @Column(name = "cat_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String catId;

    /**
     * 备注
     */
    @Column(length = 1000)
    private String remarks;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_user")
    private Long updateUser;

    @Transient
    @Column(name = "cat_name")
    private String catName;
}
