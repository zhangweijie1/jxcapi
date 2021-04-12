package com.mrguo.entity.bsd;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Table(name = "bsd_level")
public class Level {
    /**
     * ID
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 等级编号
     */
    private String code;

    /**
     * 等级名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 折扣（%）
     */
    @NotNull(message = "折扣率不能为空")
    @Max( value = 100, message = "折扣率最大100")
    @Min( value = 0, message = "折扣率最小0")
    private BigDecimal discount;

    @Column(name = "is_sys")
    private String isSys;
}