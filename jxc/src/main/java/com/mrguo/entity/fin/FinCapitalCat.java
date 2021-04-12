package com.mrguo.entity.fin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Table(name = "fin_capital_cat")
public class FinCapitalCat {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 账目类型
     */
    @NotBlank(message = "账目类型不能为空")
    private String name;

    @Transient
    @JsonSerialize(using = ToStringSerializer.class)
    private Long value;
    @Transient
    private String label;


    /**
     * 支出方向
     */
    private String direction;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private String keywords;
}