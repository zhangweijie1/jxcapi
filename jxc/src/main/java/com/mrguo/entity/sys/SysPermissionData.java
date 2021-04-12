package com.mrguo.entity.sys;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName
 * @Description 数据权限表
 * @author 郭成兴
 * @date 2020/6/17 3:06 PM
 * @updater 郭成兴
 * @updatedate 2020/6/17 3:06 PM
 */
@Data
@Table(name = "sys_permission_data")
public class SysPermissionData {
    /**
     * 权限String
     */
    private String value;

    /**
     * 用户Id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;
}