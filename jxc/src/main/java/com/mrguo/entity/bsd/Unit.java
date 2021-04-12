package com.mrguo.entity.bsd;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Table(name = "bsd_unit")
public class Unit {
    /**
     * 主键
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 单位名
     */
    @NotBlank(message = "单位名称不能为空")
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态是否可用（0不可，1可）
     */
    private String status;

    /**
     * 是否默认
     */
    @Column(name = "is_default")
    private String isDefault;

    /**
     * 搜索关键字
     */
    @Transient
    private String keywords;
}