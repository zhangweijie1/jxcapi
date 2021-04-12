package com.mrguo.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/12/121:52 PM
 * @updater 郭成兴
 * @updatedate 2019/12/121:52 PM
 */
@Data
public class Option {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long value;

    private String label;
}
