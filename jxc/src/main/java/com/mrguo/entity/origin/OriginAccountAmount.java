package com.mrguo.entity.origin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "origin_account_amount")
public class OriginAccountAmount {
    /**
     * 主键（账户ID）
     */
    @Id
    private Long id;

    @Column(name = "ac_id")
    private Long acId;

    /**
     * 期初余额
     */
    private BigDecimal amount;

    @Column(name = "balance_time")
    private Date balanceTime;

    @Column(name = "parent_id")
    private Long parentId;
}