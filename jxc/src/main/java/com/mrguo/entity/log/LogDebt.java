package com.mrguo.entity.log;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "log_debt")
public class LogDebt {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 0应付，1应收
     */
    private String cat;

    /**
     * 往来单位
     */
    @Column(name = "comego_id")
    private Long comegoId;

    /**
     * 单据ID
     */
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "is_cancle_bill")
    private String isCancleBill;

    /**
     * 原单欠款
     */
    private BigDecimal amount;

    /**
     * 结余欠款
     */
    @Column(name = "remain_amount")
    private BigDecimal remainAmount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}