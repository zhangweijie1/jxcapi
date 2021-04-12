package com.mrguo.entity.sys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "t_bill_no_rule")
public class SysBillNoRule {

    /**
     * 单据类型
     */
    @Id
    @Column(name = "bill_cat")
    private String billCat;

    @Transient
    @Column(name = "bill_cat_name")
    private String billCatName;

    /**
     * 前缀字符
     */
    private String prefix;

    /**
     * 后缀长度
     */
    @Column(name = "suffix_len")
    private Integer suffixLen;
}