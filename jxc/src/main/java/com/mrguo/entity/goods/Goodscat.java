package com.mrguo.entity.goods;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

/**
 * 商品类别实体
 *
 * @author mrguo
 */
@Data
@Table(name = "bsd_goods_cat")
public class Goodscat {

    /**
     * 编号
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /**
     * 类别名称
     */
    @NotBlank(message = "名称不能为空")
    @Column(length = 50)
    private String name;

    @Column(name = "is_default")
    private String isDefault;

    /**
     * 父菜单Id
     */
    @Column(name = "p_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String pid;

}
