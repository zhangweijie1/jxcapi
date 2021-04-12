package com.mrguo.entity.sys;

import lombok.Data;

/**
 * @author 郭成兴
 * @ClassName UserInfo
 * @Description 用于后端存放用户信息的，因为功能权限较大，故分开放
 * @date 2020/6/1712:36 PM
 * @updater 郭成兴
 * @updatedate 2020/6/1712:36 PM
 */
@Data
public class UserInfo {

    /**
     * 员工类型
     */
    private String empType;

    /**
     * 数据权限
     */
    private SysDataPermission dataPermission;

    private Long userId;
}
