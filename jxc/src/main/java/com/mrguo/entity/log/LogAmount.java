package com.mrguo.entity.log;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "log_amount")
public class LogAmount {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 0支出，1收入
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
    @JsonSerialize(using = ToStringSerializer.class)
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