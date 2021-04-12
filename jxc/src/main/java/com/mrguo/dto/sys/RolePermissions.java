package com.mrguo.dto.sys;

import java.util.List;
import java.util.Map;

/**
 * @author 郭成兴
 * @ClassName
 * @Description
 * @date 2019/4/2311:16 AM
 * @updater 郭成兴
 * @updatedate 2019/4/2311:16 AM
 */
public class RolePermissions {

    /**
     * api权限
     */
    private Map<String, List<String>> apiPermissionMsg;
    /**
     * menu权限
     */
    private Map<String, List<String>> menuPermissionMsg;
    /**
     * 数据权限
     */
    private Map<String, List<String>> dataPermissionMsg;

    public Map<String, List<String>> getApiPermissionMsg() {
        return apiPermissionMsg;
    }

    public void setApiPermissionMsg(Map<String, List<String>> apiPermissionMsg) {
        this.apiPermissionMsg = apiPermissionMsg;
    }

    public Map<String, List<String>> getMenuPermissionMsg() {
        return menuPermissionMsg;
    }

    public void setMenuPermissionMsg(Map<String, List<String>> menuPermissionMsg) {
        this.menuPermissionMsg = menuPermissionMsg;
    }

    public Map<String, List<String>> getDataPermissionMsg() {
        return dataPermissionMsg;
    }

    public void setDataPermissionMsg(Map<String, List<String>> dataPermissionMsg) {
        this.dataPermissionMsg = dataPermissionMsg;
    }
}
