package com.mrguo.vo.basedata;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mrguo.entity.bsd.Store;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

/**
 * @author 郭成兴
 * @ClassName StoreDto
 * @Description 仓库数据
 * @date 2019/11/149:05 AM
 * @updater 郭成兴
 * @updatedate 2019/11/149:05 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StoreVo extends Store {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long value;

    private String label;
}
