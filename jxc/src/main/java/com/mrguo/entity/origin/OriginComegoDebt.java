package com.mrguo.entity.origin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "origin_comego_debt")
public class OriginComegoDebt {
    /**
     * 主键
     */
    private Long id;

    @Column(name = "comego_id")
    private Long comegoId;

    /**
     * 欠款
     */
    private BigDecimal debt;

    @Column(name = "balance_time")
    private Date balanceTime;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * 0供应商 ,1客户
     */
    private String cat;

    @Column(name = "parent_id")
    private Long parentId;
}