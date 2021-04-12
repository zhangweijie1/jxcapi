package com.mrguo.service.inter.bill.sys;

import java.util.List;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/1/310:30 AM
 * @updater 郭成兴
 * @updatedate 2019/1/310:30 AM
 */
public interface RoleService {
    public List<String> getRoleByUserId(String userId);
}
