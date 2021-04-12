package com.mrguo.entity.bsd;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Table(name = "bsd_customer_cat")
public class CustomerCat {
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