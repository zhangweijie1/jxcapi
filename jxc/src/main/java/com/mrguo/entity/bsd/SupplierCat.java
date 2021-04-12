package com.mrguo.entity.bsd;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Table(name = "bsd_supplier_cat")
public class SupplierCat {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 分类名
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 父节点
     */
    @Column(name = "p_id")
    private String pid;

    @Column(name = "is_default")
    private String isDefault;
}