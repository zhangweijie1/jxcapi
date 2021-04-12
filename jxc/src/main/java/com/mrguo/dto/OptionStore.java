package com.mrguo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/121:52 PM
 * @updater 郭成兴
 * @updatedate 2019/12/121:52 PM
 */
@Data
public class OptionStore extends Option {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    @Column(name = "p_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pId;

    @Column(name = "is_default")
    private String isDefault;

    @Column(name = "is_lock")
    private String isLock;
}
