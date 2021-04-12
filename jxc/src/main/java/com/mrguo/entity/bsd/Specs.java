package com.mrguo.entity.bsd;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Table(name = "bsd_specs")
public class Specs {
    /**
     * ID
     */
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 分类名
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 父节点
     */
    @Column(name = "p_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pid;

    /**
     * 是否默认
     */
    @Column(name = "is_default")
    private String isDefault;
}